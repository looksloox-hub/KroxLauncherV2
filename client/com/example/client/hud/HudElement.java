package com.example.client.hud;

import java.util.Map;

public interface HudElement {
   String id();

   String factoryId();

   void setFactoryId(String var1);

   HudBounds bounds();

   void setBounds(HudBounds var1);

   HudAnchor anchor();

   void setAnchor(HudAnchor var1);

   boolean visible();

   void setVisible(boolean var1);

   boolean locked();

   void setLocked(boolean var1);

   float scale();

   void setScale(float var1);

   int zIndex();

   void setZIndex(int var1);

   Map<String, String> properties();

   default boolean draggable() {
      return !this.locked();
   }

   default boolean resizable() {
      return !this.locked();
   }

   default void onAdded(HudContext context) {
   }

   default void onRemoved(HudContext context) {
   }

   default void tick(HudContext context) {
   }

   void render(HudContext var1);

   default HudLayoutData snapshot() {
      return HudLayoutData.fromElement(this);
   }

   default void restore(HudLayoutData data) {
      this.setFactoryId(data.factoryId());
      this.setBounds(data.bounds());
      this.setAnchor(data.anchor());
      this.setVisible(data.visible());
      this.setLocked(data.locked());
      this.setScale(data.scale());
      this.setZIndex(data.zIndex());
   }
}
