package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassTextFieldComponent extends GlassComponent {
   private String placeholder;
   private String value = "";

   public GlassTextFieldComponent(int x, int y, int width, int height, String placeholder) {
      super(x, y, width, height);
      this.placeholder = placeholder;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public String placeholder() {
      return this.placeholder;
   }

   public String value() {
      return this.value;
   }

   public GlassTextFieldComponent setValue(String value) {
      this.value = value == null ? "" : value;
      return this;
   }
}
