package com.example.client.gpu;

import com.example.client.chunk.ChunkMesh;

public record GpuUploadTask(long key, ChunkMesh mesh) {
}
