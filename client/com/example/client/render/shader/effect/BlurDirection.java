package com.example.client.render.shader.effect;

public enum BlurDirection {
   HORIZONTAL(1.0F, 0.0F),
   VERTICAL(0.0F, 1.0F),
   BOTH(1.0F, 1.0F);

   private final float x;
   private final float y;

   private BlurDirection(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public float x() {
      return this.x;
   }

   public float y() {
      return this.y;
   }

   // $FF: synthetic method
   private static BlurDirection[] $values() {
      return new BlurDirection[]{HORIZONTAL, VERTICAL, BOTH};
   }
}
