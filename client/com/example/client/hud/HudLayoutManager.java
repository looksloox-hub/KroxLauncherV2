package com.example.client.hud;

import java.util.HashMap;
import java.util.Map;

public final class HudLayoutManager {
   private static final Map<String, HudState> STATES = new HashMap();

   private HudLayoutManager() {
   }

   public static HudState get(String name) {
      HudState state = (HudState)STATES.get(name);
      if (state == null) {
         state = new HudState();
         STATES.put(name, state);
      }

      return state;
   }
}
