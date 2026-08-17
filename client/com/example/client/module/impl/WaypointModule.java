package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.ui.RenderUtils;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class WaypointModule extends Module {
   private final int wpX = 100;
   private final int wpY = 64;
   private final int wpZ = 100;

   public WaypointModule() {
      super("Waypoint", Category.HUD, class_2960.method_60655("modid", "textures/gui/icons/map.png"));
      this.x = 220;
      this.y = 50;
      this.width = 170;
      this.height = 22;
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null) {
         int px = (int)mc.field_1724.method_23317();
         int py = (int)mc.field_1724.method_23318();
         int pz = (int)mc.field_1724.method_23321();
         double dx = (double)(px - 100);
         double dy = (double)(py - 64);
         double dz = (double)(pz - 100);
         double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
         String dirX = px < 100 ? "→" : "←";
         String dirZ = pz < 100 ? "↓" : "↑";
         int color;
         if (dist > (double)200.0F) {
            color = -43691;
         } else if (dist > (double)80.0F) {
            color = -171;
         } else {
            color = this.getRenderColor();
         }

         int w = this.getWidth();
         int h = this.getHeight();
         if (this.isBox()) {
            RenderUtils.drawSmoothRect(context, this.x, this.y, w, h, 6, -15723747);
         }

         if (this.isGlow()) {
            RenderUtils.drawNeonGlow(context, this.x, this.y, w, h, 4, color);
         }

         if (this.isOutline()) {
            RenderUtils.drawOutline(context, this.x, this.y, w, h, 6, 1, color);
         }

         String text = "WP [100,64,100] " + dirX + dirZ + " " + (int)dist + "m";
         context.method_51433(mc.field_1772, text, this.x + 10, this.y + 7, -1, false);
      }
   }
}
