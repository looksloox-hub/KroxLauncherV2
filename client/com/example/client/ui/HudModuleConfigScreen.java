package com.example.client.ui;

import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class HudModuleConfigScreen extends class_437 {
   private final Module module;
   private int panelX;
   private int panelY;
   private int panelW = 260;
   private int panelH = 260;

   public HudModuleConfigScreen(Module module) {
      super(class_2561.method_43470(module.getName() + " Config"));
      this.module = module;
   }

   protected void method_25426() {
      this.panelX = this.field_22789 - this.panelW - 16;
      this.panelY = 40;
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context, mouseX, mouseY, delta);
      context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -15723747);
      context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + 2, -7643914);
      context.method_27535(this.field_22793, class_2561.method_43470("Module Config"), this.panelX + 10, this.panelY + 10, -1);
      context.method_27535(this.field_22793, class_2561.method_43470(this.module.getName()), this.panelX + 10, this.panelY + 24, -4671233);
      int y = this.panelY + 48;

      for(Setting s : this.module.getSettings()) {
         if (s instanceof BooleanSetting b) {
            boolean hovered = this.isInside((double)mouseX, (double)mouseY, this.panelX + 10, y, this.panelW - 20, 12);
            int c = hovered ? -1 : -2236963;
            class_327 var10001 = this.field_22793;
            String var10002 = b.getName();
            context.method_27535(var10001, class_2561.method_43470(var10002 + ": " + (b.getValue() ? "ON" : "OFF")), this.panelX + 10, y, c);
            y += 14;
         } else if (s instanceof NumberSetting n) {
            boolean hovered = this.isInside((double)mouseX, (double)mouseY, this.panelX + 10, y, this.panelW - 20, 12);
            int c = hovered ? -1 : -2236963;
            class_327 var17 = this.field_22793;
            String var19 = n.getName();
            context.method_27535(var17, class_2561.method_43470(var19 + ": " + this.trim(n.getValue())), this.panelX + 10, y, c);
            y += 14;
         } else if (s instanceof ModeSetting m) {
            boolean hovered = this.isInside((double)mouseX, (double)mouseY, this.panelX + 10, y, this.panelW - 20, 12);
            int c = hovered ? -1 : -2236963;
            class_327 var18 = this.field_22793;
            String var20 = m.getName();
            context.method_27535(var18, class_2561.method_43470(var20 + ": " + m.getMode()), this.panelX + 10, y, c);
            y += 14;
         }
      }

      context.method_27535(this.field_22793, class_2561.method_43470("Left click = change"), this.panelX + 10, this.panelY + this.panelH - 34, -4671233);
      context.method_27535(this.field_22793, class_2561.method_43470("Right click = back"), this.panelX + 10, this.panelY + this.panelH - 20, -4671233);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int y = this.panelY + 48;

      for(Setting s : this.module.getSettings()) {
         if (s instanceof BooleanSetting b) {
            if (this.isInside(mouseX, mouseY, this.panelX + 10, y, this.panelW - 20, 12)) {
               if (button == 0) {
                  b.toggle();
               }

               if (button == 1) {
                  this.field_22787.method_1507(new HudEditorScreen());
               }

               return true;
            }

            y += 14;
         } else if (s instanceof NumberSetting n) {
            if (this.isInside(mouseX, mouseY, this.panelX + 10, y, this.panelW - 20, 12)) {
               if (button == 0) {
                  n.setValue(n.getValue() + n.getStep());
               }

               if (button == 1) {
                  n.setValue(n.getValue() - n.getStep());
               }

               return true;
            }

            y += 14;
         } else if (s instanceof ModeSetting m) {
            if (this.isInside(mouseX, mouseY, this.panelX + 10, y, this.panelW - 20, 12)) {
               if (button == 0) {
                  m.cycle();
               }

               if (button == 1) {
                  m.previous();
               }

               return true;
            }

            y += 14;
         }
      }

      if (button == 1) {
         this.field_22787.method_1507(new HudEditorScreen());
         return true;
      } else {
         return false;
      }
   }

   private boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private String trim(double d) {
      return Math.abs(d - (double)Math.round(d)) < 1.0E-4 ? String.valueOf((int)Math.round(d)) : String.format("%.2f", d);
   }
}
