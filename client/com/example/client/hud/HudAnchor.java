package com.example.client.hud;

public enum HudAnchor {
   TOP_LEFT((double)0.0F, (double)0.0F),
   TOP_CENTER((double)0.5F, (double)0.0F),
   TOP_RIGHT((double)1.0F, (double)0.0F),
   CENTER_LEFT((double)0.0F, (double)0.5F),
   CENTER((double)0.5F, (double)0.5F),
   CENTER_RIGHT((double)1.0F, (double)0.5F),
   BOTTOM_LEFT((double)0.0F, (double)1.0F),
   BOTTOM_CENTER((double)0.5F, (double)1.0F),
   BOTTOM_RIGHT((double)1.0F, (double)1.0F);

   private final double anchorX;
   private final double anchorY;

   private HudAnchor(double anchorX, double anchorY) {
      this.anchorX = anchorX;
      this.anchorY = anchorY;
   }

   public double anchorX() {
      return this.anchorX;
   }

   public double anchorY() {
      return this.anchorY;
   }

   // $FF: synthetic method
   private static HudAnchor[] $values() {
      return new HudAnchor[]{TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT};
   }
}
