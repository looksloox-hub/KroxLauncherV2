package com.example.client.performance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_238;
import net.minecraft.class_4604;

public final class ChunkVisibilityCache {
   private final Map<Long, Boolean> cache = new ConcurrentHashMap();

   public boolean isVisible(long key, class_4604 frustum, class_238 box) {
      if (frustum == null) {
         this.cache.put(key, Boolean.TRUE);
         return true;
      } else {
         boolean visible = frustum.method_23093(box);
         this.cache.put(key, visible);
         return visible;
      }
   }

   public void clear() {
      this.cache.clear();
   }
}
