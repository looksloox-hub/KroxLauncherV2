package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.ui.RenderUtils;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class ShieldStatusModule extends Module {
   public ShieldStatusModule() {
      super("ShieldStatus", Category.HUD, class_2960.method_60655("modid", "textures/gui/icons/shield.png"));
      this.x = 20;
      this.y = 244;
      this.width = 32;
      this.height = 32;
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc.field_1724 != null) {
         class_1799 shield = mc.field_1724.method_6079();
         if (shield.method_31574(class_1802.field_8255)) {
            boolean disabled = this.isShieldDisabled();
            int borderColor = disabled ? -50373 : this.getRenderColor();
            int w = this.getWidth();
            int h = this.getHeight();
            if (this.isBox()) {
               RenderUtils.drawSmoothRect(context, this.x, this.y, w, h, 6, -15723747);
            }

            if (this.isGlow()) {
               RenderUtils.drawNeonGlow(context, this.x, this.y, w, h, 4, borderColor);
            }

            if (this.isOutline()) {
               RenderUtils.drawOutline(context, this.x, this.y, w, h, 6, 1, borderColor);
            } else {
               context.method_25294(this.x, this.y, this.x + w, this.y + 2, borderColor);
            }

            context.method_51427(shield, this.x + 8, this.y + 8);
            String text = disabled ? "Disabled" : "Active";
            context.method_51433(mc.field_1772, text, this.x + 4, this.y + 20, borderColor, false);
         }
      }
   }

   private boolean isShieldDisabled() {
      class_1799 shield = mc.field_1724.method_6079();
      return mc.field_1724.method_7357().method_7904(shield);
   }
}
