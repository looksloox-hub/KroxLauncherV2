package com.example.client.render.shader.util;

public final class ColorMath {
   private ColorMath() {
   }

   public static float[] rgba(int argb) {
      float a = (float)(argb >>> 24 & 255) / 255.0F;
      float r = (float)(argb >>> 16 & 255) / 255.0F;
      float g = (float)(argb >>> 8 & 255) / 255.0F;
      float b = (float)(argb & 255) / 255.0F;
      return new float[]{r, g, b, a};
   }

   public static float[] rgb(int rgb) {
      float r = (float)(rgb >>> 16 & 255) / 255.0F;
      float g = (float)(rgb >>> 8 & 255) / 255.0F;
      float b = (float)(rgb & 255) / 255.0F;
      return new float[]{r, g, b};
   }
}
