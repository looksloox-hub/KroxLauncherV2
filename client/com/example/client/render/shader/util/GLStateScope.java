package com.example.client.render.shader.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public final class GLStateScope implements AutoCloseable {
   private final int previousProgram = GL20.glGetInteger(35725);
   private final int previousFramebuffer = GL11.glGetInteger(36006);
   private final int previousVAO = GL11.glGetInteger(34229);
   private final int previousActiveTexture = GL11.glGetInteger(34016);
   private final boolean blendEnabled = GL11.glIsEnabled(3042);
   private final boolean depthEnabled = GL11.glIsEnabled(2929);
   private final boolean scissorEnabled = GL11.glIsEnabled(3089);

   private GLStateScope() {
   }

   public static GLStateScope capture() {
      return new GLStateScope();
   }

   public void close() {
      if (this.blendEnabled) {
         GL11.glEnable(3042);
      } else {
         GL11.glDisable(3042);
      }

      if (this.depthEnabled) {
         GL11.glEnable(2929);
      } else {
         GL11.glDisable(2929);
      }

      if (this.scissorEnabled) {
         GL11.glEnable(3089);
      } else {
         GL11.glDisable(3089);
      }

      GL20.glUseProgram(this.previousProgram);
      GL30.glBindVertexArray(this.previousVAO);
      GL30.glBindFramebuffer(36160, this.previousFramebuffer);
      GL13.glActiveTexture(this.previousActiveTexture);
   }
}
