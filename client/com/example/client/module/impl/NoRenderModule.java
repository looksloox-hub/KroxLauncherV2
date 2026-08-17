package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class NoRenderModule extends Module {
   public boolean noFire = true;
   public boolean noWaterOverlay = true;
   public boolean noPumpkin = true;

   public NoRenderModule() {
      super("NoRender", Category.RENDER, (class_2960)null);
   }

   public void render(class_332 context) {
      if (this.isEnabled()) {
         ;
      }
   }
}
