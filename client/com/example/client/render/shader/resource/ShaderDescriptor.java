package com.example.client.render.shader.resource;

import java.util.Objects;

public record ShaderDescriptor(String name, String vertexPath, String fragmentPath) {
   public ShaderDescriptor {
      Objects.requireNonNull(name, "name");
      Objects.requireNonNull(vertexPath, "vertexPath");
      Objects.requireNonNull(fragmentPath, "fragmentPath");
   }
}
