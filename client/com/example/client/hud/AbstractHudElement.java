package com.example.client.hud;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractHudElement implements HudElement {
   private final String id;
   private String factoryId;
   private HudBounds bounds;
   private HudAnchor anchor;
   private boolean visible;
   private boolean locked;
   private float scale;
   private int zIndex;
   private final Map<String, String> properties;

   protected AbstractHudElement(String id, HudBounds bounds) {
      this.anchor = HudAnchor.TOP_LEFT;
      this.visible = true;
      this.locked = false;
      this.scale = 1.0F;
      this.zIndex = 0;
      this.properties = new LinkedHashMap();
      if (id != null && !id.isBlank()) {
         this.id = id;
         this.factoryId = id;
         this.bounds = (HudBounds)Objects.requireNonNull(bounds, "bounds");
      } else {
         throw new IllegalArgumentException("id cannot be blank");
      }
   }

   public final String id() {
      return this.id;
   }

   public final String factoryId() {
      return this.factoryId;
   }

   public final void setFactoryId(String factoryId) {
      this.factoryId = factoryId != null && !factoryId.isBlank() ? factoryId : this.id;
   }

   public final HudBounds bounds() {
      return this.bounds;
   }

   public final void setBounds(HudBounds bounds) {
      this.bounds = (HudBounds)Objects.requireNonNull(bounds, "bounds");
   }

   public final HudAnchor anchor() {
      return this.anchor;
   }

   public final void setAnchor(HudAnchor anchor) {
      this.anchor = (HudAnchor)Objects.requireNonNull(anchor, "anchor");
   }

   public final boolean visible() {
      return this.visible;
   }

   public final void setVisible(boolean visible) {
      this.visible = visible;
   }

   public final boolean locked() {
      return this.locked;
   }

   public final void setLocked(boolean locked) {
      this.locked = locked;
   }

   public final float scale() {
      return this.scale;
   }

   public final void setScale(float scale) {
      this.scale = scale <= 0.0F ? 1.0F : scale;
   }

   public final int zIndex() {
      return this.zIndex;
   }

   public final void setZIndex(int zIndex) {
      this.zIndex = zIndex;
   }

   public final Map<String, String> properties() {
      return Collections.unmodifiableMap(this.properties);
   }

   protected final Map<String, String> mutableProperties() {
      return this.properties;
   }

   protected void setProperty(String key, String value) {
      if (key != null && !key.isBlank()) {
         if (value == null) {
            this.properties.remove(key);
         } else {
            this.properties.put(key, value);
         }

      } else {
         throw new IllegalArgumentException("key cannot be blank");
      }
   }

   protected String property(String key, String fallback) {
      return (String)this.properties.getOrDefault(key, fallback);
   }
}
