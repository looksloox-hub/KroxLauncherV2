package com.example.client.performance;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_2902.class_2903;

public final class ChunkMeshOptimizer {
   private static final Queue<class_1923> rebuildQueue = new ArrayDeque();
   private static int rebuildBudget = 2;

   private ChunkMeshOptimizer() {
   }

   public static void queueChunk(class_1923 pos) {
      if (pos != null) {
         rebuildQueue.add(pos);
      }

   }

   public static void queueChunk(class_2338 pos) {
      if (pos != null) {
         rebuildQueue.add(new class_1923(pos));
      }

   }

   public static void tick(class_310 mc) {
      if (mc != null && mc.field_1687 != null && mc.field_1769 != null) {
         int fps = mc.method_47599();
         if (fps < 35) {
            rebuildBudget = 1;
         } else if (fps < 60) {
            rebuildBudget = 2;
         } else {
            rebuildBudget = 4;
         }

         for(int processed = 0; !rebuildQueue.isEmpty() && processed < rebuildBudget; ++processed) {
            class_1923 pos = (class_1923)rebuildQueue.poll();
            if (pos != null) {
               mc.field_1769.method_62219(pos.method_8326(), 0, pos.method_8328(), pos.method_8327(), mc.field_1687.method_8624(class_2903.field_13202, pos.method_8327(), pos.method_8329()), pos.method_8329());
               mc.field_1769.method_3292();
            }
         }

      }
   }
}
