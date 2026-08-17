package com.example.client.util;

import net.minecraft.class_332;

public class RenderUtil {
   public static void rounded(class_332 ctx, int x, int y, int w, int h, int color) {
      ctx.method_25294(x, y, x + w, y + h, color);
      ctx.method_25294(x, y, x + 4, y + 4, color);
      ctx.method_25294(x + w - 4, y, x + w, y + 4, color);
      ctx.method_25294(x, y + h - 4, x + 4, y + h, color);
      ctx.method_25294(x + w - 4, y + h - 4, x + w, y + h, color);
   }
}
