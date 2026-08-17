package com.example.client.gpu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class GpuUploadQueue {
   private final ConcurrentLinkedQueue<GpuUploadTask> queue = new ConcurrentLinkedQueue();

   public void submit(GpuUploadTask task) {
      if (task != null) {
         this.queue.offer(task);
      }

   }

   public GpuUploadTask poll() {
      return (GpuUploadTask)this.queue.poll();
   }

   public List<GpuUploadTask> drainAll() {
      List<GpuUploadTask> out = new ArrayList();

      GpuUploadTask task;
      while((task = (GpuUploadTask)this.queue.poll()) != null) {
         out.add(task);
      }

      return out;
   }

   public int size() {
      return this.queue.size();
   }
}
