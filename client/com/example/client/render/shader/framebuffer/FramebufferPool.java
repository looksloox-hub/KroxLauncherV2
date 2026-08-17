package com.example.client.render.shader.framebuffer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class FramebufferPool implements AutoCloseable {
   private final Map<Key, Deque<FramebufferHandle>> pool = new ConcurrentHashMap();

   public FramebufferHandle acquire(int width, int height, boolean withDepth, boolean linearFiltering) {
      Key key = new Key(width, height, withDepth, linearFiltering);
      Deque<FramebufferHandle> queue = (Deque)this.pool.get(key);
      if (queue != null) {
         FramebufferHandle handle = (FramebufferHandle)queue.pollFirst();
         if (handle != null) {
            return handle;
         }
      }

      return new FramebufferHandle(width, height, withDepth, linearFiltering);
   }

   public void release(FramebufferHandle handle) {
      Objects.requireNonNull(handle, "handle");
      Key key = new Key(handle.width(), handle.height(), handle.withDepth(), handle.linearFiltering());
      ((Deque)this.pool.computeIfAbsent(key, (ignored) -> new ArrayDeque())).offerFirst(handle);
   }

   public void clear() {
      this.pool.values().forEach((queue) -> {
         while(!queue.isEmpty()) {
            ((FramebufferHandle)queue.pollFirst()).close();
         }

      });
      this.pool.clear();
   }

   public void close() {
      this.clear();
   }

   private static record Key(int width, int height, boolean withDepth, boolean linearFiltering) {
   }
}
