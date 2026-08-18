package com.example.client.render;

import com.example.client.cosmetics.SkinManager;
import net.minecraft.class_1011;
import net.minecraft.class_1309;
import net.minecraft.class_332;
import net.minecraft.class_8685;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class CapeRenderer {
   private static final int TEXTURE_WIDTH = 64;
   private static final int TEXTURE_HEIGHT = 32;
   private static boolean capeInitialized = false;

   private CapeRenderer() {
   }

   public static void init() {
      if (!capeInitialized) {
         capeInitialized = true;
      }
   }

   /**
    * Renders the player's cape in 3D space.
    * Must be called during the model rendering phase with the appropriate
    * OpenGL state set up.
    *
    * @param ctx The rendering context (class_332)
    * @param mc The Minecraft client instance
    * @param entity The player entity
    * @param yaw The player's yaw angle
    * @param pitch The player's pitch angle
    */
   public static void renderCape(class_332 ctx, net.minecraft.class_310 mc, class_1309 entity, float yaw, float pitch) {
      if (entity == null) return;

      // Get the player's cape texture
      class_8685 textures = SkinManager.getCurrentSkinTextures();
      if (textures == null) return;

      // Bind the cape texture
      int capeTextureId = getCapeTextureId(textures);
      if (capeTextureId <= 0) return;

      // Save current OpenGL state
      GL11.glPushMatrix();
      GL11.glEnable(3042); // Enable blending
      GL11.glBlendFunc(770, 771); // Src alpha, one minus dst alpha

      // Calculate cape position and size based on entity position
      float capeX = entity.method_7934();  // Get entity X position
      float capeY = entity.method_7934();  // Get entity Y position
      float capeZ = entity.method_7934();  // Get entity Z position

      // Apply cape rotation based on yaw/pitch
      GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);

      // Bind the texture
      bindCapeTexture(capeTextureId);

      // Draw cape as a textured quad
      drawCapeQuad();

      // Restore OpenGL state
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      unbindCapeTexture();
   }

   private static int getCapeTextureId(class_8685 textures) {
      // Try to get the cape texture from the skin textures
      // This may need to be adjusted based on how capes are stored
      return textures.method_74884(null, null, null, null).method_74887(); // Get texture ID
   }

   private static void bindCapeTexture(int textureId) {
      GL13.glActiveTexture(GL13.GL_TEXTURE0);
      GL11.glBindTexture(3553, textureId);
   }

   private static void unbindCapeTexture() {
      GL11.glBindTexture(3553, 0);
   }

   private static void drawCapeQuad() {
      // Draw a textured quad representing the cape
      // Cape texture is typically 64x32 pixels
      float u1 = 0.0F;
      float v1 = 0.0F;
      float u2 = 1.0F;
      float v2 = 1.0F;

      // The cape is draped over the player's back
      // Vertices are positioned behind the player model
      GL11.glBegin(7); // GL_QUADS
      {
         // Top-left of cape
         GL11.glTexCoord2f(u1, v1);
         GL11.glVertex3f(-1.0F, 2.0F, -0.5F);

         // Top-right of cape
         GL11.glTexCoord2f(u2, v1);
         GL11.glVertex3f(1.0F, 2.0F, -0.5F);

         // Bottom-right of cape
         GL11.glTexCoord2f(u2, v2);
         GL11.glVertex3f(1.0F, -1.0F, -0.5F);

         // Bottom-left of cape
         GL11.glTexCoord2f(u1, v2);
         GL11.glVertex3f(-1.0F, -1.0F, -0.5F);
      }
      GL11.glEnd();
   }
}