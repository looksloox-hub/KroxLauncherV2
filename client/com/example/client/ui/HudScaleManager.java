package com.example.client.ui;

import net.minecraft.class_310;
import net.minecraft.class_332;

public final class HudScaleManager {
   public static final int VIRTUAL_WIDTH = 1920;
   public static final int VIRTUAL_HEIGHT = 1080;
   private static float scaleX = 1.0F;
   private static float scaleY = 1.0F;

   private HudScaleManager() {
   }

   private static void refreshFactors() {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.method_22683() != null) {
         int screenW = Math.max(1, mc.method_22683().method_4486());
         int screenH = Math.max(1, mc.method_22683().method_4502());
         scaleX = (float)screenW / 1920.0F;
         scaleY = (float)screenH / 1080.0F;
      } else {
         scaleX = 1.0F;
         scaleY = 1.0F;
      }
   }

   public static void begin(class_332 context) {
      refreshFactors();
      context.method_51448().pushMatrix();
      context.method_51448().scale(scaleX, scaleY);
   }

   public static void end(class_332 context) {
      context.method_51448().popMatrix();
   }

   public static int toHudX(double mouseX) {
      refreshFactors();
      return scaleX == 0.0F ? (int)mouseX : Math.round((float)(mouseX / (double)scaleX));
   }

   public static int toHudY(double mouseY) {
      refreshFactors();
      return scaleY == 0.0F ? (int)mouseY : Math.round((float)(mouseY / (double)scaleY));
   }

   public static int getVirtualWidth() {
      return 1920;
   }

   public static int getVirtualHeight() {
      return 1080;
   }
}
