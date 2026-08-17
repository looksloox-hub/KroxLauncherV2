package com.example.client.render.shader.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public final class FullscreenQuadRenderer {
   private static final int VAO;
   private static final int VBO;

   private FullscreenQuadRenderer() {
   }

   public static void draw() {
      GL30.glBindVertexArray(VAO);
      GL11.glDrawArrays(4, 0, 6);
      GL30.glBindVertexArray(0);
   }

   static {
      float[] vertices = new float[]{-1.0F, -1.0F, 0.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, -1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F};
      VAO = GL30.glGenVertexArrays();
      VBO = GL15.glGenBuffers();
      GL30.glBindVertexArray(VAO);
      GL15.glBindBuffer(34962, VBO);
      GL15.glBufferData(34962, vertices, 35044);
      GL20.glEnableVertexAttribArray(0);
      GL20.glVertexAttribPointer(0, 2, 5126, false, 16, 0L);
      GL20.glEnableVertexAttribArray(1);
      GL20.glVertexAttribPointer(1, 2, 5126, false, 16, 8L);
      GL15.glBindBuffer(34962, 0);
      GL30.glBindVertexArray(0);
   }
}
