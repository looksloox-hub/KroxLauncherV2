package com.example.client.renderer.vulkan.chunk;

import java.nio.ByteBuffer;

public final class VulkanChunkRasterizer {
   public ChunkMeshData rasterize(int[] blocks) {
      ByteBuffer buffer = ByteBuffer.allocateDirect(blocks.length * 24);
      int vertices = 0;

      for(int i = 0; i < blocks.length; ++i) {
         if (blocks[i] != 0) {
            buffer.putFloat((float)i);
            buffer.putFloat(0.0F);
            buffer.putFloat(0.0F);
            buffer.putFloat((float)i);
            buffer.putFloat(1.0F);
            buffer.putFloat(0.0F);
            buffer.putFloat((float)i);
            buffer.putFloat(1.0F);
            buffer.putFloat(1.0F);
            vertices += 3;
         }
      }

      buffer.flip();
      return new ChunkMeshData(buffer, vertices);
   }

   public static class ChunkMeshData {
      public final ByteBuffer vertexBuffer;
      public final int vertexCount;

      public ChunkMeshData(ByteBuffer vertexBuffer, int vertexCount) {
         this.vertexBuffer = vertexBuffer;
         this.vertexCount = vertexCount;
      }
   }
}
