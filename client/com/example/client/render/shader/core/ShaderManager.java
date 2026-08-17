package com.example.client.render.shader.core;

import com.example.client.render.shader.resource.ShaderDescriptor;
import com.example.client.render.shader.resource.ShaderSourceLoader;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ShaderManager implements AutoCloseable {
   private final ShaderSourceLoader sourceLoader;
   private final Map<ShaderDescriptor, OpenGLShaderProgram> cache;

   public ShaderManager(Path externalRoot) {
      this.cache = new ConcurrentHashMap();
      this.sourceLoader = new ShaderSourceLoader(Thread.currentThread().getContextClassLoader(), externalRoot);
   }

   public ShaderManager() {
      this((Path)null);
   }

   public OpenGLShaderProgram program(ShaderDescriptor descriptor) {
      Objects.requireNonNull(descriptor, "descriptor");
      return (OpenGLShaderProgram)this.cache.computeIfAbsent(descriptor, (key) -> OpenGLShaderProgram.load(key.name(), this.sourceLoader, key.vertexPath(), key.fragmentPath()));
   }

   public OpenGLShaderProgram program(String name, String vertexPath, String fragmentPath) {
      return this.program(new ShaderDescriptor(name, vertexPath, fragmentPath));
   }

   public void reloadAll() {
      this.cache.values().forEach(OpenGLShaderProgram::close);
      this.cache.clear();
   }

   public void close() {
      this.reloadAll();
   }
}
