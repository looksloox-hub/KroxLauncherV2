package com.example.client.performance;

import com.example.client.mixin.MinecraftClientAccessor;
import net.minecraft.class_310;
import net.minecraft.class_3565;

public final class ChunkLightingOptimizer {
   private static int terrainCooldown = 0;
   private static int lightCooldown = 0;

   private ChunkLightingOptimizer() {
   }

   public static void tick(class_310 mc) {
      if (mc != null && mc.field_1687 != null) {
         int fps = mc.method_47599();
         int terrainEvery = fps < 40 ? 3 : 1;
         int lightEvery = fps < 40 ? 4 : 2;
         if (++terrainCooldown >= terrainEvery) {
            ((MinecraftClientAccessor)mc).getWorldRenderer();
            terrainCooldown = 0;
         }

         class_3565 lighting = mc.field_1687.method_2935().method_12130();
         if (lighting != null && lighting.method_15518() && ++lightCooldown >= lightEvery) {
            lighting.method_15516();
            lightCooldown = 0;
         }

      }
   }
}
