package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassScrollPanelComponent extends GlassComponent {
   private String title;

   public GlassScrollPanelComponent(int x, int y, int width, int height) {
      super(x, y, width, height);
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public String title() {
      return this.title;
   }

   public GlassScrollPanelComponent setTitle(String title) {
      this.title = title;
      return this;
   }
}
