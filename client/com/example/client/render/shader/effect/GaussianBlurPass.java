package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.framebuffer.PingPongFramebuffer;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class GaussianBlurPass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final int iterations;
   private final float radius;
   private final float softness;

   public GaussianBlurPass(ShaderManager shaderManager, int iterations, float radius, float softness) {
      this.program = shaderManager.program("gaussian_blur", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/gaussian_blur.fsh");
      this.iterations = Math.max(1, iterations);
      this.radius = Math.max(0.0F, radius);
      this.softness = Math.max(0.0F, softness);
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      int width = output.width();
      int height = output.height();
      PingPongFramebuffer pingPong = new PingPongFramebuffer(width, height, false, true);
      FramebufferHandle currentInput = input;

      for(int i = 0; i < this.iterations; ++i) {
         FramebufferHandle currentOutput = i == this.iterations - 1 ? output : pingPong.write();
         currentOutput.resize(width, height);
         currentOutput.bind();
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glDisable(3042);
         this.program.bind();
         GL13.glActiveTexture(33984);
         GL11.glBindTexture(3553, currentInput.colorTextureId());
         this.program.setTexture("uTexture", 0);
         this.program.setUniform2f("uResolution", (float)width, (float)height);
         this.program.setUniform1f("uRadius", this.radius);
         this.program.setUniform1f("uSoftness", this.softness);
         this.program.setUniform1f("uTime", context.timeSeconds());
         this.program.setUniform1f("uDelta", context.deltaSeconds());
         this.program.setUniform2f("uDirection", (i & 1) == 0 ? 1.0F : 0.0F, (i & 1) == 0 ? 0.0F : 1.0F);
         FullscreenQuadRenderer.draw();
         GL11.glBindTexture(3553, 0);
         this.program.unbind();
         currentInput = currentOutput;
         if (i < this.iterations - 1) {
            pingPong.swap();
         }
      }

      pingPong.close();
   }
}
