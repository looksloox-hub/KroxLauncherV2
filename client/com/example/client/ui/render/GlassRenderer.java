package com.example.client.ui.render;

import com.example.client.ui.theme.GlassTheme;
import com.example.client.ui.theme.GlassThemeManager;
import com.example.client.ui.util.ColorUtil;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;

public final class GlassRenderer {
   private GlassRenderer() {
   }

   public static void drawPanel(class_332 context, float x, float y, float width, float height) {
      GlassTheme theme = GlassThemeManager.active();
      int outer = theme.border();
      int inner = theme.panel();
      RoundedRectRenderer.outline(context, x, y, width, height, theme.resolvePanelRadius(width, height), 1.0F, outer, inner);
   }

   public static void drawCard(class_332 context, float x, float y, float width, float height) {
      GlassTheme theme = GlassThemeManager.active();
      int outer = ColorUtil.scaleAlpha(theme.border(), 0.85F);
      int inner = theme.card();
      RoundedRectRenderer.outline(context, x, y, width, height, theme.resolveCardRadius(width, height), 1.0F, outer, inner);
   }

   public static void drawButton(class_332 context, float x, float y, float width, float height, String label, boolean hovered, boolean pressed) {
      GlassTheme theme = GlassThemeManager.active();
      float hoverLift = hovered ? 3.0F : 0.0F;
      float hoverExpand = hovered ? 3.0F : 0.0F;
      float pressInset = pressed ? 1.5F : 0.0F;
      float drawX = x - hoverExpand * 0.5F + pressInset * 0.5F;
      float drawY = y - hoverLift + pressInset * 0.5F;
      float drawW = width + hoverExpand - pressInset;
      float drawH = height - pressInset;
      int outer = hovered ? theme.borderStrong() : theme.border();
      int inner = theme.button(hovered, pressed);
      RoundedRectRenderer.outline(context, drawX, drawY, drawW, drawH, theme.resolveButtonRadius(drawW, drawH), 1.0F, outer, inner);
      if (label != null && !label.isEmpty()) {
         drawTextCentered(context, label, drawX + drawW * 0.5F, drawY + (drawH - 8.0F) * 0.5F + 1.0F, theme.text());
      }

   }

   public static void drawToggle(class_332 context, float x, float y, float width, float height, String label, boolean value, float progress) {
      GlassTheme theme = GlassThemeManager.active();
      int outer = theme.border();
      int inner = value ? theme.button(true, false) : theme.button(false, false);
      RoundedRectRenderer.outline(context, x, y, width, height, theme.resolveButtonRadius(width, height), 1.0F, outer, inner);
      float p = Math.max(0.0F, Math.min(1.0F, progress));
      float knobSize = Math.max(10.0F, height - 6.0F);
      float knobY = y + (height - knobSize) * 0.5F;
      float knobX = x + 3.0F + (width - knobSize - 6.0F) * p;
      RoundedRectRenderer.pill(context, knobX, knobY, knobSize, knobSize, value ? theme.accent() : theme.borderStrong());
      if (label != null && !label.isEmpty()) {
         drawText(context, label, x + width + 8.0F, y + (height - 8.0F) * 0.5F + 1.0F, theme.text());
      }

   }

   public static void drawSearchBar(class_332 context, float x, float y, float width, float height, String text, String placeholder, boolean focused, boolean cursorVisible) {
      GlassTheme theme = GlassThemeManager.active();
      int outer = focused ? theme.borderStrong() : theme.border();
      int inner = focused ? theme.button(true, false) : theme.panel();
      RoundedRectRenderer.outline(context, x, y, width, height, theme.resolveFieldRadius(width, height), 1.0F, outer, inner);
      String value = text == null ? "" : text;
      boolean hasText = !value.isEmpty();
      int color = hasText ? theme.text() : theme.textDim();
      String toDraw = hasText ? value : (placeholder == null ? "" : placeholder);
      drawText(context, toDraw, x + 10.0F, y + (height - 8.0F) * 0.5F + 1.0F, color);
      if (focused && cursorVisible) {
         int textWidth = class_310.method_1551().field_1772.method_1727(value);
         int cursorX = Math.round(x + 10.0F + (float)textWidth + 1.0F);
         RoundedRectRenderer.pill(context, (float)cursorX, y + 6.0F, 1.5F, height - 12.0F, theme.accent());
      }

   }

   public static void drawModuleCard(class_332 context, float x, float y, float width, float height, String title, String category, String iconGlyph, boolean enabled, boolean hovered) {
      GlassTheme theme = GlassThemeManager.active();
      int outer = hovered ? theme.borderStrong() : theme.border();
      int inner = enabled ? theme.card() : ColorUtil.darken(theme.card(), 0.03F);
      RoundedRectRenderer.outline(context, x, y, width, height, theme.resolveCardRadius(width, height), 1.0F, outer, inner);
      if (iconGlyph != null && !iconGlyph.isEmpty()) {
         drawText(context, iconGlyph, x + 12.0F, y + 10.0F, enabled ? theme.text() : theme.textDim());
      }

      if (title != null && !title.isEmpty()) {
         drawText(context, title, x + 40.0F, y + 10.0F, theme.text());
      }

      if (category != null && !category.isEmpty()) {
         drawText(context, category, x + 40.0F, y + 22.0F, theme.textDim());
      }

      drawToggle(context, x + width - 54.0F, y + 12.0F, 40.0F, 18.0F, "", enabled, enabled ? 1.0F : 0.0F);
   }

   public static void drawText(class_332 context, String text, float x, float y) {
      drawText(context, text, x, y, GlassThemeManager.active().text());
   }

   public static void drawText(class_332 context, String text, float x, float y, int color) {
      if (text != null && !text.isEmpty()) {
         class_310 client = class_310.method_1551();
         context.method_51439(client.field_1772, class_2561.method_43470(text), Math.round(x), Math.round(y), color, false);
      }
   }

   public static void drawTextCentered(class_332 context, String text, float centerX, float y, int color) {
      if (text != null && !text.isEmpty()) {
         class_310 client = class_310.method_1551();
         int width = client.field_1772.method_1727(text);
         context.method_51439(client.field_1772, class_2561.method_43470(text), Math.round(centerX - (float)width * 0.5F), Math.round(y), color, false);
      }
   }
}
