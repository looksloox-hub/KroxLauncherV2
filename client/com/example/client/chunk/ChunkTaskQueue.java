package com.example.client.chunk;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class ChunkTaskQueue {
   private final ConcurrentLinkedQueue<ChunkSnapshot> queue = new ConcurrentLinkedQueue();

   public void offer(ChunkSnapshot snapshot) {
      this.queue.offer(snapshot);
   }

   public ChunkSnapshot poll() {
      return (ChunkSnapshot)this.queue.poll();
   }

   public boolean isEmpty() {
      return this.queue.isEmpty();
   }

   public int size() {
      return this.queue.size();
   }
}
