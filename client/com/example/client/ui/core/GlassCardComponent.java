package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassCardComponent extends GlassComponent {
   private String title;
   private String subtitle;
   private String iconGlyph;

   public GlassCardComponent(int x, int y, int width, int height, String title, String subtitle, String iconGlyph) {
      super(x, y, width, height);
      this.title = title;
      this.subtitle = subtitle;
      this.iconGlyph = iconGlyph;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public String title() {
      return this.title;
   }

   public String subtitle() {
      return this.subtitle;
   }

   public String iconGlyph() {
      return this.iconGlyph;
   }
}
