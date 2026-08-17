package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_2960;

public class FullbrightModule extends Module {
   public FullbrightModule() {
      super("FullBright", Category.RENDER, class_2960.method_60655("modid", "textures/gui/icons/sun.png"));
   }

   private void apply() {
      if (mc.field_1724 != null) {
         mc.field_1724.method_6092(new class_1293(class_1294.field_5925, 72000, 0, false, false, false));
      }
   }

   public void onEnable() {
      this.apply();
   }

   public void onDisable() {
      if (mc.field_1724 != null) {
         mc.field_1724.method_6016(class_1294.field_5925);
      }

   }

   public void onTick() {
      if (this.isEnabled() && mc.field_1724 != null && !mc.field_1724.method_6059(class_1294.field_5925)) {
         this.apply();
      }

   }
}
