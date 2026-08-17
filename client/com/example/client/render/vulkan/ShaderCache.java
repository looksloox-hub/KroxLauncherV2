package com.example.client.render.vulkan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Optional;

public final class ShaderCache {
   private final Path path;

   public ShaderCache(Path path) {
      this.path = path;
   }

   public Optional<byte[]> load() {
      try {
         return !Files.exists(this.path, new LinkOption[0]) ? Optional.empty() : Optional.of(Files.readAllBytes(this.path));
      } catch (IOException var2) {
         return Optional.empty();
      }
   }

   public void save(byte[] data) {
      try {
         Path parent = this.path.getParent();
         if (parent != null) {
            Files.createDirectories(parent);
         }

         Files.write(this.path, data, new OpenOption[0]);
      } catch (IOException var3) {
      }

   }
}
