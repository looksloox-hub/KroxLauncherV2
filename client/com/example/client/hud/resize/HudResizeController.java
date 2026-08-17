package com.example.client.hud.resize;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import com.example.client.hud.snap.SnapResult;
import com.example.client.hud.snap.SnapSystem;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class HudResizeController {
   private HudElement element;
   private ResizeHandle handle;
   private double startMouseX;
   private double startMouseY;
   private HudBounds startBounds;
   private boolean active;
   private double minWidth;
   private double minHeight;

   public HudResizeController() {
      this.handle = ResizeHandle.NONE;
      this.minWidth = (double)16.0F;
      this.minHeight = (double)16.0F;
   }

   public void begin(HudElement element, ResizeHandle handle, double mouseX, double mouseY) {
      this.element = (HudElement)Objects.requireNonNull(element, "element");
      this.handle = (ResizeHandle)Objects.requireNonNull(handle, "handle");
      this.startMouseX = mouseX;
      this.startMouseY = mouseY;
      this.startBounds = element.bounds();
      this.active = handle != ResizeHandle.NONE;
   }

   public Optional<ResizeState> update(double mouseX, double mouseY, HudBounds screenBounds, Collection<? extends HudElement> otherElements, SnapSystem snapSystem) {
      if (this.active && this.element != null && this.startBounds != null) {
         double dx = mouseX - this.startMouseX;
         double dy = mouseY - this.startMouseY;
         HudBounds candidate = resize(this.startBounds, this.handle, dx, dy, this.minWidth, this.minHeight);
         SnapResult snapped = snapSystem.snap(candidate, screenBounds, otherElements);
         return Optional.of(new ResizeState(true, this.element, snapped.bounds(), this.handle));
      } else {
         return Optional.empty();
      }
   }

   public void end() {
      this.active = false;
      this.element = null;
      this.handle = ResizeHandle.NONE;
      this.startBounds = null;
      this.startMouseX = (double)0.0F;
      this.startMouseY = (double)0.0F;
   }

   public boolean active() {
      return this.active;
   }

   public void setMinimumSize(double minWidth, double minHeight) {
      this.minWidth = Math.max((double)1.0F, minWidth);
      this.minHeight = Math.max((double)1.0F, minHeight);
   }

   private static HudBounds resize(HudBounds bounds, ResizeHandle handle, double dx, double dy, double minWidth, double minHeight) {
      double x = bounds.x();
      double y = bounds.y();
      double width = bounds.width();
      double height = bounds.height();
      switch (handle) {
         case LEFT:
            x += dx;
            width -= dx;
            break;
         case RIGHT:
            width += dx;
            break;
         case TOP:
            y += dy;
            height -= dy;
            break;
         case BOTTOM:
            height += dy;
            break;
         case TOP_LEFT:
            x += dx;
            width -= dx;
            y += dy;
            height -= dy;
            break;
         case TOP_RIGHT:
            width += dx;
            y += dy;
            height -= dy;
            break;
         case BOTTOM_LEFT:
            x += dx;
            width -= dx;
            height += dy;
            break;
         case BOTTOM_RIGHT:
            width += dx;
            height += dy;
      }

      if (width < minWidth) {
         if (handle == ResizeHandle.LEFT || handle == ResizeHandle.TOP_LEFT || handle == ResizeHandle.BOTTOM_LEFT) {
            x -= minWidth - width;
         }

         width = minWidth;
      }

      if (height < minHeight) {
         if (handle == ResizeHandle.TOP || handle == ResizeHandle.TOP_LEFT || handle == ResizeHandle.TOP_RIGHT) {
            y -= minHeight - height;
         }

         height = minHeight;
      }

      return new HudBounds(x, y, width, height);
   }
}
