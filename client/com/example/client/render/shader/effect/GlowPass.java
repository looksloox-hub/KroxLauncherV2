package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class GlowPass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final GaussianBlurPass blurPass;
   private final float strength;
   private final float colorR;
   private final float colorG;
   private final float colorB;
   private final float colorA;

   public GlowPass(ShaderManager shaderManager, int blurIterations, float blurRadius, float strength, float colorR, float colorG, float colorB, float colorA) {
      this.program = shaderManager.program("glow_composite", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/glow_composite.fsh");
      this.blurPass = new GaussianBlurPass(shaderManager, blurIterations, blurRadius, 0.65F);
      this.strength = strength;
      this.colorR = colorR;
      this.colorG = colorG;
      this.colorB = colorB;
      this.colorA = colorA;
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      FramebufferHandle glowBuffer = new FramebufferHandle(output.width(), output.height(), false, true);
      FramebufferHandle blurredBuffer = new FramebufferHandle(output.width(), output.height(), false, true);

      try {
         input.blitTo(glowBuffer);
         this.blurPass.apply(context, glowBuffer, blurredBuffer);
         output.bind();
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glDisable(3042);
         this.program.bind();
         GL13.glActiveTexture(33984);
         GL11.glBindTexture(3553, input.colorTextureId());
         this.program.setTexture("uTexture", 0);
         GL13.glActiveTexture(33985);
         GL11.glBindTexture(3553, blurredBuffer.colorTextureId());
         this.program.setTexture("uTexture1", 1);
         this.program.setUniform2f("uResolution", (float)output.width(), (float)output.height());
         this.program.setUniform1f("uStrength", this.strength);
         this.program.setUniform4f("uGlowColor", this.colorR, this.colorG, this.colorB, this.colorA);
         FullscreenQuadRenderer.draw();
         GL11.glBindTexture(3553, 0);
         GL13.glActiveTexture(33984);
         this.program.unbind();
      } finally {
         glowBuffer.close();
         blurredBuffer.close();
      }

   }
}
