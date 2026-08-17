package com.example.client.performance;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class GpuBatchRenderer {
   private static final Queue<Runnable> QUEUE = new ConcurrentLinkedQueue();

   private GpuBatchRenderer() {
   }

   public static void submit(Runnable renderTask) {
      if (renderTask != null) {
         QUEUE.add(renderTask);
      }

   }

   public static void flush() {
      Runnable task;
      while((task = (Runnable)QUEUE.poll()) != null) {
         task.run();
      }

   }

   public static void clear() {
      QUEUE.clear();
   }
}
