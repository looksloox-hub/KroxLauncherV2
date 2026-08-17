package com.example.client.ui;

import com.example.client.module.impl.MotionBlurModule;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_357;
import net.minecraft.class_437;

public class MotionBlurSettingsScreen extends class_437 {
   private final MotionBlurModule module;
   private class_357 slider;

   public MotionBlurSettingsScreen(MotionBlurModule module) {
      super(class_2561.method_43470("Motion Blur Settings"));
      this.module = module;
   }

   protected void method_25426() {
      super.method_25426();
      int panelW = 300;
      int panelH = 150;
      int panelX = this.field_22789 / 2 - panelW / 2;
      int panelY = this.field_22790 / 2 - panelH / 2;
      int sliderX = panelX + 60;
      int sliderY = panelY + 72;
      this.slider = new class_357(sliderX, sliderY, 180, 20, class_2561.method_43470(""), (double)(this.module.getBlurLevel() - 1) / (double)4.0F) {
         protected void method_25346() {
            this.method_25355(class_2561.method_43470("Level " + MotionBlurSettingsScreen.this.module.getBlurLevel()));
         }

         protected void method_25344() {
            int level = Math.max(1, Math.min(5, (int)Math.round(this.field_22753 * (double)4.0F) + 1));
            MotionBlurSettingsScreen.this.module.setBlurLevel(level);
            this.method_25346();
         }
      };
      this.slider.method_25355(class_2561.method_43470("Level " + this.module.getBlurLevel()));
      this.method_37063(this.slider);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -586609904, -300869593);
      int panelW = 300;
      int panelH = 150;
      int panelX = this.field_22789 / 2 - panelW / 2;
      int panelY = this.field_22790 / 2 - panelH / 2;
      context.method_25294(panelX - 4, panelY - 4, panelX + panelW + 4, panelY + panelH + 4, 1140850688);
      context.method_25294(panelX, panelY, panelX + panelW, panelY + panelH, -15723747);
      context.method_25294(panelX, panelY, panelX + panelW, panelY + 2, -11688193);
      context.method_25300(this.field_22793, "Motion Blur", this.field_22789 / 2, panelY + 16, -1);
      context.method_25300(this.field_22793, "Drag the slider to change smoothness", this.field_22789 / 2, panelY + 34, -6511697);
      context.method_25300(this.field_22793, "Level " + this.module.getBlurLevel(), this.field_22789 / 2, panelY + 58, -1);
      String var10000;
      switch (this.module.getBlurLevel()) {
         case 1 -> var10000 = "Subtle";
         case 2 -> var10000 = "Smooth";
         case 3 -> var10000 = "Balanced";
         case 4 -> var10000 = "Strong";
         case 5 -> var10000 = "Cinematic";
         default -> var10000 = "Balanced";
      }

      String label = var10000;
      context.method_25300(this.field_22793, label, this.field_22789 / 2, panelY + 88, -11688193);
      super.method_25394(context, mouseX, mouseY, delta);
      context.method_25300(this.field_22793, "Press ESC to close", this.field_22789 / 2, panelY + panelH - 18, -9735552);
   }

   public boolean method_25421() {
      return false;
   }

   public boolean method_25422() {
      return true;
   }
}
