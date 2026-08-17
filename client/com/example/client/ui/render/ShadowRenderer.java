package com.example.client.ui.render;

import com.example.client.ui.theme.GlassRadius;
import com.example.client.ui.util.ColorUtil;
import net.minecraft.class_332;

public final class ShadowRenderer {
   private ShadowRenderer() {
   }

   public static void drawSoftShadow(class_332 context, float x, float y, float width, float height, float radius, int shadowColor, float strength) {
      float resolvedRadius = GlassRadius.resolve(radius, width, height);
      float alphaScale = Math.max(0.0F, strength);

      for(int i = 5; i >= 1; --i) {
         float expand = (float)i * 2.0F;
         float alpha = alphaScale * ((float)i / 5.0F) * 0.28F;
         int color = ColorUtil.scaleAlpha(shadowColor, alpha);
         RoundedRenderer.draw(context, x - expand, y - expand, width + expand * 2.0F, height + expand * 2.0F, resolvedRadius + expand, color);
      }

   }
}
