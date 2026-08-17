package com.example.client.render.shader.effect;

import com.example.client.render.shader.core.OpenGLShaderProgram;
import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.pipeline.ShaderContext;
import com.example.client.render.shader.pipeline.ShaderPass;
import com.example.client.render.shader.util.FullscreenQuadRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class DualKawaseBlurPass implements ShaderPass {
   private final OpenGLShaderProgram downsampleProgram;
   private final OpenGLShaderProgram upsampleProgram;
   private final int iterations;
   private final float baseOffset;

   public DualKawaseBlurPass(ShaderManager shaderManager, int iterations, float baseOffset) {
      this.downsampleProgram = shaderManager.program("dual_kawase_down", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/dual_kawase_down.fsh");
      this.upsampleProgram = shaderManager.program("dual_kawase_up", "assets/exampleclient/shaders/core/fullscreen.vsh", "assets/exampleclient/shaders/effects/dual_kawase_up.fsh");
      this.iterations = Math.max(1, iterations);
      this.baseOffset = Math.max(0.0F, baseOffset);
   }

   public void apply(ShaderContext context, FramebufferHandle input, FramebufferHandle output) {
      FramebufferHandle[] levels = new FramebufferHandle[this.iterations];
      boolean var15 = false;

      try {
         var15 = true;
         FramebufferHandle current = input;
         int width = Math.max(1, output.width());
         int height = Math.max(1, output.height());

         for(int i = 0; i < this.iterations; ++i) {
            width = Math.max(1, width / 2);
            height = Math.max(1, height / 2);
            levels[i] = new FramebufferHandle(width, height, false, true);
            this.renderStage(levels[i], current, this.downsampleProgram, this.baseOffset + (float)i);
            current = levels[i];
         }

         for(int i = this.iterations - 2; i >= 0; --i) {
            this.renderStage(levels[i], current, this.upsampleProgram, this.baseOffset + (float)i);
            current = levels[i];
         }

         this.renderStage(output, current, this.upsampleProgram, this.baseOffset);
         var15 = false;
      } finally {
         if (var15) {
            FramebufferHandle[] var10 = levels;
            int var11 = levels.length;
            int var12 = 0;

            while(true) {
               if (var12 >= var11) {
                  ;
               } else {
                  FramebufferHandle level = var10[var12];
                  if (level != null) {
                     level.close();
                  }

                  ++var12;
               }
            }
         }
      }

      for(FramebufferHandle level : levels) {
         if (level != null) {
            level.close();
         }
      }

   }

   private void renderStage(FramebufferHandle target, FramebufferHandle source, OpenGLShaderProgram program, float offset) {
      target.bind();
      this.setupCommon(target, source, program, offset);
   }

   private void setupCommon(FramebufferHandle target, FramebufferHandle source, OpenGLShaderProgram program, float offset) {
      GL11.glDisable(2929);
      GL11.glDisable(2884);
      GL11.glDisable(3042);
      program.bind();
      GL13.glActiveTexture(33984);
      GL11.glBindTexture(3553, source.colorTextureId());
      program.setTexture("uTexture", 0);
      program.setUniform2f("uResolution", (float)target.width(), (float)target.height());
      program.setUniform1f("uOffset", offset);
      FullscreenQuadRenderer.draw();
      GL11.glBindTexture(3553, 0);
      program.unbind();
   }
}
