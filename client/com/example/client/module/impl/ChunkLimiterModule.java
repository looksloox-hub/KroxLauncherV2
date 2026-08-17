package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class ChunkLimiterModule extends Module {
   private int oldDistance = -1;
   public int renderDistance = 6;

   public ChunkLimiterModule() {
      super("ChunkLimiter", Category.RENDER, (class_2960)null);
   }

   public void onEnable() {
      if (mc.field_1690 != null) {
         this.oldDistance = (Integer)mc.field_1690.method_42503().method_41753();
         mc.field_1690.method_42503().method_41748(this.renderDistance);
      }
   }

   public void onDisable() {
      if (mc.field_1690 != null) {
         if (this.oldDistance != -1) {
            mc.field_1690.method_42503().method_41748(this.oldDistance);
         }

      }
   }

   public void onTick() {
      if (mc.field_1690 != null) {
         if ((Integer)mc.field_1690.method_42503().method_41753() != this.renderDistance) {
            mc.field_1690.method_42503().method_41748(this.renderDistance);
         }

      }
   }
}
