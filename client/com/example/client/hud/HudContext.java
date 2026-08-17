package com.example.client.hud;

import com.example.client.hud.layout.HudLayoutAdapter;
import com.example.client.hud.snap.SnapSystem;
import java.util.Objects;

public final class HudContext {
   private final HudRenderer renderer;
   private final HudLayoutAdapter layoutAdapter;
   private final SnapSystem snapSystem;
   private final double screenWidth;
   private final double screenHeight;
   private final float uiScale;
   private final boolean editing;

   public HudContext(HudRenderer renderer, HudLayoutAdapter layoutAdapter, SnapSystem snapSystem, double screenWidth, double screenHeight, float uiScale, boolean editing) {
      this.renderer = (HudRenderer)Objects.requireNonNull(renderer, "renderer");
      this.layoutAdapter = (HudLayoutAdapter)Objects.requireNonNull(layoutAdapter, "layoutAdapter");
      this.snapSystem = (SnapSystem)Objects.requireNonNull(snapSystem, "snapSystem");
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
      this.uiScale = uiScale;
      this.editing = editing;
   }

   public HudRenderer renderer() {
      return this.renderer;
   }

   public HudLayoutAdapter layoutAdapter() {
      return this.layoutAdapter;
   }

   public SnapSystem snapSystem() {
      return this.snapSystem;
   }

   public double screenWidth() {
      return this.screenWidth;
   }

   public double screenHeight() {
      return this.screenHeight;
   }

   public float uiScale() {
      return this.uiScale;
   }

   public boolean editing() {
      return this.editing;
   }

   public HudBounds screenBounds() {
      return new HudBounds((double)0.0F, (double)0.0F, this.screenWidth, this.screenHeight);
   }
}
