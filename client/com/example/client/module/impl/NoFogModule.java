package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class NoFogModule extends Module {
   public NoFogModule() {
      super("NoFog", Category.RENDER, (class_2960)null);
   }

   public float getFogDensity(float original) {
      return !this.isEnabled() ? original : 0.0F;
   }
}
