package com.example.client.performance;

import net.minecraft.class_310;
import net.minecraft.class_4063;
import net.minecraft.class_4066;

public final class RenderPipelineOptimizer {
   private static int tickCounter = 0;

   public static void tick(class_310 mc) {
      if (mc != null && mc.field_1690 != null) {
         ++tickCounter;
         int fps = mc.method_47599();
         if (fps < 40) {
            applyLow(mc);
         } else if (fps < 70) {
            applyMedium(mc);
         } else {
            applyHigh(mc);
         }

         if (tickCounter % 10 == 0) {
            DynamicResolutionFramebuffer.updateScale(getScale(fps));
            DynamicResolutionFramebuffer.apply();
         }

      }
   }

   private static float getScale(int fps) {
      if (fps < 40) {
         return 0.55F;
      } else {
         return fps < 70 ? 0.75F : 1.0F;
      }
   }

   private static void applyLow(class_310 mc) {
      mc.field_1690.method_42503().method_41748(4);
      mc.field_1690.method_42510().method_41748(4);
      mc.field_1690.method_42475().method_41748(class_4066.field_18199);
      mc.field_1690.method_42528().method_41748(class_4063.field_18162);
      mc.field_1690.method_42563().method_41748(0);
      mc.field_1690.method_42517().method_41748((double)0.5F);
   }

   private static void applyMedium(class_310 mc) {
      mc.field_1690.method_42503().method_41748(6);
      mc.field_1690.method_42510().method_41748(5);
      mc.field_1690.method_42475().method_41748(class_4066.field_18198);
      mc.field_1690.method_42528().method_41748(class_4063.field_18162);
      mc.field_1690.method_42563().method_41748(1);
      mc.field_1690.method_42517().method_41748((double)0.75F);
   }

   private static void applyHigh(class_310 mc) {
      mc.field_1690.method_42503().method_41748(10);
      mc.field_1690.method_42510().method_41748(8);
      mc.field_1690.method_42475().method_41748(class_4066.field_18197);
      mc.field_1690.method_42528().method_41748(class_4063.field_18164);
      mc.field_1690.method_42563().method_41748(4);
      mc.field_1690.method_42517().method_41748((double)1.0F);
   }
}
