package com.example.client.update;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class OptixUpdateAgent {
   private OptixUpdateAgent() {
   }

   public static void main(String[] args) throws Exception {
      if (args.length >= 3) {
         long pid = Long.parseLong(args[0]);
         Path stagedJar = Paths.get(args[1]);
         Path currentJar = Paths.get(args[2]);

         while((Boolean)ProcessHandle.of(pid).map(ProcessHandle::isAlive).orElse(false)) {
            Thread.sleep(500L);
         }

         Thread.sleep(1000L);
         if (Files.exists(stagedJar, new LinkOption[0])) {
            Path backupJar = currentJar.resolveSibling(currentJar.getFileName().toString() + ".bak");
            Files.createDirectories(currentJar.getParent());
            if (Files.exists(currentJar, new LinkOption[0])) {
               Files.move(currentJar, backupJar, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }

            Files.move(stagedJar, currentJar, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            Files.deleteIfExists(backupJar);
         }
      }
   }
}
