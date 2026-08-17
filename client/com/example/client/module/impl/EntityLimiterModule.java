package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class EntityLimiterModule extends Module {
   public int maxRenderDistance = 64;

   public EntityLimiterModule() {
      super("EntityLimiter", Category.RENDER, (class_2960)null);
   }

   public boolean shouldRender(double distance) {
      return !this.isEnabled() || distance <= (double)this.maxRenderDistance;
   }
}
