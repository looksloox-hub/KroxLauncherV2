package com.example.client.hud.alignment;

import com.example.client.hud.HudAnchor;
import com.example.client.hud.HudBounds;

public final class HudAnchorResolver {
   private HudAnchorResolver() {
   }

   public static HudBounds resolve(HudBounds screen, HudBounds bounds, HudAnchor anchor) {
      if (screen != null && bounds != null && anchor != null) {
         double x = screen.x() + (screen.width() - bounds.width()) * anchor.anchorX();
         double y = screen.y() + (screen.height() - bounds.height()) * anchor.anchorY();
         return new HudBounds(x, y, bounds.width(), bounds.height());
      } else {
         throw new IllegalArgumentException("screen, bounds, and anchor must be non-null");
      }
   }

   public static HudBounds pin(HudBounds screen, HudBounds bounds, HudAnchor anchor, double offsetX, double offsetY) {
      HudBounds resolved = resolve(screen, bounds, anchor);
      return resolved.moveBy(offsetX, offsetY);
   }
}
