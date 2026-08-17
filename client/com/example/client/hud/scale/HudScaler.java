package com.example.client.hud.scale;

import com.example.client.hud.HudBounds;

public final class HudScaler {
   private HudScaleMode mode;
   private float customScale;
   private float minScale;
   private float maxScale;

   public HudScaler() {
      this.mode = HudScaleMode.NONE;
      this.customScale = 1.0F;
      this.minScale = 0.25F;
      this.maxScale = 4.0F;
   }

   public HudScaleMode mode() {
      return this.mode;
   }

   public void setMode(HudScaleMode mode) {
      this.mode = mode == null ? HudScaleMode.NONE : mode;
   }

   public float customScale() {
      return this.customScale;
   }

   public void setCustomScale(float customScale) {
      this.customScale = clamp(customScale, this.minScale, this.maxScale);
   }

   public float minScale() {
      return this.minScale;
   }

   public void setMinScale(float minScale) {
      this.minScale = Math.max(0.01F, minScale);
   }

   public float maxScale() {
      return this.maxScale;
   }

   public void setMaxScale(float maxScale) {
      this.maxScale = Math.max(this.minScale, maxScale);
   }

   public HudBounds apply(HudBounds bounds, float uiScale, double screenWidth, double screenHeight) {
      float var10000;
      switch (this.mode) {
         case NONE -> var10000 = 1.0F;
         case UI_SCALE -> var10000 = uiScale;
         case CUSTOM -> var10000 = this.customScale;
         case SCREEN_RELATIVE -> var10000 = (float)Math.max((double)0.5F, Math.min(screenWidth, screenHeight) / (double)1080.0F);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      float scale = var10000;
      scale = clamp(scale, this.minScale, this.maxScale);
      return new HudBounds(bounds.x() * (double)scale, bounds.y() * (double)scale, bounds.width() * (double)scale, bounds.height() * (double)scale);
   }

   public double normalize(double value) {
      return value / (double)Math.max(0.01F, this.customScale);
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }
}
