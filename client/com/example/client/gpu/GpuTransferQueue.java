package com.example.client.gpu;

import com.example.client.chunk.ChunkMesh;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import org.lwjgl.system.MemoryUtil;

public final class GpuTransferQueue {
   private final Queue<GpuTransferTask> queue = new ArrayDeque();

   public synchronized void submit(GpuTransferTask task) {
      if (task != null) {
         this.queue.add(task);
      }

   }

   public synchronized GpuTransferTask poll() {
      return (GpuTransferTask)this.queue.poll();
   }

   public static ByteBuffer packMesh(ChunkMesh mesh) {
      int floats = mesh.positions().length + mesh.uvs().length;
      ByteBuffer buffer = MemoryUtil.memAlloc(floats * 4);

      for(float f : mesh.positions()) {
         buffer.putFloat(f);
      }

      for(float f : mesh.uvs()) {
         buffer.putFloat(f);
      }

      buffer.flip();
      return buffer;
   }

   public static record GpuTransferTask(long chunkKey, ChunkMesh mesh, ByteBuffer stagingData) {
   }
}
