package com.example.client.ui;

import net.minecraft.class_332;

public class RenderUtils {
   public static void drawSmoothRect(class_332 context, int x, int y, int w, int h, int r, int color) {
      context.method_25294(x + r, y, x + w - r, y + h, color);
      context.method_25294(x, y + r, x + r, y + h - r, color);
      context.method_25294(x + w - r, y + r, x + w, y + h - r, color);
      context.method_25294(x + 1, y + 1, x + r, y + r, color);
      context.method_25294(x + w - r, y + 1, x + w - 1, y + r, color);
      context.method_25294(x + 1, y + h - r, x + r, y + h - 1, color);
      context.method_25294(x + w - r, y + h - r, x + w - 1, y + h - 1, color);
   }

   public static void drawOutline(class_332 context, int x, int y, int w, int h, int r, int thickness, int color) {
      context.method_25294(x + r, y, x + w - r, y + thickness, color);
      context.method_25294(x + r, y + h - thickness, x + w - r, y + h, color);
      context.method_25294(x, y + r, x + thickness, y + h - r, color);
      context.method_25294(x + w - thickness, y + r, x + w, y + h - r, color);
   }

   public static void drawNeonGlow(class_332 context, int x, int y, int w, int h, int size, int color) {
      for(int i = 1; i <= size; ++i) {
         int alpha = (int)((float)(size - i) / (float)size * 35.0F);
         int finalColor = alpha << 24 | color & 16777215;
         drawOutline(context, x - i, y - i, w + i * 2, h + i * 2, 12, 1, finalColor);
      }

   }

   public static void drawPremiumPill(class_332 context, int x, int y, boolean enabled, int accent) {
      drawSmoothRect(context, x, y, 32, 16, 8, enabled ? accent : -13419953);
      int dotX = enabled ? x + 18 : x + 2;
      drawSmoothRect(context, dotX, y + 2, 12, 12, 6, -1);
   }
}
