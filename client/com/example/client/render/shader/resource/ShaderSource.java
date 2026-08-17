package com.example.client.render.shader.resource;

import java.util.Objects;

public record ShaderSource(String id, String source) {
   public ShaderSource {
      Objects.requireNonNull(id, "id");
      Objects.requireNonNull(source, "source");
   }
}
