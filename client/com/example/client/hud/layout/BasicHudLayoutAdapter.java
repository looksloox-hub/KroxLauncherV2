package com.example.client.hud.layout;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import java.util.Objects;

public final class BasicHudLayoutAdapter implements HudLayoutAdapter {
   public HudBounds measure(HudElement element) {
      return ((HudElement)Objects.requireNonNull(element, "element")).bounds();
   }

   public void layout(HudElement element, HudBounds bounds) {
      ((HudElement)Objects.requireNonNull(element, "element")).setBounds((HudBounds)Objects.requireNonNull(bounds, "bounds"));
   }
}
