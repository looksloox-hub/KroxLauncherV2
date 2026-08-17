package com.example.client.ui.core;

import net.minecraft.class_332;

public abstract class GlassComponent {
   protected int x;
   protected int y;
   protected int width;
   protected int height;

   protected GlassComponent(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return false;
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return false;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return false;
   }

   public boolean charTyped(char chr, int modifiers) {
      return false;
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   public GlassComponent setX(int x) {
      this.x = x;
      return this;
   }

   public GlassComponent setY(int y) {
      this.y = y;
      return this;
   }

   public GlassComponent setWidth(int width) {
      this.width = width;
      return this;
   }

   public GlassComponent setHeight(int height) {
      this.height = height;
      return this;
   }
}
