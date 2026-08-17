package com.example.client.hud.drag;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;

public record DragState(boolean active, HudElement element, HudBounds bounds, double offsetX, double offsetY) {
   public static DragState idle() {
      return new DragState(false, (HudElement)null, (HudBounds)null, (double)0.0F, (double)0.0F);
   }
}
