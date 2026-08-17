package com.example.client.hud;

import com.example.client.hud.layout.HudLayoutAdapter;
import com.example.client.hud.snap.SnapSystem;

public interface HudHost {
   HudRenderer renderer();

   HudLayoutAdapter layoutAdapter();

   SnapSystem snapSystem();

   double screenWidth();

   double screenHeight();

   float uiScale();

   boolean editing();
}
