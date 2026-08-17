package com.example.client.render.batch;

public record ChunkDrawCommand(long key, int materialId, long vertexBuffer, long indexBuffer, int indexCount, long vertexOffset, long indexOffset) {
}
