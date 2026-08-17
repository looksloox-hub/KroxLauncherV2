package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class BloomPass implements ShaderPass {
   private final OpenGLShaderProgram thresholdProgram;
   private final OpenGLShaderProgram compositeProgram;
   private final GaussianBlurPass blurPass;
   private final float threshold;
   private final float exposure;

   public BloomPass(ShaderManager shaderManager, int blurIterations, float blurRadius, float threshold, float exposure) {
      this.thresholdProgram = shaderManager.program("bloom_threshold", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/bloom_threshold.fsh");
      this.compositeProgram = shaderManager.program("bloom_composite", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/bloom_composite.fsh");
      this.blurPass = new GaussianBlurPass(shaderManager, blurIterations, blurRadius, 0.6F);
      this.threshold = threshold;
      this.exposure = exposure;
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      FramebufferHandle bloomBuffer = new FramebufferHandle(output.width(), output.height(), false, true);
      FramebufferHandle blurredBuffer = new FramebufferHandle(output.width(), output.height(), false, true);

      try {
         bloomBuffer.bind();
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glDisable(3042);
         this.thresholdProgram.bind();
         GL13.glActiveTexture(33984);
         GL11.glBindTexture(3553, input.colorTextureId());
         this.thresholdProgram.setTexture("uTexture", 0);
         this.thresholdProgram.setUniform2f("uResolution", (float)output.width(), (float)output.height());
         this.thresholdProgram.setUniform1f("uThreshold", this.threshold);
         FullscreenQuadRenderer.draw();
         GL11.glBindTexture(3553, 0);
         this.thresholdProgram.unbind();
         this.blurPass.apply(context, bloomBuffer, blurredBuffer);
         output.bind();
         this.compositeProgram.bind();
         GL13.glActiveTexture(33984);
         GL11.glBindTexture(3553, input.colorTextureId());
         this.compositeProgram.setTexture("uTexture", 0);
         GL13.glActiveTexture(33985);
         GL11.glBindTexture(3553, blurredBuffer.colorTextureId());
         this.compositeProgram.setTexture("uTexture1", 1);
         this.compositeProgram.setUniform2f("uResolution", (float)output.width(), (float)output.height());
         this.compositeProgram.setUniform1f("uExposure", this.exposure);
         FullscreenQuadRenderer.draw();
         GL11.glBindTexture(3553, 0);
         GL13.glActiveTexture(33984);
         this.compositeProgram.unbind();
      } finally {
         bloomBuffer.close();
         blurredBuffer.close();
      }

   }
}
