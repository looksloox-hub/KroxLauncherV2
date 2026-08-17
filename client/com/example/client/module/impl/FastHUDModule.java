package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;
import net.minecraft.class_4066;

public class FastHUDModule extends Module {
   private int oldMipmaps = 4;
   private class_4066 oldParticles;

   public FastHUDModule() {
      super("FastHUD", Category.RENDER, (class_2960)null);
      this.oldParticles = class_4066.field_18197;
   }

   public void onEnable() {
      if (mc.field_1690 != null) {
         this.oldMipmaps = (Integer)mc.field_1690.method_42563().method_41753();
         this.oldParticles = (class_4066)mc.field_1690.method_42475().method_41753();
         mc.field_1690.method_42563().method_41748(0);
         mc.field_1690.method_42475().method_41748(class_4066.field_18199);
      }
   }

   public void onDisable() {
      if (mc.field_1690 != null) {
         mc.field_1690.method_42563().method_41748(this.oldMipmaps);
         mc.field_1690.method_42475().method_41748(this.oldParticles);
      }
   }

   public void onTick() {
      if (mc.field_1690 != null) {
         if (mc.field_1690.method_42475().method_41753() != class_4066.field_18199) {
            mc.field_1690.method_42475().method_41748(class_4066.field_18199);
         }

         if ((Integer)mc.field_1690.method_42563().method_41753() != 0) {
            mc.field_1690.method_42563().method_41748(0);
         }

      }
   }
}
