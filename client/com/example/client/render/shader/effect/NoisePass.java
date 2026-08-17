package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class NoisePass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final float intensity;
   private final float scale;

   public NoisePass(ShaderManager shaderManager, float intensity, float scale) {
      this.program = shaderManager.program("noise", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/noise.fsh");
      this.intensity = intensity;
      this.scale = scale;
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      output.bind();
      GL11.glDisable(2929);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      this.program.bind();
      GL13.glActiveTexture(33984);
      GL11.glBindTexture(3553, input.colorTextureId());
      this.program.setTexture("uTexture", 0);
      this.program.setUniform2f("uResolution", (float)output.width(), (float)output.height());
      this.program.setUniform1f("uTime", context.timeSeconds());
      this.program.setUniform1f("uNoiseScale", this.scale);
      this.program.setUniform1f("uStrength", this.intensity);
      FullscreenQuadRenderer.draw();
      GL11.glBindTexture(3553, 0);
      this.program.unbind();
   }
}
