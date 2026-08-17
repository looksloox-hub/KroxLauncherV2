package com.example.client.memory;

import java.util.ArrayDeque;
import java.util.Deque;

public final class PersistentBufferAllocator {
   private final long capacity;
   private long nextOffset = 0L;
   private final Deque<Region> freeList = new ArrayDeque();

   public PersistentBufferAllocator(long capacity) {
      this.capacity = Math.max(1L, capacity);
   }

   public synchronized Region allocate(long size) {
      long need = Math.max(1L, size);
      Region reused = null;

      for(Region region : this.freeList) {
         if (region.size() >= need) {
            reused = region;
            break;
         }
      }

      if (reused != null) {
         this.freeList.remove(reused);
         return new Region(reused.offset(), need);
      } else if (this.nextOffset + need > this.capacity) {
         return null;
      } else {
         Region out = new Region(this.nextOffset, need);
         this.nextOffset += need;
         return out;
      }
   }

   public synchronized void free(Region region) {
      if (region != null) {
         this.freeList.addLast(region);
      }

   }

   public static record Region(long offset, long size) {
   }
}
