package com.example.client.hud.snap;

import com.example.client.hud.HudBounds;
import java.util.Collections;
import java.util.List;

public record SnapResult(HudBounds bounds, List<SnapGuide> guides) {
   public SnapResult(HudBounds bounds, List<SnapGuide> guides) {
      guides = guides == null ? List.of() : Collections.unmodifiableList(guides);
      this.bounds = bounds;
      this.guides = guides;
   }

   public static SnapResult of(HudBounds bounds) {
      return new SnapResult(bounds, List.of());
   }
}
