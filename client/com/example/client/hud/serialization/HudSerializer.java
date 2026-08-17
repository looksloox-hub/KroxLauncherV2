package com.example.client.hud.serialization;

import com.example.client.hud.HudElement;
import com.example.client.hud.HudLayoutData;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public interface HudSerializer {
   void save(Path var1, Collection<? extends HudElement> var2) throws IOException;

   List<HudLayoutData> load(Path var1) throws IOException;
}
