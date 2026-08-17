package com.example.client.render;

import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_6367;
import net.minecraft.class_746;

public final class MotionBlurRenderer {
   private static final class_310 MC = class_310.method_1551();
   private static class_6367 historyFramebuffer;
   private static int lastW = -1;
   private static int lastH = -1;
   private static float strength = 0.8F;
   private static float motionX;
   private static float motionY;
   private static double lastCamX;
   private static double lastCamY;
   private static double lastCamZ;
   private static float lastYaw;
   private static float lastPitch;

   private MotionBlurRenderer() {
   }

   public static void init() {
      ensureFramebuffers();
      seedCamera();
   }

   public static void setStrength(float value) {
      strength = Math.max(0.0F, Math.min(0.95F, value));
   }

   public static float getStrength() {
      return strength;
   }

   public static int getBlurLevel() {
      if (strength <= 0.2F) {
         return 1;
      } else if (strength <= 0.35F) {
         return 2;
      } else if (strength <= 0.5F) {
         return 3;
      } else {
         return strength <= 0.7F ? 4 : 5;
      }
   }

   public static void setBlurLevel(int level) {
      switch (Math.max(1, Math.min(5, level))) {
         case 1 -> setStrength(0.2F);
         case 2 -> setStrength(0.35F);
         case 3 -> setStrength(0.5F);
         case 4 -> setStrength(0.7F);
         case 5 -> setStrength(0.9F);
      }

   }

   public static float getMotionX() {
      return motionX;
   }

   public static float getMotionY() {
      return motionY;
   }

   public static float getBlurIntensity() {
      return Math.min(0.95F, strength + (Math.abs(motionX) + Math.abs(motionY)) * 0.15F);
   }

   public static void render() {
      if (MC.field_1687 != null && MC.field_1724 != null) {
         ensureFramebuffers();
         updateMotion();
         class_276 main = MC.method_1522();
         if (main != null && historyFramebuffer != null) {
            historyFramebuffer.method_68445(main.method_71639());
            main.method_68445(historyFramebuffer.method_71639());
         }
      }
   }

   public static void updateFrameMotion() {
      updateMotion();
   }

   private static void updateMotion() {
      if (MC.field_1724 != null) {
         class_746 p = MC.field_1724;
         double x = p.method_23317();
         double y = p.method_23318();
         double z = p.method_23321();
         float yaw = p.method_36454();
         float pitch = p.method_36455();
         float dx = (float)(x - lastCamX);
         float dy = (float)(y - lastCamY);
         float dyaw = yaw - lastYaw;
         float dpitch = pitch - lastPitch;
         motionX = motionX * 0.65F + (dx * 0.8F + dyaw * 0.02F) * 0.35F;
         motionY = motionY * 0.65F + (dy * 0.8F + dpitch * 0.02F) * 0.35F;
         lastCamX = x;
         lastCamY = y;
         lastCamZ = z;
         lastYaw = yaw;
         lastPitch = pitch;
      }
   }

   private static void seedCamera() {
      if (MC.field_1724 != null) {
         lastCamX = MC.field_1724.method_23317();
         lastCamY = MC.field_1724.method_23318();
         lastCamZ = MC.field_1724.method_23321();
         lastYaw = MC.field_1724.method_36454();
         lastPitch = MC.field_1724.method_36455();
      }
   }

   private static void ensureFramebuffers() {
      if (MC.method_22683() != null) {
         int w = MC.method_22683().method_4489();
         int h = MC.method_22683().method_4506();
         if (historyFramebuffer == null) {
            historyFramebuffer = new class_6367("optix_motion_blur_history", w, h, true);
            lastW = w;
            lastH = h;
         } else {
            if (w != lastW || h != lastH) {
               historyFramebuffer.method_1234(w, h);
               lastW = w;
               lastH = h;
            }

         }
      }
   }
}
