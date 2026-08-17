package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.HudModule;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class HealthModule extends HudModule {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/health.png");
   private static final int DEFAULT_BG_R = 16;
   private static final int DEFAULT_BG_G = 19;
   private static final int DEFAULT_BG_B = 29;
   private final NumberSetting spacing = new NumberSetting("Padding", (double)4.0F, (double)2.0F, (double)20.0F, (double)1.0F);
   private final BooleanSetting showText = new BooleanSetting("Health Text", true);
   private final BooleanSetting roundCorners = new BooleanSetting("Round Corners", true);
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

   public HealthModule() {
      super("Health", Category.HUD, ICON);
      this.x = 20;
      this.y = 224;
      this.width = 48;
      this.height = 18;
      this.addSetting(this.spacing);
      this.addSetting(this.showText);
      this.addSetting(this.roundCorners);
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
      this.safeRefreshSize();
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         float hp = mc.field_1724.method_6032();
         float max = Math.max(1.0F, mc.field_1724.method_6063());
         int pct = (int)(hp / max * 100.0F);
         String topLine = "HP";
         String bottomLine = String.valueOf((int)hp);
         String text = this.isVertical() ? topLine + "\n" + bottomLine : topLine + " " + bottomLine;
         this.safeRefreshSize();
         int pad = (int)this.spacing.getValue();
         int scalePad = Math.max(1, Math.round((float)pad * this.getScale()));
         int baseTextColor = this.resolveTextColor();
         int color = pct <= 25 ? -43691 : baseTextColor;
         int backgroundColor = applyAlpha(rgbColor((int)this.backgroundRed.getValue(), (int)this.backgroundGreen.getValue(), (int)this.backgroundBlue.getValue()), (int)this.backgroundOpacity.getValue());
         int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineDeepness.getValue());
         int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), color), (int)this.glowOpacity.getValue());
         float radius = this.roundCorners.getValue() ? Math.max(0.0F, (float)this.getRadius()) : 0.0F;
         int outlineThickness = Math.max(1, this.getOutlineThickness());
         if (this.isGlow()) {
            int g = Math.max(0, (int)this.glowSize.getValue());
            RoundedRectRenderer.glow(context, (float)(-g), (float)(-g), (float)(this.width + g * 2), (float)(this.height + g * 2), radius + (float)g * 0.35F, glowColor);
         }

         if (this.isBox()) {
            RoundedRectRenderer.fill(context, 0.0F, 0.0F, (float)this.width, (float)this.height, radius, backgroundColor);
         }

         if (this.isOutline()) {
            RoundedRectRenderer.outline(context, 0.0F, 0.0F, (float)this.width, (float)this.height, radius, (float)outlineThickness, outlineColor, backgroundColor);
         }

         if (this.showText.getValue()) {
            if (this.isVertical()) {
               int topW = mc.field_1772.method_1727(topLine);
               int bottomW = mc.field_1772.method_1727(bottomLine);
               int topX = (this.width - topW) / 2;
               int bottomX = (this.width - bottomW) / 2;
               Objects.requireNonNull(mc.field_1772);
               int bottomY = scalePad + 9 + 1;
               context.method_51439(mc.field_1772, class_2561.method_43470(topLine), topX, scalePad, color, false);
               context.method_51439(mc.field_1772, class_2561.method_43470(bottomLine), bottomX, bottomY, color, false);
            } else {
               context.method_51439(mc.field_1772, class_2561.method_43470(text), scalePad, scalePad, color, false);
            }

         }
      }
   }

   private void safeRefreshSize() {
      if (mc != null && mc.field_1772 != null && mc.field_1724 != null) {
         boolean vertical = this.isVertical();
         boolean drawText = this.showText.getValue();
         String topLine = "HP";
         String bottomLine = String.valueOf((int)mc.field_1724.method_6032());
         String text = vertical ? topLine + "\n" + bottomLine : topLine + " " + bottomLine;
         int pad = (int)this.spacing.getValue();
         int scalePad = Math.max(1, Math.round((float)pad * this.getScale()));
         int textW;
         int textH;
         if (!drawText) {
            textW = 0;
            textH = 0;
         } else if (vertical) {
            int w1 = mc.field_1772.method_1727(topLine);
            int w2 = mc.field_1772.method_1727(bottomLine);
            textW = Math.max(w1, w2);
            Objects.requireNonNull(mc.field_1772);
            textH = 9 * 2 + 1;
         } else {
            textW = mc.field_1772.method_1727(text);
            Objects.requireNonNull(mc.field_1772);
            textH = 9;
         }

         this.width = Math.max(18, textW + scalePad * 2);
         this.height = Math.max(18, textH + scalePad * 2);
      } else {
         this.width = 48;
         this.height = 18;
      }
   }

   private int resolveTextColor() {
      if (this.isRainbow()) {
         return this.rainbowColor();
      } else {
         return this.isRGB() ? this.getHudColor() : rgbColor((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue());
      }
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
}
