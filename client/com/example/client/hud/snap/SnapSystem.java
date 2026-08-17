package com.example.client.hud.snap;

import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class SnapSystem {
   private double threshold = (double)8.0F;
   private double gridSize = (double)8.0F;
   private boolean snapToEdges = true;
   private boolean snapToElements = true;
   private boolean snapToGrid = true;

   public double threshold() {
      return this.threshold;
   }

   public void setThreshold(double threshold) {
      this.threshold = Math.max((double)0.0F, threshold);
   }

   public double gridSize() {
      return this.gridSize;
   }

   public void setGridSize(double gridSize) {
      this.gridSize = Math.max((double)1.0F, gridSize);
   }

   public boolean snapToEdges() {
      return this.snapToEdges;
   }

   public void setSnapToEdges(boolean snapToEdges) {
      this.snapToEdges = snapToEdges;
   }

   public boolean snapToElements() {
      return this.snapToElements;
   }

   public void setSnapToElements(boolean snapToElements) {
      this.snapToElements = snapToElements;
   }

   public boolean snapToGrid() {
      return this.snapToGrid;
   }

   public void setSnapToGrid(boolean snapToGrid) {
      this.snapToGrid = snapToGrid;
   }

   public SnapResult snap(HudBounds candidate, HudBounds screenBounds, Collection<? extends HudElement> elements) {
      Objects.requireNonNull(candidate, "candidate");
      Objects.requireNonNull(screenBounds, "screenBounds");
      List<SnapGuide> guides = new ArrayList();
      double x = candidate.x();
      double y = candidate.y();
      double w = candidate.width();
      double h = candidate.height();
      if (this.snapToEdges) {
         x = snapAxis(x, screenBounds.x(), this.threshold, guides, SnapKind.SCREEN_EDGE, "x", "left", "screen-left");
         x = snapAxis(x, screenBounds.right() - w, this.threshold, guides, SnapKind.SCREEN_EDGE, "x", "right", "screen-right");
         y = snapAxis(y, screenBounds.y(), this.threshold, guides, SnapKind.SCREEN_EDGE, "y", "top", "screen-top");
         y = snapAxis(y, screenBounds.bottom() - h, this.threshold, guides, SnapKind.SCREEN_EDGE, "y", "bottom", "screen-bottom");
      }

      if (this.snapToGrid) {
         double gx = roundToGrid(x, this.gridSize);
         double gy = roundToGrid(y, this.gridSize);
         if (Math.abs(gx - x) <= this.threshold) {
            guides.add(new SnapGuide(SnapKind.GRID, "grid", "x", gx, Math.abs(gx - x)));
            x = gx;
         }

         if (Math.abs(gy - y) <= this.threshold) {
            guides.add(new SnapGuide(SnapKind.GRID, "grid", "y", gy, Math.abs(gy - y)));
            y = gy;
         }
      }

      if (this.snapToElements) {
         for(HudElement element : elements) {
            if (element != null && element.bounds() != candidate) {
               HudBounds other = element.bounds();
               x = snapAxis(x, other.x() - w, this.threshold, guides, SnapKind.ELEMENT_EDGE, "x", "left", element.id());
               x = snapAxis(x, other.right(), this.threshold, guides, SnapKind.ELEMENT_EDGE, "x", "left-to-right", element.id());
               x = snapAxis(x, other.right() - w, this.threshold, guides, SnapKind.ELEMENT_EDGE, "x", "right", element.id());
               x = snapAxis(x, other.x(), this.threshold, guides, SnapKind.ELEMENT_EDGE, "x", "right-to-left", element.id());
               y = snapAxis(y, other.y() - h, this.threshold, guides, SnapKind.ELEMENT_EDGE, "y", "top", element.id());
               y = snapAxis(y, other.bottom(), this.threshold, guides, SnapKind.ELEMENT_EDGE, "y", "top-to-bottom", element.id());
               y = snapAxis(y, other.bottom() - h, this.threshold, guides, SnapKind.ELEMENT_EDGE, "y", "bottom", element.id());
               y = snapAxis(y, other.y(), this.threshold, guides, SnapKind.ELEMENT_EDGE, "y", "bottom-to-top", element.id());
            }
         }
      }

      return new SnapResult(new HudBounds(x, y, w, h), guides);
   }

   private static double snapAxis(double value, double target, double threshold, List<SnapGuide> guides, SnapKind kind, String axis, String label, String sourceId) {
      double distance = Math.abs(value - target);
      if (distance <= threshold) {
         guides.add(new SnapGuide(kind, sourceId, axis + ":" + label, target, distance));
         return target;
      } else {
         return value;
      }
   }

   private static double roundToGrid(double value, double grid) {
      return (double)Math.round(value / grid) * grid;
   }
}
