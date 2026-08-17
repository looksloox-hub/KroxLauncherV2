package com.example.client.renderer.vulkan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Optional;

public final class VulkanPipelineCache {
   private final Path cacheFile;

   public VulkanPipelineCache(Path cacheFile) {
      this.cacheFile = cacheFile;
   }

   public Optional<byte[]> load() {
      try {
         return !Files.exists(this.cacheFile, new LinkOption[0]) ? Optional.empty() : Optional.of(Files.readAllBytes(this.cacheFile));
      } catch (IOException var2) {
         return Optional.empty();
      }
   }

   public void save(byte[] data) {
      try {
         Path parent = this.cacheFile.getParent();
         if (parent != null) {
            Files.createDirectories(parent);
         }

         Files.write(this.cacheFile, data, new OpenOption[0]);
      } catch (IOException var3) {
      }

   }
}
