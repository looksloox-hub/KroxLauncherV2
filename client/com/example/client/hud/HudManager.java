package com.example.client.hud;

import com.example.client.hud.alignment.HudAnchorResolver;
import com.example.client.hud.layout.HudLayoutAdapter;
import com.example.client.hud.serialization.HudSerializer;
import com.example.client.hud.snap.SnapSystem;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HudManager {
   private final Map<String, HudElement> elements = new LinkedHashMap();
   private final HudRegistry registry;
   private final HudSerializer serializer;
   private final HudLayoutAdapter layoutAdapter;
   private final SnapSystem snapSystem;

   public HudManager(HudRegistry registry, HudSerializer serializer, HudLayoutAdapter layoutAdapter, SnapSystem snapSystem) {
      this.registry = (HudRegistry)Objects.requireNonNull(registry, "registry");
      this.serializer = (HudSerializer)Objects.requireNonNull(serializer, "serializer");
      this.layoutAdapter = (HudLayoutAdapter)Objects.requireNonNull(layoutAdapter, "layoutAdapter");
      this.snapSystem = (SnapSystem)Objects.requireNonNull(snapSystem, "snapSystem");
   }

   public HudRegistry registry() {
      return this.registry;
   }

   public HudSerializer serializer() {
      return this.serializer;
   }

   public HudLayoutAdapter layoutAdapter() {
      return this.layoutAdapter;
   }

   public SnapSystem snapSystem() {
      return this.snapSystem;
   }

   public void register(HudElement element) {
      Objects.requireNonNull(element, "element");
      this.elements.put(element.id(), element);
   }

   public Optional<HudElement> find(String id) {
      return Optional.ofNullable((HudElement)this.elements.get(id));
   }

   public Collection<HudElement> elements() {
      return Collections.unmodifiableCollection(this.elements.values());
   }

   public List<HudElement> orderedElements() {
      ArrayList<HudElement> list = new ArrayList(this.elements.values());
      list.sort((a, b) -> Integer.compare(a.zIndex(), b.zIndex()));
      return list;
   }

   public void unregister(String id) {
      this.elements.remove(id);
   }

   public void clear() {
      this.elements.clear();
   }

   public void tickAll(HudContext context) {
      for(HudElement element : this.orderedElements()) {
         if (element.visible()) {
            element.tick(context);
         }
      }

   }

   public void renderAll(HudContext context) {
      for(HudElement element : this.orderedElements()) {
         if (element.visible()) {
            element.render(context);
         }
      }

   }

   public void applyAnchors(HudContext context) {
      HudBounds screen = context.screenBounds();

      for(HudElement element : this.elements.values()) {
         element.setBounds(HudAnchorResolver.resolve(screen, element.bounds(), element.anchor()));
      }

   }

   public void save(Path path) throws IOException {
      this.serializer.save(path, this.elements.values());
   }

   public void load(Path path) throws IOException {
      this.elements.clear();

      for(HudLayoutData data : this.serializer.load(path)) {
         HudElement element = this.registry.create(data.factoryId(), data.id());
         element.restore(data);
         this.elements.put(element.id(), element);
      }

   }

   public void replaceAll(Collection<? extends HudElement> newElements) {
      this.elements.clear();

      for(HudElement element : newElements) {
         this.register(element);
      }

   }
}
