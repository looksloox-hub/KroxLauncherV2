package com.example.client.ui.hud;

import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import net.minecraft.class_332;

public class HudOverlayRenderer {
   public static void render(class_332 context) {
      for(Module m : ModuleManager.getModules()) {
         if (m.isHud() && m.isEnabled()) {
            float x = (float)m.getX();
            float y = (float)m.getY();
            float w = (float)m.getWidth();
            float h = (float)m.getHeight();
            context.method_25294((int)x, (int)y, (int)(x + w), (int)(y + h), 570425344);
            context.method_25294((int)x, (int)y, (int)(x + w), (int)y + 1, -1);
            context.method_25294((int)x, (int)(y + h - 1.0F), (int)(x + w), (int)(y + h), -1);
            context.method_25294((int)x, (int)y, (int)x + 1, (int)(y + h), -1);
            context.method_25294((int)(x + w - 1.0F), (int)y, (int)(x + w), (int)(y + h), -1);
            context.method_25294((int)(x + w - 6.0F), (int)(y + h - 6.0F), (int)(x + w), (int)(y + h), -1);
         }
      }

   }
}
