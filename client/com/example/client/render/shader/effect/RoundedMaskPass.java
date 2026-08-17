package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class RoundedMaskPass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final float radius;
   private final float softness;

   public RoundedMaskPass(ShaderManager shaderManager, float radius, float softness) {
      this.program = shaderManager.program("rounded_mask", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/rounded_mask.fsh");
      this.radius = radius;
      this.softness = softness;
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
      this.program.setUniform1f("uRadius", this.radius);
      this.program.setUniform1f("uSoftness", this.softness);
      FullscreenQuadRenderer.draw();
      GL11.glBindTexture(3553, 0);
      this.program.unbind();
   }
}
