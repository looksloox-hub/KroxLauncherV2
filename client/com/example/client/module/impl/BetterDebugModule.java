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
import net.minecraft.class_1959;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_6880;

public class BetterDebugModule extends HudModule {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/bug.png");
   private static final int DEFAULT_BG_R = 16;
   private static final int DEFAULT_BG_G = 19;
   private static final int DEFAULT_BG_B = 29;
   private static final int MIN_W = 18;
   private static final int MIN_H = 18;
   private static final int ROW_GAP = 1;
   private static final int COLUMN_GAP = 8;
   private final NumberSetting spacing = new NumberSetting("Padding", (double)4.0F, (double)2.0F, (double)20.0F, (double)1.0F);
   private final BooleanSetting showXYZ = new BooleanSetting("Show XYZ", true);
   private final BooleanSetting showDirection = new BooleanSetting("Show Direction", true);
   private final BooleanSetting showBiome = new BooleanSetting("Show Biome", true);
   private final BooleanSetting showFPS = new BooleanSetting("Show FPS", true);
   private final BooleanSetting roundCorners = new BooleanSetting("Round Corners", true);
   private final NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", (double)220.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundRed = new NumberSetting("Background Red", (double)16.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundGreen = new NumberSetting("Background Green", (double)19.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundBlue = new NumberSetting("Background Blue", (double)29.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textRed = new NumberSetting("Text Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textGreen = new NumberSetting("Text Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textBlue = new NumberSetting("Text Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting biomeRed = new NumberSetting("Biome Red", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting biomeGreen = new NumberSetting("Biome Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting biomeBlue = new NumberSetting("Biome Blue", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting glowHue = new NumberSetting("Glow Hue", 0.85, (double)0.0F, (double)1.0F, 0.01);
   private final NumberSetting glowSize = new NumberSetting("Glow Size", (double)8.0F, (double)0.0F, (double)32.0F, (double)1.0F);
   private final NumberSetting glowOpacity = new NumberSetting("Glow Opacity", (double)180.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineRed = new NumberSetting("Outline Red", (double)139.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineGreen = new NumberSetting("Outline Green", (double)92.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineBlue = new NumberSetting("Outline Blue", (double)246.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting outlineDeepness = new NumberSetting("Outline Deepness", (double)200.0F, (double)0.0F, (double)255.0F, (double)1.0F);

   public BetterDebugModule() {
      super("BetterDebug", Category.HUD, ICON);
      this.x = 220;
      this.y = 20;
      this.width = 80;
      this.height = 20;
      this.addSetting(this.spacing);
      this.addSetting(this.showXYZ);
      this.addSetting(this.showDirection);
      this.addSetting(this.showBiome);
      this.addSetting(this.showFPS);
      this.addSetting(this.roundCorners);
      this.addSetting(this.backgroundOpacity);
      this.addSetting(this.backgroundRed);
      this.addSetting(this.backgroundGreen);
      this.addSetting(this.backgroundBlue);
      this.addSetting(this.textRed);
      this.addSetting(this.textGreen);
      this.addSetting(this.textBlue);
      this.addSetting(this.biomeRed);
      this.addSetting(this.biomeGreen);
      this.addSetting(this.biomeBlue);
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
         DebugLayout layout = this.buildLayout();
         this.width = layout.width;
         this.height = layout.height;
         int textColor = this.resolveTextColor();
         int biomeColor = this.resolveBiomeColor();
         int backgroundColor = applyAlpha(rgbColor((int)this.backgroundRed.getValue(), (int)this.backgroundGreen.getValue(), (int)this.backgroundBlue.getValue()), (int)this.backgroundOpacity.getValue());
         int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineDeepness.getValue());
         int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), textColor), (int)this.glowOpacity.getValue());
         float radius = this.roundCorners.getValue() ? Math.max(0.0F, (float)this.getRadius()) : 0.0F;
         int glowSpread = Math.max(0, (int)this.glowSize.getValue());
         int outlineThickness = Math.max(1, this.getOutlineThickness());
         if (this.isGlow()) {
            RoundedRectRenderer.glow(context, (float)(-glowSpread), (float)(-glowSpread), (float)(layout.width + glowSpread * 2), (float)(layout.height + glowSpread * 2), radius + (float)glowSpread * 0.35F, glowColor);
         }

         if (this.isBox()) {
            RoundedRectRenderer.fill(context, 0.0F, 0.0F, (float)layout.width, (float)layout.height, radius, backgroundColor);
         }

         if (this.isOutline()) {
            RoundedRectRenderer.outline(context, 0.0F, 0.0F, (float)layout.width, (float)layout.height, radius, (float)outlineThickness, outlineColor, backgroundColor);
         }

         Objects.requireNonNull(mc.field_1772);
         int fontH = 9;
         int pad = layout.pad;
         int rowY = pad;

         for(DebugRow row : layout.rows) {
            if (row.leftText == null) {
               boolean var10000 = false;
            } else {
               mc.field_1772.method_1727(row.leftText);
            }

            int rightW = row.rightText == null ? 0 : mc.field_1772.method_1727(row.rightText);
            if (row.leftText != null && !row.leftText.isEmpty()) {
               context.method_51439(mc.field_1772, class_2561.method_43470(row.leftText), pad, rowY, row.leftColor, false);
            }

            if (row.rightText != null && !row.rightText.isEmpty()) {
               int rightX = layout.width - pad - rightW;
               context.method_51439(mc.field_1772, class_2561.method_43470(row.rightText), rightX, rowY, row.rightColor, false);
            }

            rowY += fontH + 1;
         }

      }
   }

   private void safeRefreshSize() {
      DebugLayout layout = this.buildLayout();
      this.width = layout.width;
      this.height = layout.height;
   }

   private DebugLayout buildLayout() {
      if (mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         int pad = (int)this.spacing.getValue();
         Objects.requireNonNull(mc.field_1772);
         int fontH = 9;
         List<DebugRow> rows = new ArrayList();
         if (this.showXYZ.getValue()) {
            int var10000 = (int)mc.field_1724.method_23317();
            String xLine = "X: " + var10000 + " " + axisTagX(mc.field_1724.method_23317());
            String yLine = "Y: " + (int)mc.field_1724.method_23318();
            var10000 = (int)mc.field_1724.method_23321();
            String zLine = "Z: " + var10000 + " " + axisTagZ(mc.field_1724.method_23321());
            String direction = this.showDirection.getValue() ? "- " + this.getCompassDirection() + " -" : null;
            rows.add(new DebugRow(xLine, direction, this.resolveTextColor(), this.resolveTextColor()));
            rows.add(new DebugRow(yLine, (String)null, this.resolveTextColor(), 0));
            rows.add(new DebugRow(zLine, (String)null, this.resolveTextColor(), 0));
         } else if (this.showDirection.getValue()) {
            rows.add(new DebugRow((String)null, "- " + this.getCompassDirection() + " -", 0, this.resolveTextColor()));
         }

         if (this.showBiome.getValue() || this.showFPS.getValue()) {
            String biome = this.showBiome.getValue() ? this.getBiomeName() : null;
            String fps = this.showFPS.getValue() ? mc.method_47599() + " FPS" : null;
            rows.add(new DebugRow(biome, fps, this.showBiome.getValue() ? this.resolveBiomeColor() : 0, this.showFPS.getValue() ? this.resolveTextColor() : 0));
         }

         if (rows.isEmpty()) {
            rows.add(new DebugRow("Debug", (String)null, this.resolveTextColor(), 0));
         }

         int maxRowW = 0;
         int totalH = rows.size() * fontH + Math.max(0, rows.size() - 1) * 1;

         for(DebugRow row : rows) {
            int leftW = row.leftText == null ? 0 : mc.field_1772.method_1727(row.leftText);
            int rightW = row.rightText == null ? 0 : mc.field_1772.method_1727(row.rightText);
            int rowW;
            if (leftW > 0 && rightW > 0) {
               rowW = leftW + 8 + rightW;
            } else {
               rowW = Math.max(leftW, rightW);
            }

            maxRowW = Math.max(maxRowW, rowW);
         }

         int width = Math.max(18, maxRowW + pad * 2);
         int height = Math.max(18, totalH + pad * 2);
         return new DebugLayout(width, height, pad, rows);
      } else {
         return new DebugLayout(80, 20, 4, new ArrayList());
      }
   }

   private String getBiomeName() {
      try {
         if (mc != null && mc.field_1687 != null && mc.field_1724 != null) {
            class_6880<class_1959> entry = mc.field_1687.method_23753(mc.field_1724.method_24515());
            return (String)entry.method_40230().map((key) -> prettifyBiomeName(key.method_29177().method_12832())).orElse("Plains");
         } else {
            return "Plains";
         }
      } catch (Throwable var2) {
         return "Plains";
      }
   }

   private static String prettifyBiomeName(String raw) {
      if (raw != null && !raw.isEmpty()) {
         String[] parts = raw.split("_");
         StringBuilder out = new StringBuilder();

         for(String part : parts) {
            if (!part.isEmpty()) {
               if (out.length() > 0) {
                  out.append(' ');
               }

               out.append(Character.toUpperCase(part.charAt(0)));
               if (part.length() > 1) {
                  out.append(part.substring(1));
               }
            }
         }

         return out.toString();
      } else {
         return "Plains";
      }
   }

   private static String axisTagX(double x) {
      return x >= (double)0.0F ? "(+)" : "(-)";
   }

   private static String axisTagZ(double z) {
      return z >= (double)0.0F ? "(-)" : "(+)";
   }

   private String getCompassDirection() {
      if (mc != null && mc.field_1724 != null) {
         float yaw = mc.field_1724.method_36454();
         int index = (int)Math.floor((double)((yaw + 22.5F) / 45.0F)) & 7;
         String var10000;
         switch (index) {
            case 0 -> var10000 = "S";
            case 1 -> var10000 = "SW";
            case 2 -> var10000 = "W";
            case 3 -> var10000 = "NW";
            case 4 -> var10000 = "N";
            case 5 -> var10000 = "NE";
            case 6 -> var10000 = "E";
            default -> var10000 = "SE";
         }

         return var10000;
      } else {
         return "N";
      }
   }

   private int resolveTextColor() {
      if (this.isRainbow()) {
         return this.rainbowColor();
      } else {
         return this.isRGB() ? this.getHudColor() : rgbColor((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue());
      }
   }

   private int resolveBiomeColor() {
      if (this.isRainbow()) {
         return this.rainbowColor();
      } else {
         return this.isRGB() ? this.getHudColor() : rgbColor((int)this.biomeRed.getValue(), (int)this.biomeGreen.getValue(), (int)this.biomeBlue.getValue());
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

   private static final class DebugRow {
      private final String leftText;
      private final String rightText;
      private final int leftColor;
      private final int rightColor;

      private DebugRow(String leftText, String rightText, int leftColor, int rightColor) {
         this.leftText = leftText;
         this.rightText = rightText;
         this.leftColor = leftColor;
         this.rightColor = rightColor;
      }
   }

   private static final class DebugLayout {
      private final int width;
      private final int height;
      private final int pad;
      private final List<DebugRow> rows;

      private DebugLayout(int width, int height, int pad, List<DebugRow> rows) {
         this.width = width;
         this.height = height;
         this.pad = pad;
         this.rows = rows;
      }
   }
}
