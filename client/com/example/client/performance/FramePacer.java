package com.example.client.performance;

public final class FramePacer {
   private final long[] samples = new long[120];
   private int index;
   private int filled;
   private long startNanos;
   private int targetFps;

   public FramePacer(int targetFps) {
      this.targetFps = Math.max(1, targetFps);
   }

   public void begin() {
      this.startNanos = System.nanoTime();
   }

   public void end() {
      long elapsed = Math.max(0L, System.nanoTime() - this.startNanos);
      this.samples[this.index] = elapsed;
      this.index = (this.index + 1) % this.samples.length;
      this.filled = Math.min(this.samples.length, this.filled + 1);
   }

   public double averageMillis() {
      if (this.filled == 0) {
         return (double)0.0F;
      } else {
         long sum = 0L;

         for(int i = 0; i < this.filled; ++i) {
            sum += this.samples[i];
         }

         return (double)sum / (double)this.filled / (double)1000000.0F;
      }
   }

   public boolean overBudget() {
      return this.filled > 0 && this.averageMillis() > (double)1000.0F / (double)this.targetFps;
   }

   public void setTargetFps(int targetFps) {
      this.targetFps = Math.max(1, targetFps);
   }
}
