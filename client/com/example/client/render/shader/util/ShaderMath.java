package com.example.client.render.shader.util;

public final class ShaderMath {
   private ShaderMath() {
   }

   public static float clamp(float value, float min, float max) {
      return value < min ? min : Math.min(value, max);
   }

   public static int clamp(int value, int min, int max) {
      return value < min ? min : Math.min(value, max);
   }

   public static float lerp(float start, float end, float alpha) {
      return start + (end - start) * alpha;
   }

   public static float smoothStep(float edge0, float edge1, float value) {
      float t = clamp((value - edge0) / (edge1 - edge0), 0.0F, 1.0F);
      return t * t * (3.0F - 2.0F * t);
   }

   public static float capsuleRadius(float requestedRadius, float width, float height) {
      return requestedRadius >= 999.0F ? Math.min(width, height) * 0.5F : Math.max(0.0F, Math.min(requestedRadius, Math.min(width, height) * 0.5F));
   }

   public static float inverseLerp(float start, float end, float value) {
      return end == start ? 0.0F : clamp((value - start) / (end - start), 0.0F, 1.0F);
   }
}
