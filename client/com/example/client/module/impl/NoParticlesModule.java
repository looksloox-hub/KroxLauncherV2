package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class NoParticlesModule extends Module {
   public NoParticlesModule() {
      super("NoParticles", Category.RENDER, (class_2960)null);
   }

   public void onTick() {
      if (this.isEnabled() && mc.field_1687 != null) {
         mc.field_1713.method_3057();
      }
   }
}
