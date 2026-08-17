package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class ArmorStatusModule extends Module {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/armor.png");
   private static final int ICON_SIZE = 16;
   private static final int TEXT_GAP = 4;
   private static final int ROW_GAP = 4;
   private static final int SEGMENT_GAP = 8;
   private static final int ROW_EXTRA_HEIGHT = 8;
   private static final int MIN_PANEL_SIZE = 24;
   private static final int DEFAULT_BG_R = 16;
   private static final int DEFAULT_BG_G = 19;
   private static final int DEFAULT_BG_B = 29;
   private final ModeSetting layoutMode = new ModeSetting("Layout", "Vertical", new String[]{"Vertical", "Horizontal"});
   private final BooleanSetting showHelmet = new BooleanSetting("Show Helmet", true);
   private final BooleanSetting showChestplate = new BooleanSetting("Show Chestplate", true);
   private final BooleanSetting showLeggings = new BooleanSetting("Show Leggings", true);
   private final BooleanSetting showBoots = new BooleanSetting("Show Boots", true);
   private final BooleanSetting showHeldItem = new BooleanSetting("Show Held Item", false);
   private final BooleanSetting showOffHandItem = new BooleanSetting("Show Off Hand Item", false);
   private final BooleanSetting showItemName = new BooleanSetting("Show Item Name", true);
   private final BooleanSetting showValue = new BooleanSetting("Show Value", true);
   private final ModeSetting valueFormat = new ModeSetting("Value Format", "Value", new String[]{"Value", "Percent"});
   private final ModeSetting nameColorMode = new ModeSetting("Name Color Mode", "Static", new String[]{"Static", "HUD", "Rainbow"});
   private final ModeSetting valueColorMode = new ModeSetting("Value Color Mode", "Gradient", new String[]{"Gradient", "Static", "HUD", "Rainbow"});
   private final NumberSetting nameColor = new NumberSetting("Name Color", (double)1.1568383E7F, (double)0.0F, (double)1.6777215E7F, (double)1.0F);
   private final NumberSetting valueColor = new NumberSetting("Value Color", (double)5635925.0F, (double)0.0F, (double)1.6777215E7F, (double)1.0F);
   private final BooleanSetting roundCorners = new BooleanSetting("Round Corners", true);
   private final NumberSetting padding = new NumberSetting("Padding", (double)4.0F, (double)2.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting radius = new NumberSetting("Radius", (double)4.0F, (double)0.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", (double)220.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineRed = new NumberSetting("Outline Red", (double)139.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineGreen = new NumberSetting("Outline Green", (double)92.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineBlue = new NumberSetting("Outline Blue", (double)246.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineOpacity = new NumberSetting("Outline Opacity", (double)200.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting glowHue = new NumberSetting("Glow Hue", 0.85, (double)0.0F, (double)1.0F, 0.01);
   private final NumberSetting glowSize = new NumberSetting("Glow Size", (double)8.0F, (double)0.0F, (double)32.0F, (double)1.0F);
   private final NumberSetting glowOpacity = new NumberSetting("Glow Opacity", (double)180.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final BooleanSetting textShadow = new BooleanSetting("Text Shadow", true);

   public ArmorStatusModule() {
      super("Armor Status", Category.HUD, ICON);
      this.x = 20;
      this.y = 100;
      this.width = 90;
      this.height = 24;
      this.addSetting(this.showHelmet);
      this.addSetting(this.showChestplate);
      this.addSetting(this.showLeggings);
      this.addSetting(this.showBoots);
      this.addSetting(this.showHeldItem);
      this.addSetting(this.showOffHandItem);
      this.addSetting(this.showItemName);
      this.addSetting(this.showValue);
      this.addSetting(this.valueFormat);
      this.addSetting(this.padding);
      this.addSetting(this.radius);
      this.addSetting(this.backgroundOpacity);
      this.addSetting(this.outlineRed);
      this.addSetting(this.outlineGreen);
      this.addSetting(this.outlineBlue);
      this.addSetting(this.outlineOpacity);
      this.addSetting(this.glowHue);
      this.addSetting(this.glowSize);
      this.addSetting(this.glowOpacity);
      this.addSetting(this.textShadow);
   }

   public void onEnable() {
      this.refreshSize();
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         RenderState state = this.recalculateLayout();
         if (state.entries.isEmpty()) {
            this.width = 18;
            this.height = 18;
         } else {
            this.width = state.panelW;
            this.height = state.panelH;
            int bgColor = applyAlpha(rgbColor(16, 19, 29), (int)this.backgroundOpacity.getValue());
            int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineOpacity.getValue());
            int accentColor = this.getHudColor();
            int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), accentColor), (int)this.glowOpacity.getValue());
            float radiusValue = this.roundCorners.getValue() ? (float)this.radius.getValue() : 0.0F;
            int glowSpread = Math.max(0, (int)this.glowSize.getValue());
            int outlineThickness = Math.max(1, this.getOutlineThickness());
            boolean drawBackground = this.isBox();
            if (this.isGlow()) {
               RoundedRectRenderer.glow(context, (float)(-glowSpread), (float)(-glowSpread), (float)(state.panelW + glowSpread * 2), (float)(state.panelH + glowSpread * 2), radiusValue + (float)glowSpread * 0.35F, glowColor);
            }

            if (drawBackground) {
               RoundedRectRenderer.fill(context, 0.0F, 0.0F, (float)state.panelW, (float)state.panelH, radiusValue, bgColor);
            }

            if (this.isOutline()) {
               RoundedRectRenderer.outline(context, 0.0F, 0.0F, (float)state.panelW, (float)state.panelH, radiusValue, (float)outlineThickness, outlineColor, drawBackground ? bgColor : 0);
            }

            if (this.layoutMode.is("Vertical")) {
               this.renderVertical(context, state.entries, state.layout, state.panelW, state.panelH, state.pad);
            } else {
               this.renderHorizontal(context, state.entries, state.layout, state.panelW, state.panelH, state.pad);
            }

         }
      }
   }

   private RenderState recalculateLayout() {
      if (mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         List<DisplayEntry> entries = this.buildEntries();
         Layout layout = this.measureLayout(entries);
         int pad = (int)this.padding.getValue();
         int panelW;
         int panelH;
         if (this.layoutMode.is("Vertical")) {
            panelW = Math.max(24, layout.maxRowW + pad * 2);
            panelH = Math.max(24, layout.totalH + pad * 2);
         } else {
            panelW = Math.max(24, layout.totalW + pad * 2);
            panelH = Math.max(24, layout.rowH + pad * 2);
         }

         return new RenderState(entries, layout, panelW, panelH, pad);
      } else {
         return new RenderState(new ArrayList(), new Layout(18, 18, 18, new ArrayList(), 18), 18, 18, 4);
      }
   }

   private void renderVertical(class_332 context, List<DisplayEntry> entries, Layout layout, int panelW, int panelH, int pad) {
      Objects.requireNonNull(mc.field_1772);
      int fontH = 9;
      int rowH = layout.rowH;
      int yCursor = pad;

      for(DisplayEntry entry : entries) {
         int iconY = yCursor + (rowH - 16) / 2;
         int textY = yCursor + (rowH - fontH) / 2 - 1;
         int nameX = pad + 16 + 4;
         int valueW = this.showValue.getValue() && !entry.valueText.isEmpty() ? mc.field_1772.method_1727(entry.valueText) : 0;
         int valueX = panelW - pad - valueW;
         int maxNameW = Math.max(0, valueX - 4 - nameX);
         if (this.showItemName.getValue() && !entry.nameText.isEmpty()) {
            String trimmedName = mc.field_1772.method_27523(entry.nameText, maxNameW);
            context.method_51439(mc.field_1772, class_2561.method_43470(trimmedName), nameX, textY, this.resolveNameColor(), this.textShadow.getValue());
         }

         if (this.showValue.getValue() && !entry.valueText.isEmpty()) {
            context.method_51439(mc.field_1772, class_2561.method_43470(entry.valueText), valueX, textY, this.resolveValueColor(entry), this.textShadow.getValue());
         }

         context.method_51427(entry.stack, pad, iconY);
         yCursor += rowH + 4;
      }

   }

   private void renderHorizontal(class_332 context, List<DisplayEntry> entries, Layout layout, int panelW, int panelH, int pad) {
      Objects.requireNonNull(mc.field_1772);
      int fontH = 9;
      int rowH = layout.rowH;
      int xCursor = pad;
      int rowY = pad;

      for(int i = 0; i < entries.size(); ++i) {
         DisplayEntry entry = (DisplayEntry)entries.get(i);
         int segmentW = (Integer)layout.segmentWidths.get(i);
         int iconY = rowY + (rowH - 16) / 2;
         int textY = rowY + (rowH - fontH) / 2 - 1;
         int nameX = xCursor + 16 + 4;
         int valueW = this.showValue.getValue() && !entry.valueText.isEmpty() ? mc.field_1772.method_1727(entry.valueText) : 0;
         int valueX = xCursor + segmentW - valueW;
         int maxNameW = Math.max(0, valueX - 4 - nameX);
         if (this.showItemName.getValue() && !entry.nameText.isEmpty()) {
            String trimmedName = mc.field_1772.method_27523(entry.nameText, maxNameW);
            context.method_51439(mc.field_1772, class_2561.method_43470(trimmedName), nameX, textY, this.resolveNameColor(), this.textShadow.getValue());
         }

         if (this.showValue.getValue() && !entry.valueText.isEmpty()) {
            context.method_51439(mc.field_1772, class_2561.method_43470(entry.valueText), valueX, textY, this.resolveValueColor(entry), this.textShadow.getValue());
         }

         context.method_51427(entry.stack, xCursor, iconY);
         xCursor += segmentW + 8;
      }

   }

   private void refreshSize() {
      RenderState state = this.recalculateLayout();
      this.width = state.panelW;
      this.height = state.panelH;
   }

   private Layout measureLayout(List<DisplayEntry> entries) {
      if (mc != null && mc.field_1772 != null && !entries.isEmpty()) {
         Objects.requireNonNull(mc.field_1772);
         int fontH = 9;
         int rowH = Math.max(16, fontH) + 8;
         List<Integer> segmentWidths = new ArrayList(entries.size());
         int maxRowW = 0;
         int totalW = 0;
         int totalH = entries.size() * rowH + Math.max(0, entries.size() - 1) * 4;

         for(DisplayEntry entry : entries) {
            int nameW = this.showItemName.getValue() ? mc.field_1772.method_1727(entry.nameText) : 0;
            int valueW = this.showValue.getValue() && !entry.valueText.isEmpty() ? mc.field_1772.method_1727(entry.valueText) : 0;
            int segmentW = 16;
            if (this.showItemName.getValue() && !entry.nameText.isEmpty()) {
               segmentW += 4 + nameW;
            }

            if (this.showValue.getValue() && !entry.valueText.isEmpty()) {
               segmentW += 4 + valueW;
            }

            segmentW = Math.max(segmentW, 16);
            segmentWidths.add(segmentW);
            maxRowW = Math.max(maxRowW, segmentW);
            totalW += segmentW;
         }

         totalW += Math.max(0, entries.size() - 1) * 8;
         return new Layout(maxRowW, rowH, totalH, segmentWidths, totalW);
      } else {
         return new Layout(18, 18, 18, new ArrayList(), 18);
      }
   }

   private List<DisplayEntry> buildEntries() {
      List<DisplayEntry> out = new ArrayList();
      if (mc != null && mc.field_1724 != null) {
         if (this.showHelmet.getValue()) {
            this.addEntry(out, mc.field_1724.method_31548().method_5438(39));
         }

         if (this.showChestplate.getValue()) {
            this.addEntry(out, mc.field_1724.method_31548().method_5438(38));
         }

         if (this.showLeggings.getValue()) {
            this.addEntry(out, mc.field_1724.method_31548().method_5438(37));
         }

         if (this.showBoots.getValue()) {
            this.addEntry(out, mc.field_1724.method_31548().method_5438(36));
         }

         if (this.showHeldItem.getValue()) {
            this.addEntry(out, mc.field_1724.method_6047());
         }

         if (this.showOffHandItem.getValue()) {
            this.addEntry(out, mc.field_1724.method_6079());
         }

         return out;
      } else {
         return out;
      }
   }

   private void addEntry(List<DisplayEntry> out, class_1799 stack) {
      if (stack != null && !stack.method_7960()) {
         out.add(new DisplayEntry(stack, stack.method_7964().getString(), this.buildValueText(stack)));
      }
   }

   private String buildValueText(class_1799 stack) {
      if (stack != null && !stack.method_7960()) {
         if (!stack.method_7963()) {
            return "";
         } else {
            int max = Math.max(1, stack.method_7936());
            int current = Math.max(0, max - stack.method_7919());
            if (this.valueFormat.is("Percent")) {
               long var10000 = Math.round((double)current * (double)100.0F / (double)max);
               return (int)var10000 + "%";
            } else {
               return String.valueOf(current);
            }
         }
      } else {
         return "";
      }
   }

   private int resolveNameColor() {
      if (this.nameColorMode.is("Rainbow")) {
         return this.rainbowColor();
      } else {
         return this.nameColorMode.is("HUD") ? this.getHudColor() : this.resolveStaticColor(this.nameColor);
      }
   }

   private int resolveValueColor(DisplayEntry entry) {
      if (this.valueColorMode.is("Rainbow")) {
         return this.rainbowColor();
      } else if (this.valueColorMode.is("HUD")) {
         return this.getHudColor();
      } else if (this.valueColorMode.is("Static")) {
         return this.resolveStaticColor(this.valueColor);
      } else if (entry.stack != null && !entry.stack.method_7960() && entry.stack.method_7963()) {
         int max = Math.max(1, entry.stack.method_7936());
         int current = Math.max(0, max - entry.stack.method_7919());
         float ratio = (float)current / (float)max;
         if (ratio >= 0.75F) {
            return rgbColor(85, 255, 85);
         } else if (ratio >= 0.5F) {
            return rgbColor(255, 255, 85);
         } else {
            return ratio >= 0.25F ? rgbColor(255, 170, 0) : rgbColor(255, 85, 85);
         }
      } else {
         return this.resolveStaticColor(this.valueColor);
      }
   }

   private int resolveStaticColor(NumberSetting setting) {
      int raw = (int)setting.getValue();
      return rgbColor(raw >> 16 & 255, raw >> 8 & 255, raw & 255);
   }

   private int hueGlowColor(float hue, int baseTextColor) {
      hue = Math.max(0.0F, Math.min(1.0F, hue));
      float[] hsb = Color.RGBtoHSB(baseTextColor >> 16 & 255, baseTextColor >> 8 & 255, baseTextColor & 255, (float[])null);
      int rgb = Color.HSBtoRGB(hue, Math.max(0.45F, hsb[1]), 1.0F);
      return rgb & 16777215;
   }

   private int rainbowColor() {
      float time = (float)(System.currentTimeMillis() % 6000L) / 6000.0F;
      return Color.HSBtoRGB(time, 0.85F, 1.0F) | -16777216;
   }

   private static int rgbColor(int r, int g, int b) {
      return -16777216 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(b);
   }

   private static int applyAlpha(int color, int alpha) {
      return clamp255(alpha) << 24 | color & 16777215;
   }

   private static int clamp255(int value) {
      return Math.max(0, Math.min(255, value));
   }

   private static final class DisplayEntry {
      private final class_1799 stack;
      private final String nameText;
      private final String valueText;

      private DisplayEntry(class_1799 stack, String nameText, String valueText) {
         this.stack = stack;
         this.nameText = nameText == null ? "" : nameText;
         this.valueText = valueText == null ? "" : valueText;
      }
   }

   private static final class Layout {
      private final int maxRowW;
      private final int rowH;
      private final int totalH;
      private final List<Integer> segmentWidths;
      private final int totalW;

      private Layout(int maxRowW, int rowH, int totalH, List<Integer> segmentWidths, int totalW) {
         this.maxRowW = maxRowW;
         this.rowH = rowH;
         this.totalH = totalH;
         this.segmentWidths = segmentWidths;
         this.totalW = totalW;
      }
   }

   private static final class RenderState {
      private final List<DisplayEntry> entries;
      private final Layout layout;
      private final int panelW;
      private final int panelH;
      private final int pad;

      private RenderState(List<DisplayEntry> entries, Layout layout, int panelW, int panelH, int pad) {
         this.entries = entries;
         this.layout = layout;
         this.panelW = panelW;
         this.panelH = panelH;
         this.pad = pad;
      }
   }
}
