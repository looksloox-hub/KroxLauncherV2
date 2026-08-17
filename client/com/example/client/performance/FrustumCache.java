package com.example.client.performance;

import net.minecraft.class_4604;

public final class FrustumCache {
   private static class_4604 cachedFrustum;
   private static long lastUpdate = -1L;

   public static void update(class_4604 frustum) {
      long time = System.nanoTime() / 50000000L;
      if (time != lastUpdate) {
         cachedFrustum = frustum;
         lastUpdate = time;
      }

   }

   public static class_4604 get() {
      return cachedFrustum;
   }
}
