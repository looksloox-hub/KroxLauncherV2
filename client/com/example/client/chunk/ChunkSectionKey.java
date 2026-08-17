package com.example.client.chunk;

import net.minecraft.class_238;

public record ChunkSectionKey(int x, int y, int z) {
   public class_238 toBox(int sectionSize) {
      double minX = (double)this.x * (double)sectionSize;
      double minY = (double)this.y * (double)sectionSize;
      double minZ = (double)this.z * (double)sectionSize;
      double maxX = minX + (double)sectionSize;
      double maxY = minY + (double)sectionSize;
      double maxZ = minZ + (double)sectionSize;
      return new class_238(minX, minY, minZ, maxX, maxY, maxZ);
   }
}
