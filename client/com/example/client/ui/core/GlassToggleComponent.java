package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassToggleComponent extends GlassComponent {
   private String label;
   private boolean enabled;
   private Runnable onChange;

   public GlassToggleComponent(int x, int y, int width, int height, String label, boolean enabled, Runnable onChange) {
      super(x, y, width, height);
      this.label = label;
      this.enabled = enabled;
      this.onChange = onChange;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0) {
         this.enabled = !this.enabled;
         if (this.onChange != null) {
            this.onChange.run();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean enabled() {
      return this.enabled;
   }

   public String label() {
      return this.label;
   }

   public GlassToggleComponent setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }
}
