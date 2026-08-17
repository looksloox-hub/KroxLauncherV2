package com.example.client.render;

import com.example.client.module.ModuleManager;
import com.example.client.module.impl.AdaptiveResolutionModule;
import net.minecraft.class_276;
import net.minecraft.class_310;

public final class DynamicResolutionController {
   private static int lastWidth = -1;
   private static int lastHeight = -1;

   private DynamicResolutionController() {
   }

   public static void apply() {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.method_22683() != null && mc.method_1522() != null) {
         if (mc.field_1687 == null) {
            restore(mc);
         } else {
            AdaptiveResolutionModule module = (AdaptiveResolutionModule)ModuleManager.getModule(AdaptiveResolutionModule.class);
            if (module != null && module.isEnabled()) {
               float scale = module.getScale();
               int targetW = Math.max(320, Math.round((float)mc.method_22683().method_4489() * scale));
               int targetH = Math.max(180, Math.round((float)mc.method_22683().method_4506() * scale));
               if (targetW != lastWidth || targetH != lastHeight) {
                  class_276 fb = mc.method_1522();
                  fb.method_1234(targetW, targetH);
                  lastWidth = targetW;
                  lastHeight = targetH;
               }
            } else {
               restore(mc);
            }
         }
      }
   }

   public static void restore(class_310 mc) {
      if (mc != null && mc.method_22683() != null && mc.method_1522() != null) {
         int fullW = mc.method_22683().method_4489();
         int fullH = mc.method_22683().method_4506();
         if (lastWidth != fullW || lastHeight != fullH) {
            mc.method_1522().method_1234(fullW, fullH);
            lastWidth = fullW;
            lastHeight = fullH;
         }
      }
   }
}
