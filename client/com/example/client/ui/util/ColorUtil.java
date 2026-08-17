package com.example.client.ui.util;

public final class ColorUtil {
   private ColorUtil() {
   }

   public static int argb(int alpha, int red, int green, int blue) {
      return (alpha & 255) << 24 | (red & 255) << 16 | (green & 255) << 8 | blue & 255;
   }

   public static int withAlpha(int color, int alpha) {
      return color & 16777215 | (alpha & 255) << 24;
   }

   public static int scaleAlpha(int color, float scale) {
      int alpha = color >>> 24 & 255;
      int scaled = Math.max(0, Math.min(255, Math.round((float)alpha * scale)));
      return color & 16777215 | scaled << 24;
   }

   public static int multiplyAlpha(int color, float multiplier) {
      return scaleAlpha(color, multiplier);
   }

   public static int blend(int from, int to, float progress) {
      float p = clamp01(progress);
      int a = lerp(from >>> 24 & 255, to >>> 24 & 255, p);
      int r = lerp(from >>> 16 & 255, to >>> 16 & 255, p);
      int g = lerp(from >>> 8 & 255, to >>> 8 & 255, p);
      int b = lerp(from & 255, to & 255, p);
      return argb(a, r, g, b);
   }

   public static int darken(int color, float amount) {
      float p = clamp01(amount);
      int a = color >>> 24 & 255;
      int r = Math.round((float)(color >>> 16 & 255) * (1.0F - p));
      int g = Math.round((float)(color >>> 8 & 255) * (1.0F - p));
      int b = Math.round((float)(color & 255) * (1.0F - p));
      return argb(a, r, g, b);
   }

   public static int lighten(int color, float amount) {
      float p = clamp01(amount);
      int a = color >>> 24 & 255;
      int r = Math.round((float)(color >>> 16 & 255) + (255.0F - (float)(color >>> 16 & 255)) * p);
      int g = Math.round((float)(color >>> 8 & 255) + (255.0F - (float)(color >>> 8 & 255)) * p);
      int b = Math.round((float)(color & 255) + (255.0F - (float)(color & 255)) * p);
      return argb(a, r, g, b);
   }

   private static int lerp(int a, int b, float p) {
      return Math.round((float)a + (float)(b - a) * p);
   }

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
