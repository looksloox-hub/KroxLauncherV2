package com.example.client.ui;

import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.example.client.module.ModuleStateManager;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class HudEditorScreen extends class_437 {
   private Module draggingModule = null;

   public HudEditorScreen() {
      super(class_2561.method_43470("HUD Editor"));
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.method_25420(context, mouseX, mouseY, delta);
      int hudMouseX = HudScaleManager.toHudX((double)mouseX);
      int hudMouseY = HudScaleManager.toHudY((double)mouseY);
      context.method_27535(this.field_22793, class_2561.method_43470("HUD Editor  |  LMB drag  |  RMB config"), 10, 10, -1);
      HudScaleManager.begin(context);

      try {
         for(Module m : ModuleManager.getModules()) {
            if (m.isHud() && m.isEnabled()) {
               int x = m.getX();
               int y = m.getY();
               int w = m.getWidth();
               int h = m.getHeight();
               if (m.isHovering((double)hudMouseX, (double)hudMouseY)) {
                  context.method_25294(x - 1, y - 1, x + w + 1, y, -1);
                  context.method_25294(x - 1, y + h, x + w + 1, y + h + 1, -1);
                  context.method_25294(x - 1, y, x, y + h, -1);
                  context.method_25294(x + w, y, x + w + 1, y + h, -1);
               }

               context.method_27535(this.field_22793, class_2561.method_43470(m.getName()), x, y - 10, -1);
            }
         }

         Module.renderSelectionBox(context);
      } finally {
         HudScaleManager.end(context);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int hudMouseX = HudScaleManager.toHudX(mouseX);
      int hudMouseY = HudScaleManager.toHudY(mouseY);

      for(Module m : ModuleManager.getModules()) {
         if (m.isHud() && m.isEnabled() && m.isHovering((double)hudMouseX, (double)hudMouseY)) {
            if (button == 0) {
               this.draggingModule = m;
               m.startDrag(hudMouseX, hudMouseY);
               return true;
            }

            if (button == 1) {
               class_310 mc = class_310.method_1551();
               if (mc != null) {
                  mc.method_1507(new HudModuleConfigScreen(m));
               }

               return true;
            }
         }
      }

      return false;
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.draggingModule != null) {
         int hudMouseX = HudScaleManager.toHudX(mouseX);
         int hudMouseY = HudScaleManager.toHudY(mouseY);
         this.draggingModule.dragTo(hudMouseX, hudMouseY);
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (this.draggingModule != null) {
         this.draggingModule.stopDrag();
         this.draggingModule = null;
         ModuleStateManager.saveAll(ModuleManager.getModules());
         return true;
      } else {
         return false;
      }
   }

   public void method_25432() {
      if (this.draggingModule != null) {
         this.draggingModule.stopDrag();
         this.draggingModule = null;
      }

      ModuleStateManager.saveAll(ModuleManager.getModules());
      super.method_25432();
   }
}
