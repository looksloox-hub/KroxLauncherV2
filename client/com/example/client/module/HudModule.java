package com.example.client.module;

import com.example.client.ui.HudDragManager;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;

public abstract class HudModule extends Module {
   protected int hudWidth = 50;
   protected int hudHeight = 20;

   protected HudModule(String name, Category category, class_2960 icon) {
      super(name, category, icon);
   }

   public int getHudWidth() {
      return (int)((float)this.hudWidth * this.getScale());
   }

   public int getHudHeight() {
      return (int)((float)this.hudHeight * this.getScale());
   }

   public int getWidth() {
      return this.getHudWidth();
   }

   public int getHeight() {
      return this.getHudHeight();
   }

   public void setSize(int width, int height) {
      this.hudWidth = Math.max(10, width);
      this.hudHeight = Math.max(10, height);
   }

   public void setPosition(int x, int y) {
      super.setPosition(x, y);
   }

   public boolean isHovering(double mouseX, double mouseY) {
      return mouseX >= (double)this.getX() && mouseX <= (double)(this.getX() + this.getWidth()) && mouseY >= (double)this.getY() && mouseY <= (double)(this.getY() + this.getHeight());
   }

   public void startDrag(int mouseX, int mouseY) {
      if (this.isDraggable()) {
         super.startDrag(mouseX, mouseY);
      }
   }

   public void dragTo(int mouseX, int mouseY) {
      super.dragTo(mouseX, mouseY);
   }

   public void stopDrag() {
      super.stopDrag();
   }

   public void render(class_332 context) {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.method_22683() != null) {
         int mouseX = (int)(mc.field_1729.method_1603() * (double)mc.method_22683().method_4486() / (double)mc.method_22683().method_4480());
         int mouseY = (int)(mc.field_1729.method_1604() * (double)mc.method_22683().method_4502() / (double)mc.method_22683().method_4507());
         boolean mouseDown = mc.field_1729.method_1608();
         if (HudDragManager.current == this || HudDragManager.dragging) {
            HudDragManager.handleDragging(mouseX, mouseY, mouseDown);
            context.method_25294(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 2, this.getY() + this.getHeight() + 2, 1442840575);
            int snapX = HudDragManager.snap(this.getX(), 5);
            int snapY = HudDragManager.snap(this.getY(), 5);
            context.method_25294(snapX - 1, snapY - 1, snapX + 1, snapY + 1, -1);
         }

      }
   }
}
