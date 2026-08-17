package com.example.client.chunk;

import com.example.client.gpu.GpuUploadQueue;
import com.example.client.gpu.GpuUploadTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class ChunkMeshingService implements AutoCloseable {
   private final ConcurrentLinkedQueue<ChunkSnapshot> queue = new ConcurrentLinkedQueue();
   private final ExecutorService workers;
   private final GreedyMesher mesher = new GreedyMesher();
   private final GpuUploadQueue uploadQueue;
   private final int maxTasksPerFrame;

   public ChunkMeshingService(GpuUploadQueue uploadQueue, int workerThreads, int maxTasksPerFrame) {
      this.uploadQueue = uploadQueue;
      this.maxTasksPerFrame = Math.max(1, maxTasksPerFrame);
      ThreadFactory factory = (r) -> {
         Thread t = new Thread(r, "Optix-ChunkMesher");
         t.setDaemon(true);
         return t;
      };
      this.workers = Executors.newFixedThreadPool(Math.max(1, workerThreads), factory);
   }

   public void enqueue(ChunkSnapshot snapshot) {
      if (snapshot != null) {
         this.queue.offer(snapshot);
      }

   }

   public void pump() {
      for(int i = 0; i < this.maxTasksPerFrame; ++i) {
         ChunkSnapshot snapshot = (ChunkSnapshot)this.queue.poll();
         if (snapshot == null) {
            return;
         }

         CompletableFuture.supplyAsync(() -> this.mesher.mesh(snapshot.world(), snapshot.baseX(), snapshot.baseY(), snapshot.baseZ()), this.workers).thenAccept((mesh) -> this.uploadQueue.submit(new GpuUploadTask(snapshot.key(), mesh)));
      }

   }

   public void close() {
      this.workers.shutdownNow();
   }
}
