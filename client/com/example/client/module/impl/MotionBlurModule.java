package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.render.MotionBlurRenderer;
import net.minecraft.class_2960;

public class MotionBlurModule extends Module {
   private int blurLevel = 3;

   public MotionBlurModule() {
      super("MotionBlur", Category.RENDER, (class_2960)null);
   }

   public int getBlurLevel() {
      return this.blurLevel;
   }

   public void setBlurLevel(int level) {
      this.blurLevel = Math.max(1, Math.min(5, level));
      MotionBlurRenderer.setBlurLevel(this.blurLevel);
   }

   public void cycleBlurLevel() {
      this.setBlurLevel(this.blurLevel >= 5 ? 1 : this.blurLevel + 1);
   }

   public float getBlurStrength() {
      float var10000;
      switch (this.blurLevel) {
         case 1 -> var10000 = 0.2F;
         case 2 -> var10000 = 0.35F;
         case 3 -> var10000 = 0.5F;
         case 4 -> var10000 = 0.7F;
         case 5 -> var10000 = 0.9F;
         default -> var10000 = 0.5F;
      }

      return var10000;
   }
}
