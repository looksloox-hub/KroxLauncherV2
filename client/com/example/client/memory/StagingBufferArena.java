package com.example.client.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Queue;

public final class StagingBufferArena {
   private final Queue<ByteBuffer> pool = new ArrayDeque();

   public synchronized ByteBuffer acquire(int minBytes) {
      while(true) {
         ByteBuffer buffer;
         if ((buffer = (ByteBuffer)this.pool.poll()) != null) {
            if (buffer.capacity() < minBytes) {
               continue;
            }

            buffer.clear();
            return buffer;
         }

         return ByteBuffer.allocateDirect(Math.max(1, minBytes)).order(ByteOrder.nativeOrder());
      }
   }

   public synchronized void release(ByteBuffer buffer) {
      if (buffer != null) {
         buffer.clear();
         this.pool.offer(buffer);
      }
   }
}
