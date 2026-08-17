package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassPanelComponent extends GlassComponent {
   private String title;

   public GlassPanelComponent(int x, int y, int width, int height) {
      this((String)null, x, y, width, height);
   }

   public GlassPanelComponent(String title, int x, int y, int width, int height) {
      super(x, y, width, height);
      this.title = title;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public String title() {
      return this.title;
   }

   public GlassPanelComponent setTitle(String title) {
      this.title = title;
      return this;
   }
}
