package com.example.client.performance;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4184;

public final class RenderCore {
   private static int frameSkip = 0;

   private RenderCore() {
   }

   public static void onRenderTick(class_310 mc, class_4184 camera) {
      if (mc != null && mc.field_1687 != null && camera != null) {
         ++frameSkip;
         int fps = mc.method_47599();
         if (fps >= 40 || frameSkip % 2 == 0) {
            if (fps >= 25 || frameSkip % 3 == 0) {
               class_243 camPos = camera.method_71156();

               for(class_1297 e : mc.field_1687.method_18112()) {
                  if (e != null && !e.method_7325() && !e.method_5767()) {
                     double dx = e.method_23317() - camPos.field_1352;
                     double dy = e.method_23318() - camPos.field_1351;
                     double dz = e.method_23321() - camPos.field_1350;
                     double distSq = dx * dx + dy * dy + dz * dz;
                     if (distSq > (double)9216.0F) {
                     }
                  }
               }

            }
         }
      }
   }
}
