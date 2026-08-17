package com.example.client.render.shader.core;

public final class ShaderCompileException extends IllegalStateException {
   public ShaderCompileException(String message) {
      super(message);
   }

   public ShaderCompileException(String message, Throwable cause) {
      super(message, cause);
   }
}
