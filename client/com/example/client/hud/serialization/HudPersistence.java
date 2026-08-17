package com.example.client.hud.serialization;

import com.example.client.hud.HudElement;
import com.example.client.hud.HudLayoutData;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class HudPersistence {
   private final HudSerializer serializer;

   public HudPersistence(HudSerializer serializer) {
      this.serializer = (HudSerializer)Objects.requireNonNull(serializer, "serializer");
   }

   public void save(Path path, Collection<? extends HudElement> elements) throws IOException {
      this.serializer.save(path, elements);
   }

   public List<HudLayoutData> load(Path path) throws IOException {
      return this.serializer.load(path);
   }
}
