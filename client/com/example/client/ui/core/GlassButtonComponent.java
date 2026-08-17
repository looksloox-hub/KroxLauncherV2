package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassButtonComponent extends GlassComponent {
   private String label;
   private Runnable action;

   public GlassButtonComponent(int x, int y, int width, int height, String label, Runnable action) {
      super(x, y, width, height);
      this.label = label;
      this.action = action;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0 && this.action != null) {
         this.action.run();
         return true;
      } else {
         return false;
      }
   }

   public String label() {
      return this.label;
   }

   public Runnable action() {
      return this.action;
   }
}
