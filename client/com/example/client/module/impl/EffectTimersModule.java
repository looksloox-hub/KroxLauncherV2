package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_7923;

public class EffectTimersModule extends Module {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/clock.png");
   private static final int ROW_GAP = 4;
   private static final int BASE_ROW_HEIGHT = 40;
   private static final int ICON_BOX_WIDTH = 40;
   private static final float TILE_RADIUS = 4.0F;
   private static final float PANEL_RADIUS = 4.0F;
   private static final float BADGE_RADIUS = 2.0F;
   private static final int DEFAULT_ICON_OUTER = -13944504;
   private static final int DEFAULT_ICON_INNER = -11509389;
   private static final int DEFAULT_PANEL_OUTER = -11114115;
   private static final int DEFAULT_PANEL_INNER = -8679776;
   private static final int DEFAULT_BADGE_BG = -16119286;
   private static final int SEAM_COLOR = -14800845;
   private static final int TEXT_COLOR = -1;
   private final NumberSetting displayMode = new NumberSetting("Display Mode", (double)0.0F, (double)0.0F, (double)2.0F, (double)1.0F);
   private final BooleanSetting showTitle = new BooleanSetting("Show Title", false);
   private final BooleanSetting showDuration = new BooleanSetting("Show Duration", true);
   private final BooleanSetting showAmplifier = new BooleanSetting("Show Amplifier", true);
   private final BooleanSetting showProgressBar = new BooleanSetting("Show Progress Bar", false);
   private final BooleanSetting showBackground = new BooleanSetting("Show Background", false);
   private final BooleanSetting showGlow = new BooleanSetting("Show Glow", false);
   private final BooleanSetting showOutline = new BooleanSetting("Show Outline", false);
   private final BooleanSetting useEffectColor = new BooleanSetting("Use Effect Color", false);
   private final BooleanSetting rainbowText = new BooleanSetting("Rainbow Text", false);
   private final BooleanSetting rainbowBars = new BooleanSetting("Rainbow Bars", false);
   private final BooleanSetting compactSpacing = new BooleanSetting("Compact Spacing", true);
   private final BooleanSetting sortLongestFirst = new BooleanSetting("Longest First", true);
   private final BooleanSetting iconLetters = new BooleanSetting("Icon Letters", false);
   private final BooleanSetting darkTheme = new BooleanSetting("Dark Theme", true);
   private final BooleanSetting ultraDarkTheme = new BooleanSetting("Ultra Dark Theme", false);
   private final NumberSetting darkThemeOpacity = new NumberSetting("Theme Opacity", (double)220.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting ultraDarkOpacity = new NumberSetting("Ultra Theme Opacity", (double)245.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting padding = new NumberSetting("Padding", (double)4.0F, (double)2.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting rowHeight = new NumberSetting("Row Height", (double)40.0F, (double)20.0F, (double)48.0F, (double)1.0F);
   private final NumberSetting iconSize = new NumberSetting("Icon Size", (double)24.0F, (double)12.0F, (double)40.0F, (double)1.0F);
   private final NumberSetting maxVisibleEffects = new NumberSetting("Max Effects", (double)8.0F, (double)1.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", (double)220.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundRed = new NumberSetting("Background Red", (double)16.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundGreen = new NumberSetting("Background Green", (double)19.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundBlue = new NumberSetting("Background Blue", (double)29.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textRed = new NumberSetting("Text Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textGreen = new NumberSetting("Text Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textBlue = new NumberSetting("Text Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting glowHue = new NumberSetting("Glow Hue", 0.85, (double)0.0F, (double)1.0F, 0.01);
   private final NumberSetting glowSize = new NumberSetting("Glow Size", (double)8.0F, (double)0.0F, (double)32.0F, (double)1.0F);
   private final NumberSetting glowOpacity = new NumberSetting("Glow Opacity", (double)180.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineRed = new NumberSetting("Outline Red", (double)139.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineGreen = new NumberSetting("Outline Green", (double)92.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineBlue = new NumberSetting("Outline Blue", (double)246.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineDeepness = new NumberSetting("Outline Deepness", (double)200.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final Map<String, Integer> trackedMaxDurations = new HashMap();

   public EffectTimersModule() {
      super("Effects", Category.HUD, ICON);
      this.x = 20;
      this.y = 320;
      this.width = 140;
      this.height = 40;
      this.addSetting(this.displayMode);
      this.addSetting(this.showTitle);
      this.addSetting(this.showDuration);
      this.addSetting(this.showAmplifier);
      this.addSetting(this.showProgressBar);
      this.addSetting(this.showBackground);
      this.addSetting(this.showGlow);
      this.addSetting(this.showOutline);
      this.addSetting(this.useEffectColor);
      this.addSetting(this.rainbowText);
      this.addSetting(this.rainbowBars);
      this.addSetting(this.compactSpacing);
      this.addSetting(this.sortLongestFirst);
      this.addSetting(this.iconLetters);
      this.addSetting(this.darkTheme);
      this.addSetting(this.ultraDarkTheme);
      this.addSetting(this.darkThemeOpacity);
      this.addSetting(this.ultraDarkOpacity);
      this.addSetting(this.padding);
      this.addSetting(this.rowHeight);
      this.addSetting(this.iconSize);
      this.addSetting(this.maxVisibleEffects);
      this.addSetting(this.backgroundOpacity);
      this.addSetting(this.backgroundRed);
      this.addSetting(this.backgroundGreen);
      this.addSetting(this.backgroundBlue);
      this.addSetting(this.textRed);
      this.addSetting(this.textGreen);
      this.addSetting(this.textBlue);
      this.addSetting(this.glowHue);
      this.addSetting(this.glowSize);
      this.addSetting(this.glowOpacity);
      this.addSetting(this.outlineRed);
      this.addSetting(this.outlineGreen);
      this.addSetting(this.outlineBlue);
      this.addSetting(this.outlineDeepness);
   }

   public void onEnable() {
      this.refreshSize();
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         List<class_1293> effects = new ArrayList(mc.field_1724.method_6026());
         this.sortEffects(effects);
         Set<String> activeKeys = new HashSet();

         for(class_1293 effect : effects) {
            String key = this.effectKey(effect);
            activeKeys.add(key);
            this.trackedMaxDurations.merge(key, effect.method_5584(), Math::max);
         }

         this.trackedMaxDurations.keySet().retainAll(activeKeys);
         int visibleCount = Math.min(effects.size(), this.getMaxEffects());
         int mode = this.getDisplayMode();
         int icon = Math.max(12, (int)this.iconSize.getValue());
         int rowH = Math.max((int)this.rowHeight.getValue(), icon + 6);
         if (mode == 2) {
            rowH = Math.max(22, rowH - 4);
         }

         int rowGap = this.compactSpacing.getValue() ? 4 : 6;
         int var10000;
         if (this.showTitle.getValue()) {
            Objects.requireNonNull(mc.field_1772);
            var10000 = 9 + 6;
         } else {
            var10000 = 0;
         }

         int titleH = var10000;
         int totalWidth = this.computeWidth(effects, visibleCount, (int)this.padding.getValue(), icon, mode);
         int totalHeight = this.computeHeight(visibleCount, rowH, rowGap, titleH);
         this.width = totalWidth;
         this.height = totalHeight;
         int bgColor = applyAlpha(rgbColor((int)this.backgroundRed.getValue(), (int)this.backgroundGreen.getValue(), (int)this.backgroundBlue.getValue()), (int)this.backgroundOpacity.getValue());
         int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineDeepness.getValue());
         if (this.showBackground.getValue() && this.isBox()) {
            RoundedRectRenderer.fill(context, 0.0F, 0.0F, (float)totalWidth, (float)totalHeight, 6.0F, bgColor);
         }

         if (this.showGlow.getValue() && this.isGlow()) {
            int g = Math.max(0, (int)this.glowSize.getValue());
            RoundedRectRenderer.glow(context, (float)(-g), (float)(-g), (float)(totalWidth + g * 2), (float)(totalHeight + g * 2), 6.0F + (float)g * 0.25F, applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), this.getRenderColor()), (int)this.glowOpacity.getValue()));
         }

         if (this.showOutline.getValue() && this.isOutline()) {
            RoundedRectRenderer.outline(context, 0.0F, 0.0F, (float)totalWidth, (float)totalHeight, 6.0F, (float)Math.max(1, this.getOutlineThickness()), outlineColor, bgColor);
         }

         int startY = 0;
         if (this.showTitle.getValue()) {
            String title = "Effects";
            int titleColor = this.rainbowText.getValue() ? this.rainbowColor() : -1;
            context.method_51439(mc.field_1772, class_2561.method_43470(title), (int)this.padding.getValue(), 2, titleColor, false);
            startY = titleH;
         }

         int yOffset = startY;

         for(int i = 0; i < visibleCount; ++i) {
            this.renderRow(context, (class_1293)effects.get(i), 0, yOffset, mode);
            yOffset += rowH + rowGap;
         }

      }
   }

   private void renderRow(class_332 context, class_1293 effect, int x, int y, int mode) {
      int rowH = Math.max((int)this.rowHeight.getValue(), Math.max(12, (int)this.iconSize.getValue()) + 6);
      if (mode == 2) {
         rowH = Math.max(22, rowH - 4);
      }

      int panelW = this.computePanelWidth(effect, mode);
      int rowW = 40 + panelW - 1;
      int panelX = x + 40 - 1;
      ThemePalette palette = this.resolvePalette();
      int badgeBg = palette.badgeBg;
      int tileOuter = palette.tileOuter;
      int tileInner = palette.tileInner;
      int panelOuter = palette.panelOuter;
      int panelInner = palette.panelInner;
      RoundedRectRenderer.fill(context, (float)x, (float)y, 40.0F, (float)rowH, 4.0F, tileOuter);
      RoundedRectRenderer.fill(context, (float)(x + 2), (float)(y + 2), 36.0F, (float)(rowH - 4), 3.0F, tileInner);
      context.method_25294(x + 3, y + 3, x + 40 - 3, y + 4, 1157627903);
      context.method_25294(x + 3, y + rowH - 4, x + 40 - 3, y + rowH - 3, 855638016);
      context.method_25294(x + 40 - 1, y + 4, x + 40, y + rowH - 4, -14800845);
      RoundedRectRenderer.fill(context, (float)panelX, (float)y, (float)panelW, (float)rowH, 4.0F, panelOuter);
      RoundedRectRenderer.fill(context, (float)(panelX + 2), (float)(y + 2), (float)(panelW - 4), (float)(rowH - 4), 3.0F, panelInner);
      context.method_25294(panelX + 3, y + 3, panelX + panelW - 3, y + 4, 872415231);
      context.method_25294(panelX + 3, y + rowH - 4, panelX + panelW - 3, y + rowH - 3, 570425344);
      if (this.showOutline.getValue() && this.isOutline()) {
         int bgColor = -16777216;
         RoundedRectRenderer.outline(context, (float)x, (float)y, 40.0F, (float)rowH, 4.0F, (float)Math.max(1, this.getOutlineThickness()), -14932944, bgColor);
         RoundedRectRenderer.outline(context, (float)panelX, (float)y, (float)panelW, (float)rowH, 4.0F, (float)Math.max(1, this.getOutlineThickness()), -14932944, bgColor);
      }

      if (this.showGlow.getValue() && this.isGlow()) {
         int g = Math.max(0, (int)this.glowSize.getValue());
         int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), this.useEffectColor.getValue() ? this.resolveEffectColor(effect) : -8481366), (int)this.glowOpacity.getValue());
         RoundedRectRenderer.glow(context, (float)(x - g), (float)(y - g), (float)(rowW + g * 2), (float)(rowH + g * 2), 5.0F, glowColor);
      }

      this.drawEffectIcon(context, effect, x, y, 40, rowH, badgeBg);
      if (mode == 1) {
         this.drawNameMode(context, effect, panelX, y, panelW, rowH);
      } else {
         this.drawCompactMode(context, effect, panelX, y, panelW, rowH);
      }

      if (this.showProgressBar.getValue()) {
         this.drawProgressBar(context, effect, panelX + 4, y + rowH - 4, panelW - 8, 2, this.resolveEffectColor(effect));
      }

   }

   private void drawEffectIcon(class_332 context, class_1293 effect, int x, int y, int w, int h, int badgeBg) {
      class_2960 iconId = this.getEffectIconId(effect);
      int inner = Math.max(18, Math.min(w, h) - 16);
      int ix = x + (w - inner) / 2;
      int iy = y + (h - inner) / 2;

      try {
         context.method_70845(iconId, ix, iy, ix + inner, iy + inner, 0.0F, 1.0F, 0.0F, 1.0F);
      } catch (Throwable var18) {
         context.method_25294(ix, iy, ix + inner, iy + inner, -8550763);
      }

      if (this.iconLetters.getValue()) {
         String letter = this.getEffectLetter(effect);
         int letterW = mc.field_1772.method_1727(letter);
         int letterX = x + Math.max(0, (w - letterW) / 2);
         Objects.requireNonNull(mc.field_1772);
         int letterY = y + Math.max(0, (h - 9) / 2) - 1;
         context.method_51439(mc.field_1772, class_2561.method_43470(letter), letterX, letterY, -1, false);
      }

      if (this.showAmplifier.getValue()) {
         int amp = effect.method_5578() + 1;
         String badge = String.valueOf(amp);
         int badgeW = Math.max(14, mc.field_1772.method_1727(badge) + 4);
         int badgeH = 14;
         int bx = x + w - badgeW + 1;
         int by = y - 1;
         RoundedRectRenderer.fill(context, (float)bx, (float)by, (float)badgeW, (float)badgeH, 2.0F, badgeBg);
         context.method_51439(mc.field_1772, class_2561.method_43470(badge), bx + (badgeW - mc.field_1772.method_1727(badge)) / 2, by + 2, -1, false);
      }

   }

   private void drawNameMode(class_332 context, class_1293 effect, int panelX, int panelY, int panelW, int rowH) {
      String name = this.getEffectName(effect);
      String duration = this.formatDuration(effect);
      int nameW = mc.field_1772.method_1727(name);
      int durationW = mc.field_1772.method_1727(duration);
      int nameX = panelX + Math.max(0, (panelW - nameW) / 2);
      int durationX = panelX + Math.max(0, (panelW - durationW) / 2);
      int nameY = panelY + 4;
      int var10000 = panelY + rowH;
      Objects.requireNonNull(mc.field_1772);
      int durationY = var10000 - 9 - 4;
      context.method_51439(mc.field_1772, class_2561.method_43470(name), nameX, nameY, this.resolveTextColor(), false);
      if (this.showDuration.getValue()) {
         context.method_51439(mc.field_1772, class_2561.method_43470(duration), durationX, durationY, this.useEffectColor.getValue() ? this.resolveEffectColor(effect) : this.resolveTextColor(), false);
      }

   }

   private void drawCompactMode(class_332 context, class_1293 effect, int panelX, int panelY, int panelW, int rowH) {
      if (this.showDuration.getValue()) {
         String duration = this.formatDuration(effect);
         int durationW = mc.field_1772.method_1727(duration);
         int tx = panelX + Math.max(0, (panelW - durationW) / 2);
         Objects.requireNonNull(mc.field_1772);
         int ty = panelY + (rowH - 9) / 2 - 1;
         context.method_51439(mc.field_1772, class_2561.method_43470(duration), tx, ty, this.resolveTextColor(), false);
      }
   }

   private void drawProgressBar(class_332 context, class_1293 effect, int x, int y, int w, int h, int accentColor) {
      if (w > 0 && h > 0) {
         String key = this.effectKey(effect);
         int maxDuration = Math.max((Integer)this.trackedMaxDurations.getOrDefault(key, effect.method_5584()), 1);
         float progress = Math.max(0.0F, Math.min(1.0F, (float)effect.method_5584() / (float)maxDuration));
         int barColor = this.rainbowBars.getValue() ? this.rainbowColor() : accentColor;
         context.method_25294(x, y, x + w, y + h, 1426063360);
         context.method_25294(x, y, x + Math.round((float)w * progress), y + h, barColor);
      }
   }

   private int computeWidth(List<class_1293> effects, int visibleCount, int pad, int icon, int mode) {
      int maxWidth = 0;

      for(int i = 0; i < visibleCount; ++i) {
         class_1293 effect = (class_1293)effects.get(i);
         maxWidth = Math.max(maxWidth, this.computeRowWidth(effect, icon, mode));
      }

      if (this.showTitle.getValue()) {
         maxWidth = Math.max(maxWidth, mc.field_1772.method_1727("Effects") + pad * 2);
      }

      return Math.max(120, maxWidth);
   }

   private int computeRowWidth(class_1293 effect, int icon, int mode) {
      int panelW = this.computePanelWidth(effect, mode);
      return 40 + panelW - 1;
   }

   private int computePanelWidth(class_1293 effect, int mode) {
      int durationW = this.showDuration.getValue() ? mc.field_1772.method_1727(this.formatDuration(effect)) : 0;
      int nameW = mc.field_1772.method_1727(this.getEffectName(effect));
      if (mode == 1) {
         return Math.max(96, Math.max(nameW, durationW) + 24);
      } else {
         return mode == 2 ? Math.max(84, durationW + 22) : Math.max(88, durationW + 26);
      }
   }

   private int computeHeight(int visibleCount, int rowH, int rowGap, int titleH) {
      return visibleCount <= 0 ? Math.max(40, titleH + rowH) : titleH + visibleCount * rowH + (visibleCount - 1) * rowGap;
   }

   private void refreshSize() {
      if (mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         List<class_1293> effects = new ArrayList(mc.field_1724.method_6026());
         int visibleCount = Math.min(effects.size(), this.getMaxEffects());
         int icon = Math.max(12, (int)this.iconSize.getValue());
         int mode = this.getDisplayMode();
         int rowH = Math.max((int)this.rowHeight.getValue(), icon + 6);
         if (mode == 2) {
            rowH = Math.max(22, rowH - 4);
         }

         int rowGap = this.compactSpacing.getValue() ? 4 : 6;
         int var10000;
         if (this.showTitle.getValue()) {
            Objects.requireNonNull(mc.field_1772);
            var10000 = 9 + 6;
         } else {
            var10000 = 0;
         }

         int titleH = var10000;
         this.width = this.computeWidth(effects, visibleCount, (int)this.padding.getValue(), icon, mode);
         this.height = this.computeHeight(visibleCount, rowH, rowGap, titleH);
      } else {
         this.width = 140;
         this.height = 40;
      }
   }

   private void sortEffects(List<class_1293> effects) {
      if (this.sortLongestFirst.getValue()) {
         effects.sort(Comparator.comparingInt(class_1293::method_5584).reversed());
      } else {
         effects.sort(Comparator.comparing(this::getEffectName, String.CASE_INSENSITIVE_ORDER));
      }

   }

   private int getDisplayMode() {
      return Math.max(0, Math.min(2, (int)this.displayMode.getValue()));
   }

   private int getMaxEffects() {
      return Math.max(1, (int)this.maxVisibleEffects.getValue());
   }

   private String getEffectName(class_1293 effect) {
      return class_2561.method_43471(effect.method_5586()).getString();
   }

   private String getEffectLetter(class_1293 effect) {
      String name = this.getEffectName(effect).trim();
      return name.isEmpty() ? "?" : String.valueOf(Character.toUpperCase(name.charAt(0)));
   }

   private String formatDuration(class_1293 effect) {
      int ticks = Math.max(0, effect.method_5584());
      if (ticks >= 1000000) {
         return "∞";
      } else {
         int seconds = ticks / 20;
         int minutes = seconds / 60;
         int remaining = seconds % 60;
         return String.format("%02d:%02d", minutes, remaining);
      }
   }

   private String effectKey(class_1293 effect) {
      String var10000 = effect.method_5586();
      return var10000 + ":" + effect.method_5578();
   }

   private class_2960 getEffectIconId(class_1293 effect) {
      try {
         class_1291 statusEffect = (class_1291)effect.method_5579().comp_349();
         class_2960 id = class_7923.field_41174.method_10221(statusEffect);
         return id == null ? class_2960.method_60655("minecraft", "textures/mob_effect/speed.png") : class_2960.method_60655(id.method_12836(), "textures/mob_effect/" + id.method_12832() + ".png");
      } catch (Throwable var4) {
         return class_2960.method_60655("minecraft", "textures/mob_effect/speed.png");
      }
   }

   private int resolveEffectColor(class_1293 effect) {
      try {
         class_1291 statusEffect = (class_1291)effect.method_5579().comp_349();
         return -16777216 | statusEffect.method_5556() & 16777215;
      } catch (Throwable var3) {
         return -8481366;
      }
   }

   private int resolveTextColor() {
      if (this.rainbowText.getValue()) {
         return this.rainbowColor();
      } else {
         return this.isRGB() ? this.getHudColor() : rgbColor((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue());
      }
   }

   private ThemePalette resolvePalette() {
      if (!this.darkTheme.getValue()) {
         return new ThemePalette(-13944504, -11509389, -11114115, -8679776, -16119286);
      } else if (this.ultraDarkTheme.getValue()) {
         int a = clamp255((int)this.ultraDarkOpacity.getValue());
         return new ThemePalette(applyAlpha(-15262686, a), applyAlpha(-14670290, a), applyAlpha(-14472653, a), applyAlpha(-13880515, a), applyAlpha(-16514044, a));
      } else {
         int a = clamp255((int)this.darkThemeOpacity.getValue());
         return new ThemePalette(applyAlpha(-14669773, a), applyAlpha(-14011071, a), applyAlpha(-13616055, a), applyAlpha(-12958124, a), applyAlpha(-16448251, a));
      }
   }

   private int rainbowColor() {
      float time = (float)(System.currentTimeMillis() % 6000L) / 6000.0F;
      return Color.HSBtoRGB(time, 0.85F, 1.0F) | -16777216;
   }

   private int hueGlowColor(float hue, int baseTextColor) {
      hue = Math.max(0.0F, Math.min(1.0F, hue));
      float[] hsb = Color.RGBtoHSB(baseTextColor >> 16 & 255, baseTextColor >> 8 & 255, baseTextColor & 255, (float[])null);
      int rgb = Color.HSBtoRGB(hue, Math.max(0.45F, hsb[1]), 1.0F);
      return rgb & 16777215;
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

   private static final class ThemePalette {
      final int tileOuter;
      final int tileInner;
      final int panelOuter;
      final int panelInner;
      final int badgeBg;

      private ThemePalette(int tileOuter, int tileInner, int panelOuter, int panelInner, int badgeBg) {
         this.tileOuter = tileOuter;
         this.tileInner = tileInner;
         this.panelOuter = panelOuter;
         this.panelInner = panelInner;
         this.badgeBg = badgeBg;
      }
   }
}
