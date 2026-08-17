package com.example.client.hud.layout;

import com.example.client.hud.HudAlignItems;
import com.example.client.hud.HudJustifyContent;
import com.example.client.hud.HudOrientation;

public record HudAutoLayoutConfig(HudOrientation orientation, double gap, double padding, boolean wrap, HudJustifyContent justifyContent, HudAlignItems alignItems) {
   public HudAutoLayoutConfig(HudOrientation orientation, double gap, double padding, boolean wrap, HudJustifyContent justifyContent, HudAlignItems alignItems) {
      orientation = orientation == null ? HudOrientation.VERTICAL : orientation;
      justifyContent = justifyContent == null ? HudJustifyContent.START : justifyContent;
      alignItems = alignItems == null ? HudAlignItems.START : alignItems;
      gap = Math.max((double)0.0F, gap);
      padding = Math.max((double)0.0F, padding);
      this.orientation = orientation;
      this.gap = gap;
      this.padding = padding;
      this.wrap = wrap;
      this.justifyContent = justifyContent;
      this.alignItems = alignItems;
   }

   public static HudAutoLayoutConfig vertical() {
      return new HudAutoLayoutConfig(HudOrientation.VERTICAL, (double)6.0F, (double)0.0F, false, HudJustifyContent.START, HudAlignItems.START);
   }
}
