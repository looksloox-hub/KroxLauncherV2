package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import net.minecraft.class_2960;

public class AntiOverlayModule extends Module {
   public AntiOverlayModule() {
      super("AntiOverlay", Category.RENDER, (class_2960)null);
   }

   public static boolean enabled() {
      AntiOverlayModule mod = (AntiOverlayModule)ModuleManager.getModuleByName("AntiOverlay");
      return mod != null && mod.isEnabled();
   }
}
