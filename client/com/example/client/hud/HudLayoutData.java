package com.example.client.hud;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public record HudLayoutData(String id, String factoryId, HudBounds bounds, HudAnchor anchor, boolean visible, boolean locked, float scale, int zIndex, Map<String, String> properties) {
   public HudLayoutData(String id, String factoryId, HudBounds bounds, HudAnchor anchor, boolean visible, boolean locked, float scale, int zIndex, Map<String, String> properties) {
      if (id != null && !id.isBlank()) {
         factoryId = factoryId != null && !factoryId.isBlank() ? factoryId : id;
         bounds = bounds == null ? new HudBounds((double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F) : bounds;
         anchor = anchor == null ? HudAnchor.TOP_LEFT : anchor;
         properties = properties != null && !properties.isEmpty() ? Collections.unmodifiableMap(new LinkedHashMap(properties)) : Map.of();
         this.id = id;
         this.factoryId = factoryId;
         this.bounds = bounds;
         this.anchor = anchor;
         this.visible = visible;
         this.locked = locked;
         this.scale = scale;
         this.zIndex = zIndex;
         this.properties = properties;
      } else {
         throw new IllegalArgumentException("id cannot be blank");
      }
   }

   public static HudLayoutData fromElement(HudElement element) {
      return new HudLayoutData(element.id(), element.factoryId(), element.bounds(), element.anchor(), element.visible(), element.locked(), element.scale(), element.zIndex(), element.properties());
   }
}
