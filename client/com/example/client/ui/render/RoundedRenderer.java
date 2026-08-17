package com.example.client.ui.render;

import com.example.client.ui.theme.GlassRadius;
import net.minecraft.class_332;

public final class RoundedRenderer {
   private RoundedRenderer() {
   }

   public static void draw(class_332 context, float x, float y, float width, float height, float radius, int color) {
      float resolvedRadius = GlassRadius.resolve(radius, width, height);
      int left = Math.round(x);
      int top = Math.round(y);
      int right = Math.round(x + width);
      int bottom = Math.round(y + height);
      if (!(width <= 0.0F) && !(height <= 0.0F)) {
         if (resolvedRadius <= 0.5F) {
            context.method_25294(left, top, right, bottom, color);
         } else {
            int r = Math.max(1, Math.round(resolvedRadius));
            int innerLeft = left + r;
            int innerRight = right - r;
            int innerTop = top + r;
            int innerBottom = bottom - r;
            if (innerRight > innerLeft) {
               context.method_25294(innerLeft, top, innerRight, bottom, color);
            }

            if (innerBottom > innerTop) {
               context.method_25294(left, innerTop, innerLeft, innerBottom, color);
               context.method_25294(innerRight, innerTop, right, innerBottom, color);
            }

            float rr = resolvedRadius * resolvedRadius;

            for(int i = 0; i < r; ++i) {
               float dy = (float)(r - i) - 0.5F;
               float dx = (float)Math.sqrt((double)Math.max(0.0F, rr - dy * dy));
               int inset = Math.max(0, Math.round(resolvedRadius - dx));
               int rowTop = top + i;
               int rowBottom = bottom - i - 1;
               int lineLeft = left + inset;
               int lineRight = right - inset;
               if (lineRight > lineLeft) {
                  context.method_25294(lineLeft, rowTop, lineRight, rowTop + 1, color);
                  context.method_25294(lineLeft, rowBottom, lineRight, rowBottom + 1, color);
               }
            }

         }
      }
   }
}
