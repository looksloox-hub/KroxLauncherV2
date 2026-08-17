package com.example.client.ui.render;

import com.example.client.ui.theme.GlassRadius;
import net.minecraft.class_332;

public final class RoundedOutlineRenderer {
   private RoundedOutlineRenderer() {
   }

   public static void drawBordered(class_332 context, float x, float y, float width, float height, float radius, int fillColor, int borderColor, int borderThickness) {
      int thickness = Math.max(1, borderThickness);
      float resolvedRadius = GlassRadius.resolve(radius, width, height);
      RoundedRenderer.draw(context, x, y, width, height, resolvedRadius, borderColor);
      float innerX = x + (float)thickness;
      float innerY = y + (float)thickness;
      float innerW = Math.max(0.0F, width - (float)thickness * 2.0F);
      float innerH = Math.max(0.0F, height - (float)thickness * 2.0F);
      if (innerW > 0.0F && innerH > 0.0F) {
         RoundedRenderer.draw(context, innerX, innerY, innerW, innerH, Math.max(0.0F, resolvedRadius - (float)thickness), fillColor);
      }

   }
}
