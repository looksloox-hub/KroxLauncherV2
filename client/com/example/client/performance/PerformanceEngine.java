package com.example.client.performance;

import net.minecraft.class_310;
import net.minecraft.class_4063;
import net.minecraft.class_4066;

public final class PerformanceEngine {
   private static volatile int entityCullDistanceSq = 9216;
   private static volatile int blockEntityCullDistanceSq = 2304;
   private static volatile int targetViewDistance = 10;
   private static volatile int targetSimulationDistance = 8;
   private static volatile float targetScale = 1.0F;

   private PerformanceEngine() {
   }

   public static void tick(class_310 mc) {
      if (mc != null && mc.field_1690 != null) {
         int fps = mc.method_47599();
         if (mc.field_1687 == null) {
            entityCullDistanceSq = 9216;
            blockEntityCullDistanceSq = 2304;
            targetViewDistance = 10;
            targetSimulationDistance = 8;
            targetScale = 1.0F;
            DynamicResolutionFramebuffer.updateScale(targetScale);
            DynamicResolutionFramebuffer.apply();
         } else {
            if (fps < 35) {
               entityCullDistanceSq = 1024;
               blockEntityCullDistanceSq = 256;
               targetViewDistance = 4;
               targetSimulationDistance = 4;
               targetScale = 0.55F;
            } else if (fps < 50) {
               entityCullDistanceSq = 2304;
               blockEntityCullDistanceSq = 576;
               targetViewDistance = 6;
               targetSimulationDistance = 4;
               targetScale = 0.7F;
            } else if (fps < 75) {
               entityCullDistanceSq = 4096;
               blockEntityCullDistanceSq = 1024;
               targetViewDistance = 8;
               targetSimulationDistance = 6;
               targetScale = 0.85F;
            } else {
               entityCullDistanceSq = 9216;
               blockEntityCullDistanceSq = 2304;
               targetViewDistance = 10;
               targetSimulationDistance = 8;
               targetScale = 1.0F;
            }

            if ((Integer)mc.field_1690.method_42503().method_41753() != targetViewDistance) {
               mc.field_1690.method_42503().method_41748(targetViewDistance);
            }

            if ((Integer)mc.field_1690.method_42510().method_41753() != targetSimulationDistance) {
               mc.field_1690.method_42510().method_41748(targetSimulationDistance);
            }

            if (mc.field_1690.method_42475().method_41753() != class_4066.field_18199) {
               mc.field_1690.method_42475().method_41748(class_4066.field_18199);
            }

            if ((Integer)mc.field_1690.method_42563().method_41753() != 0) {
               mc.field_1690.method_42563().method_41748(0);
            }

            if (mc.field_1690.method_42528().method_41753() != class_4063.field_18162) {
               mc.field_1690.method_42528().method_41748(class_4063.field_18162);
            }

            if ((Double)mc.field_1690.method_42517().method_41753() != (double)0.5F) {
               mc.field_1690.method_42517().method_41748((double)0.5F);
            }

            DynamicResolutionFramebuffer.updateScale(targetScale);
            DynamicResolutionFramebuffer.apply();
         }
      }
   }

   public static int getEntityCullDistanceSq() {
      return entityCullDistanceSq;
   }

   public static int getBlockEntityCullDistanceSq() {
      return blockEntityCullDistanceSq;
   }

   public static float getTargetScale() {
      return targetScale;
   }
}
