package com.example.client.chunk;

import net.minecraft.class_1922;

public class ChunkSnapshot {
   private final class_1922 world;
   private final int baseX;
   private final int baseY;
   private final int baseZ;
   private final long key;

   public ChunkSnapshot(class_1922 world, int baseX, int baseY, int baseZ, long key) {
      this.world = world;
      this.baseX = baseX;
      this.baseY = baseY;
      this.baseZ = baseZ;
      this.key = key;
   }

   public class_1922 world() {
      return this.world;
   }

   public int baseX() {
      return this.baseX;
   }

   public int baseY() {
      return this.baseY;
   }

   public int baseZ() {
      return this.baseZ;
   }

   public long key() {
      return this.key;
   }
}
