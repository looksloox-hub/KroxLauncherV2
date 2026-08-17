package com.example.client.hud.layout;

import com.example.client.hud.HudAlignItems;
import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import com.example.client.hud.HudJustifyContent;
import com.example.client.hud.HudOrientation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class HudAutoLayout {
   public void layout(Collection<? extends HudElement> children, HudBounds container, HudAutoLayoutConfig config, HudLayoutAdapter adapter) {
      Objects.requireNonNull(children, "children");
      Objects.requireNonNull(container, "container");
      Objects.requireNonNull(config, "config");
      Objects.requireNonNull(adapter, "adapter");
      List<HudElement> list = new ArrayList(children);
      if (!list.isEmpty()) {
         double startX = container.x() + config.padding();
         double startY = container.y() + config.padding();
         double innerWidth = Math.max((double)0.0F, container.width() - config.padding() * (double)2.0F);
         double innerHeight = Math.max((double)0.0F, container.height() - config.padding() * (double)2.0F);
         if (config.orientation() == HudOrientation.VERTICAL) {
            this.layoutVertical(list, startX, startY, innerWidth, innerHeight, config, adapter);
         } else {
            this.layoutHorizontal(list, startX, startY, innerWidth, innerHeight, config, adapter);
         }

      }
   }

   private void layoutVertical(List<HudElement> children, double startX, double startY, double innerWidth, double innerHeight, HudAutoLayoutConfig config, HudLayoutAdapter adapter) {
      double totalHeight = (double)0.0F;

      for(HudElement child : children) {
         totalHeight += adapter.measure(child).height();
      }

      totalHeight += (double)Math.max(0, children.size() - 1) * config.gap();
      double var10000;
      switch (config.justifyContent()) {
         case CENTER:
            var10000 = startY + Math.max((double)0.0F, (innerHeight - totalHeight) / (double)2.0F);
            break;
         case END:
            var10000 = startY + Math.max((double)0.0F, innerHeight - totalHeight);
            break;
         case SPACE_BETWEEN:
         case SPACE_AROUND:
         case START:
            var10000 = startY;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      double y = var10000;
      double extraGap = config.justifyContent() == HudJustifyContent.SPACE_BETWEEN && children.size() > 1 ? Math.max((double)0.0F, (innerHeight - totalHeight) / (double)(children.size() - 1)) : config.gap();

      for(HudElement child : children) {
         HudBounds measured = adapter.measure(child);
         double width = config.alignItems() == HudAlignItems.STRETCH ? innerWidth : measured.width();
         double x = alignedX(startX, innerWidth, width, config.alignItems());
         adapter.layout(child, new HudBounds(x, y, width, measured.height()));
         y += measured.height() + extraGap;
      }

   }

   private void layoutHorizontal(List<HudElement> children, double startX, double startY, double innerWidth, double innerHeight, HudAutoLayoutConfig config, HudLayoutAdapter adapter) {
      double totalWidth = (double)0.0F;

      for(HudElement child : children) {
         totalWidth += adapter.measure(child).width();
      }

      totalWidth += (double)Math.max(0, children.size() - 1) * config.gap();
      double var10000;
      switch (config.justifyContent()) {
         case CENTER:
            var10000 = startX + Math.max((double)0.0F, (innerWidth - totalWidth) / (double)2.0F);
            break;
         case END:
            var10000 = startX + Math.max((double)0.0F, innerWidth - totalWidth);
            break;
         case SPACE_BETWEEN:
         case SPACE_AROUND:
         case START:
            var10000 = startX;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      double x = var10000;
      double extraGap = config.justifyContent() == HudJustifyContent.SPACE_BETWEEN && children.size() > 1 ? Math.max((double)0.0F, (innerWidth - totalWidth) / (double)(children.size() - 1)) : config.gap();

      for(HudElement child : children) {
         HudBounds measured = adapter.measure(child);
         double height = config.alignItems() == HudAlignItems.STRETCH ? innerHeight : measured.height();
         double y = alignedY(startY, innerHeight, height, config.alignItems());
         adapter.layout(child, new HudBounds(x, y, measured.width(), height));
         x += measured.width() + extraGap;
      }

   }

   private static double alignedX(double startX, double innerWidth, double width, HudAlignItems alignItems) {
      double var10000;
      switch (alignItems) {
         case CENTER:
            var10000 = startX + Math.max((double)0.0F, (innerWidth - width) / (double)2.0F);
            break;
         case END:
            var10000 = startX + Math.max((double)0.0F, innerWidth - width);
            break;
         case START:
         case STRETCH:
            var10000 = startX;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private static double alignedY(double startY, double innerHeight, double height, HudAlignItems alignItems) {
      double var10000;
      switch (alignItems) {
         case CENTER:
            var10000 = startY + Math.max((double)0.0F, (innerHeight - height) / (double)2.0F);
            break;
         case END:
            var10000 = startY + Math.max((double)0.0F, innerHeight - height);
            break;
         case START:
         case STRETCH:
            var10000 = startY;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }
}
