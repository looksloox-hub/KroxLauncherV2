package com.example.client.hud.resize;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;

public record ResizeState(boolean active, HudElement element, HudBounds bounds, ResizeHandle handle) {
   public static ResizeState idle() {
      return new ResizeState(false, (HudElement)null, (HudBounds)null, ResizeHandle.NONE);
   }
}
