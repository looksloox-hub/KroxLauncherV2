package com.example.client.performance;

public final class AdaptiveResolutionController {
   private float scale;
   private final float minScale;
   private final float maxScale;
   private final float downStep;
   private final float upStep;

   public AdaptiveResolutionController(float minScale, float maxScale, float downStep, float upStep) {
      this.minScale = Math.max(0.25F, minScale);
      this.maxScale = Math.max(this.minScale, maxScale);
      this.downStep = Math.max(0.01F, downStep);
      this.upStep = Math.max(0.01F, upStep);
      this.scale = this.maxScale;
   }

   public void update(double gpuFrameMs, double targetMs) {
      if (gpuFrameMs > targetMs * 1.1) {
         this.scale = Math.max(this.minScale, this.scale - this.downStep);
      } else if (gpuFrameMs < targetMs * 0.85) {
         this.scale = Math.min(this.maxScale, this.scale + this.upStep);
      }

   }

   public float scale() {
      return this.scale;
   }
}
