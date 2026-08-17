package com.example.client.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.fabricmc.loader.api.FabricLoader;

public class DownloadManager {
   private static final ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue();
   private static final Map<String, Task> active = new ConcurrentHashMap();

   public static void enqueue(String projectId, String url, String fileName, String installFolder) {
      Task t = new Task();
      t.projectId = projectId;
      t.url = url;
      t.fileName = fileName;
      t.installFolder = installFolder;
      queue.add(t);
      process();
   }

   public static Task get(String projectId) {
      return (Task)active.get(projectId);
   }

   private static void process() {
      Task task = (Task)queue.poll();
      if (task != null) {
         active.put(task.projectId, task);
         (new Thread(() -> run(task))).start();
      }
   }

   private static void run(Task task) {
      try {
         Path folder = FabricLoader.getInstance().getGameDir().resolve(task.installFolder);
         Files.createDirectories(folder);
         Path target = folder.resolve(task.fileName);
         URLConnection conn = (new URL(task.url)).openConnection();
         long size = conn.getContentLengthLong();
         InputStream in = conn.getInputStream();

         try {
            OutputStream out = Files.newOutputStream(target);

            try {
               byte[] buf = new byte[8192];
               long total = 0L;

               int r;
               while((r = in.read(buf)) != -1) {
                  out.write(buf, 0, r);
                  total += (long)r;
                  if (size > 0L) {
                     task.progress = (float)total / (float)size;
                  }
               }
            } catch (Throwable var14) {
               if (out != null) {
                  try {
                     out.close();
                  } catch (Throwable var13) {
                     var14.addSuppressed(var13);
                  }
               }

               throw var14;
            }

            if (out != null) {
               out.close();
            }
         } catch (Throwable var15) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }
            }

            throw var15;
         }

         if (in != null) {
            in.close();
         }

         task.progress = 1.0F;
         task.done = true;
      } catch (Exception e) {
         task.failed = true;
         e.printStackTrace();
      }

      process();
   }

   public static class Task {
      public String projectId;
      public String url;
      public String fileName;
      public String installFolder = "mods";
      public float progress = 0.0F;
      public boolean done = false;
      public boolean failed = false;
   }
}
