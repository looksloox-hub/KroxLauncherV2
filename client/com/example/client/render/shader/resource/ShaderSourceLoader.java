package com.example.client.render.shader.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;

public final class ShaderSourceLoader {
   private final ClassLoader classLoader;
   private final Path externalRoot;

   public ShaderSourceLoader(ClassLoader classLoader, Path externalRoot) {
      this.classLoader = (ClassLoader)Objects.requireNonNull(classLoader, "classLoader");
      this.externalRoot = externalRoot;
   }

   public ShaderSource load(String id, String classpathLocation) {
      Objects.requireNonNull(id, "id");
      Objects.requireNonNull(classpathLocation, "classpathLocation");
      if (this.externalRoot != null) {
         Path candidate = this.externalRoot.resolve(classpathLocation.replace('/', File.separatorChar));
         if (Files.isRegularFile(candidate, new LinkOption[0])) {
            try {
               return new ShaderSource(id, Files.readString(candidate, StandardCharsets.UTF_8));
            } catch (IOException ex) {
               throw new IllegalStateException("Failed to read shader source: " + String.valueOf(candidate), ex);
            }
         }
      }

      try {
         InputStream stream = this.classLoader.getResourceAsStream(classpathLocation);

         ShaderSource ex;
         try {
            if (stream == null) {
               throw new IllegalStateException("Missing shader resource: " + classpathLocation);
            }

            ex = new ShaderSource(id, new String(stream.readAllBytes(), StandardCharsets.UTF_8));
         } catch (Throwable var8) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var6) {
                  var8.addSuppressed(var6);
               }
            }

            throw var8;
         }

         if (stream != null) {
            stream.close();
         }

         return ex;
      } catch (IOException ex) {
         throw new IllegalStateException("Failed to load shader resource: " + classpathLocation, ex);
      }
   }
}
