package com.example.client.render.shader.engine;

import java.nio.file.Path;

public record ShaderEngineConfig(Path externalShaderRoot, boolean hotReloadEnabled, float defaultScale) {
   public ShaderEngineConfig() {
      this((Path)null, false, 1.0F);
   }
}
