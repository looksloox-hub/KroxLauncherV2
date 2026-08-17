package com.example.client.hud;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HudRegistry {
   private final Map<String, HudElementFactory<? extends HudElement>> factories = new LinkedHashMap();

   public <T extends HudElement> void registerFactory(String id, HudElementFactory<T> factory) {
      if (id != null && !id.isBlank()) {
         this.factories.put(id, (HudElementFactory)Objects.requireNonNull(factory, "factory"));
      } else {
         throw new IllegalArgumentException("id cannot be blank");
      }
   }

   public boolean hasFactory(String id) {
      return this.factories.containsKey(id);
   }

   public <T extends HudElement> Optional<HudElementFactory<T>> factory(String id) {
      return Optional.ofNullable((HudElementFactory)this.factories.get(id));
   }

   public HudElement create(String factoryId, String elementId) {
      HudElementFactory<? extends HudElement> factory = (HudElementFactory)this.factories.get(factoryId);
      if (factory == null) {
         throw new IllegalArgumentException("Unknown HUD factory: " + factoryId);
      } else {
         return factory.create(elementId);
      }
   }

   public Collection<String> ids() {
      return this.factories.keySet();
   }
}
