package com.example.client.hud;

public interface HudRenderer {
   void pushClip(HudBounds var1);

   void popClip();

   void drawPanel(HudBounds var1, float var2, int var3, int var4, float var5);

   void drawShadow(HudBounds var1, float var2, int var3, float var4);

   void drawGlow(HudBounds var1, float var2, int var3, float var4);

   void drawText(String var1, double var2, double var4, int var6);

   double measureTextWidth(String var1);
}
