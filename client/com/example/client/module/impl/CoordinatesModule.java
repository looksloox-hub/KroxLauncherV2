package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.HudModule;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public class CoordinatesModule extends HudModule {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/map.png");
   private static final int DEFAULT_BG_R = 16;
   private static final int DEFAULT_BG_G = 19;
   private static final int DEFAULT_BG_B = 29;
   private final BooleanSetting verticalFormat = new BooleanSetting("Vertical Format", true);
   private final NumberSetting spacing = new NumberSetting("Padding", (double)4.0F, (double)2.0F, (double)20.0F, (double)1.0F);
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

   public CoordinatesModule() {
      super("Coords", Category.HUD, ICON);
      this.x = 20;
      this.y = 50;
      this.width = 48;
      this.height = 18;
      this.addSetting(this.verticalFormat);
      this.addSetting(this.spacing);
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
         int px = mc.field_1724.method_31477();
         int py = mc.field_1724.method_31478();
         int pz = mc.field_1724.method_31479();
         this.safeRefreshSize();
         boolean vertical = this.verticalFormat.getValue();
         List<String> lines = this.buildLines(px, py, pz, vertical);
         int pad = (int)this.spacing.getValue();
         int scalePad = Math.max(1, Math.round((float)pad * this.getScale()));
         int textColor = this.resolveTextColor();
         int backgroundColor = applyAlpha(rgbColor((int)this.backgroundRed.getValue(), (int)this.backgroundGreen.getValue(), (int)this.backgroundBlue.getValue()), (int)this.backgroundOpacity.getValue());
         int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineDeepness.getValue());
         int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), textColor), (int)this.glowOpacity.getValue());
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

         if (vertical) {
            int y = scalePad;

            for(String line : lines) {
               context.method_51439(mc.field_1772, class_2561.method_43470(line), scalePad, y, textColor, false);
               Objects.requireNonNull(mc.field_1772);
               y += 9 + 1;
            }
         } else {
            context.method_51439(mc.field_1772, class_2561.method_43470((String)lines.getFirst()), scalePad, scalePad, textColor, false);
         }

      }
   }

   private void safeRefreshSize() {
      if (mc != null && mc.field_1772 != null && mc.field_1724 != null) {
         int px = mc.field_1724.method_31477();
         int py = mc.field_1724.method_31478();
         int pz = mc.field_1724.method_31479();
         boolean vertical = this.verticalFormat.getValue();
         List<String> lines = this.buildLines(px, py, pz, vertical);
         int pad = (int)this.spacing.getValue();
         int scalePad = Math.max(1, Math.round((float)pad * this.getScale()));
         if (lines.isEmpty()) {
            this.width = 18;
            this.height = 18;
         } else {
            int textW = 0;
            int textH;
            if (vertical) {
               for(String line : lines) {
                  textW = Math.max(textW, mc.field_1772.method_1727(line));
               }

               Objects.requireNonNull(mc.field_1772);
               textH = 9 * lines.size() + Math.max(0, lines.size() - 1);
            } else {
               textW = mc.field_1772.method_1727((String)lines.getFirst());
               Objects.requireNonNull(mc.field_1772);
               textH = 9;
            }

            this.width = Math.max(18, textW + scalePad * 2);
            this.height = Math.max(18, textH + scalePad * 2);
         }
      } else {
         this.width = 48;
         this.height = 18;
      }
   }

   private List<String> buildLines(int x, int y, int z, boolean vertical) {
      List<String> lines = new ArrayList(3);
      String xLine = "X: " + x + " " + this.axisSuffix(x);
      String yLine = "Y: " + y;
      String zLine = "Z: " + z + " " + this.axisSuffix(z);
      if (vertical) {
         lines.add(xLine);
         lines.add(yLine);
         lines.add(zLine);
      } else {
         lines.add(xLine + "   " + yLine + "   " + zLine);
      }

      return lines;
   }

   private String axisSuffix(int value) {
      return value >= 0 ? "(+)" : "(-)";
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
