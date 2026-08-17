package com.example.client.ui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;

public class AnimatedIconRenderer {
   public static void draw(class_332 context, class_2960 icon, int x, int y, boolean hovered, boolean enabled, float anim) {
      class_310 mc = class_310.method_1551();
      float time = (float)(System.currentTimeMillis() % 2000L) / 2000.0F;
      float pulse = (float)Math.sin((double)time * Math.PI * (double)2.0F) * 0.5F + 0.5F;
      float glow = enabled ? 1.0F : anim;
      int glowAlpha = (int)(60.0F + glow * 120.0F);
      context.method_25294(x - 3, y - 3, x + 19, y + 19, glowAlpha << 24 | 9133302);
      context.method_52706((RenderPipeline)null, icon, x, y, 16, 16);
      if (hovered || enabled) {
         int shine = (int)(pulse * 80.0F);
         context.method_25294(x, y, x + 16, y + 16, shine << 24 | 16777215);
      }

   }
}
