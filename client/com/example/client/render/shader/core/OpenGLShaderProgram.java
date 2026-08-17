package com.example.client.render.shader.core;

import com.example.client.render.shader.resource.ShaderSource;
import com.example.client.render.shader.resource.ShaderSourceLoader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.lwjgl.opengl.GL20;

public final class OpenGLShaderProgram implements AutoCloseable {
   private final int programId;
   private final String name;
   private final Map<String, Integer> uniformLocations = new ConcurrentHashMap();

   public OpenGLShaderProgram(String name, ShaderSource vertexSource, ShaderSource fragmentSource) {
      this.name = (String)Objects.requireNonNull(name, "name");
      int vertexShader = compile(35633, vertexSource);
      int fragmentShader = compile(35632, fragmentSource);
      this.programId = GL20.glCreateProgram();
      GL20.glAttachShader(this.programId, vertexShader);
      GL20.glAttachShader(this.programId, fragmentShader);
      GL20.glBindAttribLocation(this.programId, 0, "Position");
      GL20.glBindAttribLocation(this.programId, 1, "UV0");
      GL20.glLinkProgram(this.programId);
      if (GL20.glGetProgrami(this.programId, 35714) == 0) {
         String log = GL20.glGetProgramInfoLog(this.programId, 32768);
         GL20.glDeleteShader(vertexShader);
         GL20.glDeleteShader(fragmentShader);
         GL20.glDeleteProgram(this.programId);
         throw new ShaderCompileException("Failed to link shader program '" + name + "': " + log);
      } else {
         GL20.glDetachShader(this.programId, vertexShader);
         GL20.glDetachShader(this.programId, fragmentShader);
         GL20.glDeleteShader(vertexShader);
         GL20.glDeleteShader(fragmentShader);
      }
   }

   public static OpenGLShaderProgram load(String name, ShaderSourceLoader loader, String vertexPath, String fragmentPath) {
      return new OpenGLShaderProgram(name, loader.load(name + ":vertex", vertexPath), loader.load(name + ":fragment", fragmentPath));
   }

   private static int compile(int type, ShaderSource source) {
      int shaderId = GL20.glCreateShader(type);
      GL20.glShaderSource(shaderId, source.source());
      GL20.glCompileShader(shaderId);
      if (GL20.glGetShaderi(shaderId, 35713) == 0) {
         String log = GL20.glGetShaderInfoLog(shaderId, 32768);
         GL20.glDeleteShader(shaderId);
         String var10002 = source.id();
         throw new ShaderCompileException("Failed to compile shader '" + var10002 + "': " + log);
      } else {
         return shaderId;
      }
   }

   public int id() {
      return this.programId;
   }

   public String name() {
      return this.name;
   }

   public void bind() {
      GL20.glUseProgram(this.programId);
   }

   public void unbind() {
      GL20.glUseProgram(0);
   }

   public int uniformLocation(String uniformName) {
      return (Integer)this.uniformLocations.computeIfAbsent(uniformName, (key) -> GL20.glGetUniformLocation(this.programId, key));
   }

   public boolean hasUniform(String uniformName) {
      return this.uniformLocation(uniformName) >= 0;
   }

   public void setUniform1i(String uniformName, int value) {
      int location = this.uniformLocation(uniformName);
      if (location >= 0) {
         GL20.glUniform1i(location, value);
      }

   }

   public void setUniform1f(String uniformName, float value) {
      int location = this.uniformLocation(uniformName);
      if (location >= 0) {
         GL20.glUniform1f(location, value);
      }

   }

   public void setUniform2f(String uniformName, float x, float y) {
      int location = this.uniformLocation(uniformName);
      if (location >= 0) {
         GL20.glUniform2f(location, x, y);
      }

   }

   public void setUniform3f(String uniformName, float x, float y, float z) {
      int location = this.uniformLocation(uniformName);
      if (location >= 0) {
         GL20.glUniform3f(location, x, y, z);
      }

   }

   public void setUniform4f(String uniformName, float x, float y, float z, float w) {
      int location = this.uniformLocation(uniformName);
      if (location >= 0) {
         GL20.glUniform4f(location, x, y, z, w);
      }

   }

   public void setTexture(String uniformName, int textureUnit) {
      this.setUniform1i(uniformName, textureUnit);
   }

   public void close() {
      GL20.glDeleteProgram(this.programId);
   }
}
