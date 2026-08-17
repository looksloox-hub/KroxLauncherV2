package com.example.client.renderer.vulkan;

import com.example.client.chunk.ChunkMesh;
import com.example.client.memory.PersistentBufferAllocator;
import com.example.client.memory.StagingBufferArena;
import java.nio.ByteBuffer;

public final class VulkanUploadBridge {
   private final StagingBufferArena stagingBuffers;
   private final PersistentBufferAllocator allocator;

   public VulkanUploadBridge(StagingBufferArena stagingBuffers, PersistentBufferAllocator allocator) {
      this.stagingBuffers = stagingBuffers;
      this.allocator = allocator;
   }

   public ByteBuffer packMesh(ChunkMesh mesh) {
      int bytes = (mesh.positions().length + mesh.uvs().length) * 4;
      ByteBuffer staging = this.stagingBuffers.acquire(bytes);

      for(float f : mesh.positions()) {
         staging.putFloat(f);
      }

      for(float f : mesh.uvs()) {
         staging.putFloat(f);
      }

      staging.flip();
      return staging;
   }

   public void release(ByteBuffer staging) {
      this.stagingBuffers.release(staging);
   }
}
