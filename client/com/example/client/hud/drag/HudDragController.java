package com.example.client.hud.drag;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import com.example.client.hud.snap.SnapResult;
import com.example.client.hud.snap.SnapSystem;
import java.util.Collection;
import java.util.Objects;

public final class HudDragController {
   private HudElement element;
   private double grabOffsetX;
   private double grabOffsetY;
   private boolean active;

   public void begin(HudElement element, double mouseX, double mouseY) {
      this.element = (HudElement)Objects.requireNonNull(element, "element");
      HudBounds bounds = element.bounds();
      this.grabOffsetX = mouseX - bounds.x();
      this.grabOffsetY = mouseY - bounds.y();
      this.active = true;
   }

   public DragState update(double mouseX, double mouseY, HudBounds screenBounds, Collection<? extends HudElement> otherElements, SnapSystem snapSystem) {
      if (this.active && this.element != null) {
         HudBounds current = this.element.bounds();
         HudBounds candidate = new HudBounds(mouseX - this.grabOffsetX, mouseY - this.grabOffsetY, current.width(), current.height());
         SnapResult snap = snapSystem.snap(candidate, screenBounds, otherElements);
         return new DragState(true, this.element, snap.bounds(), this.grabOffsetX, this.grabOffsetY);
      } else {
         return DragState.idle();
      }
   }

   public void end() {
      this.active = false;
      this.element = null;
      this.grabOffsetX = (double)0.0F;
      this.grabOffsetY = (double)0.0F;
   }

   public boolean active() {
      return this.active;
   }
}
