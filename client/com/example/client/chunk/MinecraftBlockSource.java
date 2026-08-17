package com.example.client.chunk;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public final class MinecraftBlockSource implements ChunkBlockSource {
   private final class_1922 view;
   private final class_2338.class_2339 pos = new class_2338.class_2339();

   public MinecraftBlockSource(class_1922 view) {
      this.view = view;
   }

   public class_2680 getBlockState(int x, int y, int z) {
      return this.view.method_8320(this.pos.method_10103(x, y, z));
   }
}
