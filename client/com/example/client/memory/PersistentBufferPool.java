package com.example.client.memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class PersistentBufferPool {
   private final ConcurrentLinkedQueue<ByteBuffer> pool = new ConcurrentLinkedQueue();

   public ByteBuffer acquire(int minBytes) {
      int required = Math.max(1, minBytes);

      ByteBuffer buffer;
      while((buffer = (ByteBuffer)this.pool.poll()) != null) {
         if (buffer.capacity() >= required) {
            buffer.clear();
            return buffer;
         }
      }

      return ByteBuffer.allocateDirect(required).order(ByteOrder.nativeOrder());
   }

   public void release(ByteBuffer buffer) {
      if (buffer != null) {
         buffer.clear();
         this.pool.offer(buffer);
      }
   }

   public int pooledCount() {
      return this.pool.size();
   }
}
