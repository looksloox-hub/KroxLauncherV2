package com.example.client.performance;

import net.minecraft.class_276;
import net.minecraft.class_310;

public final class DynamicResolutionFramebuffer {
   private static int lastW = -1;
   private static int lastH = -1;
   private static float scale = 1.0F;

   private DynamicResolutionFramebuffer() {
   }

   public static void updateScale(float newScale) {
      scale = Math.max(0.5F, Math.min(1.0F, newScale));
   }

   public static float getScale() {
      return scale;
   }

   public static void apply() {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.field_1687 != null && mc.method_22683() != null && mc.method_1522() != null) {
         class_276 fb = mc.method_1522();
         int w = Math.max(320, Math.round((float)mc.method_22683().method_4489() * scale));
         int h = Math.max(180, Math.round((float)mc.method_22683().method_4506() * scale));
         if (w != lastW || h != lastH) {
            fb.method_1234(w, h);
            lastW = w;
            lastH = h;
         }
      }
   }
}
