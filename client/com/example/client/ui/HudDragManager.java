package com.example.client.ui;

import com.example.client.module.HudModule;
import com.example.client.module.Module;

public class HudDragManager {
   public static boolean dragging = false;
   public static Module current = null;
   public static int offsetX;
   public static int offsetY;
   public static boolean resizing = false;
   public static Module resizeTarget = null;

   public static void handleDragging(int mouseX, int mouseY, boolean mouseDown) {
      Module var4 = current;
      if (var4 instanceof HudModule hud) {
         if (mouseDown) {
            if (!dragging) {
               dragging = true;
               offsetX = mouseX - hud.getX();
               offsetY = mouseY - hud.getY();
            }

            int targetX = mouseX - offsetX;
            int targetY = mouseY - offsetY;
            hud.setPosition(targetX, targetY);
         } else {
            dragging = false;
         }

      }
   }

   public static int snap(int value, int grid) {
      return Math.round((float)value / (float)grid) * grid;
   }

   public static void stopDragging() {
      dragging = false;
      current = null;
   }
}
