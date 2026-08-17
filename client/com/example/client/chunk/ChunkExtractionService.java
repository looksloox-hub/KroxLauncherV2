package com.example.client.chunk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import net.minecraft.class_1922;

public final class ChunkExtractionService implements AutoCloseable {
   private final ExecutorService workers;
   private final GreedyMesher mesher = new GreedyMesher();

   public ChunkExtractionService(int workerThreads) {
      this.workers = Executors.newFixedThreadPool(Math.max(1, workerThreads));
   }

   public void rebuild(class_1922 world, int chunkX, int chunkY, int chunkZ, Consumer<ChunkMesh> callback) {
      this.workers.submit(() -> {
         int baseX = chunkX << 4;
         int baseY = chunkY << 4;
         int baseZ = chunkZ << 4;
         ChunkMesh mesh = this.mesher.mesh(world, baseX, baseY, baseZ);
         callback.accept(mesh);
      });
   }

   public void close() {
      this.workers.shutdownNow();
   }
}
