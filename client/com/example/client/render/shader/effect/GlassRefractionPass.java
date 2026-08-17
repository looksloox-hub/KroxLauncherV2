package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class GlassRefractionPass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final float refraction;
   private final float noiseScale;
   private final float distortion;

   public GlassRefractionPass(ShaderManager shaderManager, float refraction, float noiseScale, float distortion) {
      this.program = shaderManager.program("glass_refraction", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/glass_refraction.fsh");
      this.refraction = refraction;
      this.noiseScale = noiseScale;
      this.distortion = distortion;
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
      this.program.setUniform1f("uRefraction", this.refraction);
      this.program.setUniform1f("uNoiseScale", this.noiseScale);
      this.program.setUniform1f("uStrength", this.distortion);
      this.program.setUniform1f("uTime", context.timeSeconds());
      FullscreenQuadRenderer.draw();
      GL11.glBindTexture(3553, 0);
      this.program.unbind();
   }
}
