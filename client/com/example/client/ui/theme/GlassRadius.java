package com.example.client.ui.theme;

public final class GlassRadius {
   public static final float NONE = 0.0F;
   public static final float X_SMALL = 4.0F;
   public static final float SMALL = 8.0F;
   public static final float MEDIUM = 12.0F;
   public static final float CARD = 18.0F;
   public static final float PANEL = 24.0F;
   public static final float LARGE = 28.0F;
   public static final float XLARGE = 36.0F;
   public static final float PILL = 9999.0F;

   private GlassRadius() {
   }

   public static float resolve(float requestedRadius, float width, float height) {
      float max = Math.max(0.0F, Math.min(width, height) * 0.5F);
      return requestedRadius >= 9999.0F ? max : Math.max(0.0F, Math.min(requestedRadius, max));
   }

   public static float pill(float width, float height) {
      return resolve(9999.0F, width, height);
   }
}
