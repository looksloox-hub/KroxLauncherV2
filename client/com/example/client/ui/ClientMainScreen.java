package com.example.client.ui;

import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class ClientMainScreen extends class_437 {
   private Module selectedModule = null;

   public ClientMainScreen() {
      super(class_2561.method_43470("GUI"));
   }

   public void method_25394(class_332 ctx, int mouseX, int mouseY, float delta) {
      this.method_25420(ctx, mouseX, mouseY, delta);
      int x = 20;
      int y = 20;

      for(Module m : ModuleManager.getModules()) {
         ctx.method_51433(this.field_22793, m.getName(), x, y, -1, false);
         if (this.selectedModule == m) {
            ctx.method_51433(this.field_22793, ">", x - 10, y, -65536, false);
         }

         y += 12;
      }

      super.method_25394(ctx, mouseX, mouseY, delta);
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      int x = 20;
      int y = 20;
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();

      for(Module m : ModuleManager.getModules()) {
         if (mouseX >= (double)x && mouseX <= (double)(x + 100) && mouseY >= (double)y && mouseY <= (double)(y + 10)) {
            if (button == 0) {
               m.toggle();
            }

            if (button == 1) {
               this.selectedModule = m;
            }
         }

         y += 12;
      }

      return super.method_25402(click, doubled);
   }
}
