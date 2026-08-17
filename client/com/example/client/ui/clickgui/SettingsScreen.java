package com.example.client.ui.clickgui;

import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class SettingsScreen extends class_437 {
   private final Module module;
   private int offset = 0;

   public SettingsScreen(Module module) {
      super(class_2561.method_43470(module.getName() + " Settings"));
      this.module = module;
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context, mouseX, mouseY, delta);
      int x = this.field_22789 / 2 - 100;
      int y = this.field_22790 / 2 - 100;
      context.method_51433(this.field_22793, this.module.getName(), x, y - 15, -1, true);
      this.offset = 0;

      for(Setting setting : this.module.getSettings()) {
         int sy = y + this.offset;
         if (setting instanceof BooleanSetting bool) {
            context.method_51433(this.field_22793, setting.getName() + ": " + (bool.isEnabled() ? "ON" : "OFF"), x, sy, 16777215, false);
         }

         if (setting instanceof NumberSetting num) {
            class_327 var10001 = this.field_22793;
            String var10002 = setting.getName();
            context.method_51433(var10001, var10002 + ": " + String.format("%.2f", num.getValue()), x, sy, 11184895, false);
         }

         if (setting instanceof ModeSetting mode) {
            context.method_51433(this.field_22793, setting.getName() + ": " + mode.getMode(), x, sy, 16755455, false);
         }

         this.offset += 12;
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int x = this.field_22789 / 2 - 100;
      int y = this.field_22790 / 2 - 100;
      int i = 0;

      for(Setting setting : this.module.getSettings()) {
         int sy = y + i;
         if (mouseX >= (double)x && mouseX <= (double)(x + 200) && mouseY >= (double)sy && mouseY <= (double)(sy + 10)) {
            if (setting instanceof BooleanSetting) {
               BooleanSetting bool = (BooleanSetting)setting;
               bool.setEnabled(!bool.isEnabled());
            }

            if (setting instanceof ModeSetting) {
               ModeSetting mode = (ModeSetting)setting;
               mode.cycle();
            }
         }

         i += 12;
      }

      return true;
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontal, double vertical) {
      int x = this.field_22789 / 2 - 100;
      int y = this.field_22790 / 2 - 100;
      int i = 0;

      for(Setting setting : this.module.getSettings()) {
         int sy = y + i;
         if (mouseX >= (double)x && mouseX <= (double)(x + 200) && mouseY >= (double)sy && mouseY <= (double)(sy + 10) && setting instanceof NumberSetting num) {
            num.setValue(num.getValue() + vertical * num.getIncrement());
         }

         i += 12;
      }

      return super.method_25401(mouseX, mouseY, horizontal, vertical);
   }
}
