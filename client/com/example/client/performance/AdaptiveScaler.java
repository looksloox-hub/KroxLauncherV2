package com.example.client.performance;

public final class AdaptiveScaler {
   private float internalScale = 1.0F;
   private final float minScale;
   private final float maxScale;

   public AdaptiveScaler(float minScale, float maxScale) {
      this.minScale = Math.max(0.25F, minScale);
      this.maxScale = Math.max(this.minScale, maxScale);
      this.internalScale = this.maxScale;
   }

   public void update(double gpuFrameMillis, double targetMillis) {
      if (gpuFrameMillis > targetMillis * 1.15) {
         this.internalScale = Math.max(this.minScale, this.internalScale - 0.05F);
      } else if (gpuFrameMillis < targetMillis * 0.85) {
         this.internalScale = Math.min(this.maxScale, this.internalScale + 0.02F);
      }

   }

   public float internalScale() {
      return this.internalScale;
   }
}
