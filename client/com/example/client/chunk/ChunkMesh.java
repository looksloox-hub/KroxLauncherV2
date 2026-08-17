package com.example.client.chunk;

public record ChunkMesh(float[] positions, float[] uvs, int vertexCount) {
   public static ChunkMesh empty() {
      return new ChunkMesh(new float[0], new float[0], 0);
   }
}
