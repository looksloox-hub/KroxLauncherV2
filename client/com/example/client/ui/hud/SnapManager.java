package com.example.client.ui.hud;

import com.example.client.module.Module;
import com.example.client.module.ModuleManager;

public class SnapManager {
   public static final float SNAP_DISTANCE = 6.0F;

   public static SnapResult handleSnap(Module dragging) {
      SnapResult result = new SnapResult((float)dragging.getX(), (float)dragging.getY());
      float x = (float)dragging.getX();
      float y = (float)dragging.getY();
      float w = (float)dragging.getWidth();
      float h = (float)dragging.getHeight();
      int screenW = dragging.getMc().method_22683().method_4486();
      int screenH = dragging.getMc().method_22683().method_4502();
      if (Math.abs(x + w / 2.0F - (float)screenW / 2.0F) < 6.0F) {
         result.x = (float)screenW / 2.0F - w / 2.0F;
         result.snapX = true;
      }

      if (Math.abs(y + h / 2.0F - (float)screenH / 2.0F) < 6.0F) {
         result.y = (float)screenH / 2.0F - h / 2.0F;
         result.snapY = true;
      }

      if (Math.abs(x) < 6.0F) {
         result.x = 0.0F;
         result.snapX = true;
      }

      if (Math.abs(y) < 6.0F) {
         result.y = 0.0F;
         result.snapY = true;
      }

      if (Math.abs(x + w - (float)screenW) < 6.0F) {
         result.x = (float)screenW - w;
         result.snapX = true;
      }

      if (Math.abs(y + h - (float)screenH) < 6.0F) {
         result.y = (float)screenH - h;
         result.snapY = true;
      }

      for(Module other : ModuleManager.getModules()) {
         if (other != dragging && other.isHud()) {
            float ox = (float)other.getX();
            float oy = (float)other.getY();
            float ow = (float)other.getWidth();
            float oh = (float)other.getHeight();
            if (Math.abs(x - (ox + ow)) < 6.0F) {
               result.x = ox + ow;
               result.snapX = true;
            }

            if (Math.abs(x + w - ox) < 6.0F) {
               result.x = ox - w;
               result.snapX = true;
            }

            if (Math.abs(y - (oy + oh)) < 6.0F) {
               result.y = oy + oh;
               result.snapY = true;
            }

            if (Math.abs(y + h - oy) < 6.0F) {
               result.y = oy - h;
               result.snapY = true;
            }
         }
      }

      return result;
   }

   public static class SnapResult {
      public float x;
      public float y;
      public boolean snapX;
      public boolean snapY;

      public SnapResult(float x, float y) {
         this.x = x;
         this.y = y;
      }
   }
}
