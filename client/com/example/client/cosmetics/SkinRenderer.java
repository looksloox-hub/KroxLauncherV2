package com.example.client.cosmetics;

import net.minecraft.class_1309;
import net.minecraft.class_332;
import net.minecraft.class_490;

public final class SkinRenderer {
   private SkinRenderer() {
   }

   public static void renderPreview(class_332 context, int x, int y, int size, float animation, class_1309 entity) {
      if (entity != null) {
         try {
            float bodyYaw = (float)Math.sin((double)(animation * 0.01F)) * 15.0F;
            float yaw = (float)Math.sin((double)(animation * 0.015F)) * 25.0F;
            class_490.method_2486(context, x, y, size, 0, 0, bodyYaw, yaw, 0.0F, entity);
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }
}
