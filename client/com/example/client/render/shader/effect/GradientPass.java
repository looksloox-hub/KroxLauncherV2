package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;

public final class GradientPass implements ShaderPass {
   private final OpenGLShaderProgram program;
   private final float[] colorA;
   private final float[] colorB;
   private final float angle;

   public GradientPass(ShaderManager shaderManager, float[] colorA, float[] colorB, float angle) {
      this.program = shaderManager.program("gradient", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/gradient.fsh");
      this.colorA = (float[])(([F)colorA).clone();
      this.colorB = (float[])(([F)colorB).clone();
      this.angle = angle;
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      output.bind();
      GL11.glDisable(2929);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      this.program.bind();
      this.program.setUniform2f("uResolution", (float)output.width(), (float)output.height());
      this.program.setUniform4f("uColorA", this.colorA[0], this.colorA[1], this.colorA[2], this.colorA.length > 3 ? this.colorA[3] : 1.0F);
      this.program.setUniform4f("uColorB", this.colorB[0], this.colorB[1], this.colorB[2], this.colorB.length > 3 ? this.colorB[3] : 1.0F);
      this.program.setUniform1f("uAngle", this.angle);
      FullscreenQuadRenderer.draw();
      this.program.unbind();
   }
}
