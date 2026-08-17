package com.example.client.hud.layout;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;

public interface HudLayoutAdapter {
   HudBounds measure(HudElement var1);

   void layout(HudElement var1, HudBounds var2);
}
