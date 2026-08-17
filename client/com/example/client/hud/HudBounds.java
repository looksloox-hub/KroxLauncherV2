package com.example.client.hud;

import java.util.Objects;

public record HudBounds(double x, double y, double width, double height) {
   public HudBounds(double x, double y, double width, double height) {
      if (Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(width) && Double.isFinite(height)) {
         if (!(width < (double)0.0F) && !(height < (double)0.0F)) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
         } else {
            throw new IllegalArgumentException("HudBounds width and height must be non-negative");
         }
      } else {
         throw new IllegalArgumentException("HudBounds values must be finite");
      }
   }

   public double right() {
      return this.x + this.width;
   }

   public double bottom() {
      return this.y + this.height;
   }

   public HudBounds withPosition(double newX, double newY) {
      return new HudBounds(newX, newY, this.width, this.height);
   }

   public HudBounds withSize(double newWidth, double newHeight) {
      return new HudBounds(this.x, this.y, newWidth, newHeight);
   }

   public HudBounds moveBy(double dx, double dy) {
      return new HudBounds(this.x + dx, this.y + dy, this.width, this.height);
   }

   public HudBounds inset(double padding) {
      return this.inset(padding, padding, padding, padding);
   }

   public HudBounds inset(double left, double top, double right, double bottom) {
      double nx = this.x + left;
      double ny = this.y + top;
      double nw = Math.max((double)0.0F, this.width - left - right);
      double nh = Math.max((double)0.0F, this.height - top - bottom);
      return new HudBounds(nx, ny, nw, nh);
   }

   public boolean contains(double px, double py) {
      return px >= this.x && py >= this.y && px <= this.right() && py <= this.bottom();
   }

   public boolean intersects(HudBounds other) {
      Objects.requireNonNull(other, "other");
      return other.right() > this.x && other.bottom() > this.y && other.x() < this.right() && other.y() < this.bottom();
   }

   public HudBounds clampWithin(HudBounds area) {
      Objects.requireNonNull(area, "area");
      double nx = Math.min(Math.max(this.x, area.x()), Math.max(area.x(), area.right() - this.width));
      double ny = Math.min(Math.max(this.y, area.y()), Math.max(area.y(), area.bottom() - this.height));
      return new HudBounds(nx, ny, this.width, this.height);
   }
}
