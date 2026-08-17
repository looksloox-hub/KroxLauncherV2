package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class AdaptiveResolutionModule extends Module {
   private float scale = 1.0F;
   private final float minScale = 0.55F;
   private final int lowFpsThreshold = 50;
   private final int highFpsThreshold = 70;
   private final float downStep = 0.06F;
   private final float upStep = 0.03F;

   public AdaptiveResolutionModule() {
      super("AdaptiveResolution", Category.RENDER, (class_2960)null);
   }

   public void onTick() {
      if (mc != null) {
         int fps = mc.method_47599();
         if (fps <= 50) {
            this.scale -= 0.06F;
         } else if (fps >= 70) {
            this.scale += 0.03F;
         } else if (this.scale < 1.0F) {
            this.scale += 0.01F;
         }

         this.scale = Math.max(0.55F, Math.min(1.0F, this.scale));
      }
   }

   public void onDisable() {
      this.scale = 1.0F;
   }

   public float getScale() {
      return this.isEnabled() ? this.scale : 1.0F;
   }

   public boolean isActive() {
      return this.isEnabled() && this.scale < 0.999F;
   }
}
