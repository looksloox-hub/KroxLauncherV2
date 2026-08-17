package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3481;
import net.minecraft.class_3965;

public class BlockHighlightModule extends Module {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/block_highlight.png");
   private static final int DEFAULT_BG_R = 16;
   private static final int DEFAULT_BG_G = 19;
   private static final int DEFAULT_BG_B = 29;
   private final NumberSetting padding = new NumberSetting("Padding", (double)6.0F, (double)2.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting radius = new NumberSetting("Radius", (double)4.0F, (double)0.0F, (double)20.0F, (double)1.0F);
   private final BooleanSetting showBlockName = new BooleanSetting("Block Name", true);
   private final BooleanSetting showTool = new BooleanSetting("Tool", true);
   private final BooleanSetting showMiningLevel = new BooleanSetting("Mining Level", true);
   private final BooleanSetting showHardness = new BooleanSetting("Hardness", true);
   private final BooleanSetting centerText = new BooleanSetting("Center Text", true);
   private final NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", (double)204.0F, (double)0.0F, (double)255.0F, (double)1.0F);
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
   private final NumberSetting outlineOpacity = new NumberSetting("Outline Opacity", (double)200.0F, (double)0.0F, (double)255.0F, (double)1.0F);

   public BlockHighlightModule() {
      super("BlockHighlight", Category.RENDER, ICON);
      this.x = 20;
      this.y = 220;
      this.width = 80;
      this.height = 20;
      this.addSetting(this.padding);
      this.addSetting(this.radius);
      this.addSetting(this.showBlockName);
      this.addSetting(this.showTool);
      this.addSetting(this.showMiningLevel);
      this.addSetting(this.showHardness);
      this.addSetting(this.centerText);
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
      this.addSetting(this.outlineOpacity);
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1687 != null && mc.field_1724 != null && mc.field_1772 != null) {
         class_239 hit = mc.field_1765;
         if (hit instanceof class_3965) {
            class_3965 bhr = (class_3965)hit;
            class_2338 pos = bhr.method_17777();
            class_2680 state = mc.field_1687.method_8320(pos);
            boolean unbreakable = this.isUnbreakable(state, pos);
            List<String> lines = this.buildLines(state, pos, unbreakable);
            if (!lines.isEmpty()) {
               this.refreshSize(lines);
               int textColor = rgbColor((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue());
               int backgroundColor = applyAlpha(rgbColor((int)this.backgroundRed.getValue(), (int)this.backgroundGreen.getValue(), (int)this.backgroundBlue.getValue()), (int)this.backgroundOpacity.getValue());
               int outlineColor = applyAlpha(rgbColor((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue()), (int)this.outlineOpacity.getValue());
               int glowColor = applyAlpha(this.hueGlowColor((float)this.glowHue.getValue(), textColor), (int)this.glowOpacity.getValue());
               float cornerRadius = (float)this.radius.getValue();
               int glow = Math.max(0, (int)this.glowSize.getValue());
               int pad = (int)this.padding.getValue();
               if (this.isGlow()) {
                  RoundedRectRenderer.glow(context, (float)(-glow), (float)(-glow), (float)(this.width + glow * 2), (float)(this.height + glow * 2), cornerRadius + (float)glow * 0.35F, glowColor);
               }

               if (this.isBox()) {
                  RoundedRectRenderer.fill(context, 0.0F, 0.0F, (float)this.width, (float)this.height, cornerRadius, backgroundColor);
               }

               if (this.isOutline()) {
                  RoundedRectRenderer.outline(context, 0.0F, 0.0F, (float)this.width, (float)this.height, cornerRadius, 1.0F, outlineColor, backgroundColor);
               }

               int y = pad;

               for(String line : lines) {
                  int lineW = mc.field_1772.method_1727(line);
                  int x = this.centerText.getValue() ? (this.width - lineW) / 2 : pad;
                  context.method_51439(mc.field_1772, class_2561.method_43470(line), x, y, textColor, false);
                  Objects.requireNonNull(mc.field_1772);
                  y += 9 + 1;
               }

            }
         }
      }
   }

   private List<String> buildLines(class_2680 state, class_2338 pos, boolean unbreakable) {
      List<String> lines = new ArrayList(4);
      if (this.showBlockName.getValue()) {
         lines.add(state.method_26204().method_9518().getString());
      }

      if (this.showTool.getValue()) {
         String var10001 = this.getToolText(state, unbreakable);
         lines.add("Tool: " + var10001);
      }

      if (this.showMiningLevel.getValue()) {
         String var5 = this.getMiningLevelText(state, unbreakable);
         lines.add("Mining Level: " + var5);
      }

      if (this.showHardness.getValue()) {
         lines.add(unbreakable ? "Unbreakable" : "Hardness: " + this.formatHardness(state, pos));
      }

      return lines;
   }

   private void refreshSize(List<String> lines) {
      if (mc != null && mc.field_1772 != null && !lines.isEmpty()) {
         int pad = (int)this.padding.getValue();
         int textW = 0;

         for(String line : lines) {
            textW = Math.max(textW, mc.field_1772.method_1727(line));
         }

         Objects.requireNonNull(mc.field_1772);
         int textH = 9 * lines.size() + Math.max(0, lines.size() - 1);
         this.width = Math.max(18, textW + pad * 2);
         this.height = Math.max(18, textH + pad * 2);
      } else {
         this.width = 80;
         this.height = 20;
      }
   }

   private boolean isUnbreakable(class_2680 state, class_2338 pos) {
      try {
         return state.method_26214(mc.field_1687, pos) < 0.0F;
      } catch (Throwable var4) {
         return false;
      }
   }

   private String getToolText(class_2680 state, boolean unbreakable) {
      if (unbreakable) {
         return "None";
      } else if (state.method_26164(class_3481.field_33713)) {
         return "Axe";
      } else if (state.method_26164(class_3481.field_33716)) {
         return "Shovel";
      } else if (state.method_26164(class_3481.field_33714)) {
         return "Hoe";
      } else {
         return state.method_26164(class_3481.field_33715) ? "Pickaxe" : "None";
      }
   }

   private String getMiningLevelText(class_2680 state, boolean unbreakable) {
      if (unbreakable) {
         return "None";
      } else if (state.method_26164(class_3481.field_33717)) {
         return "Diamond";
      } else if (state.method_26164(class_3481.field_33718)) {
         return "Iron";
      } else {
         return state.method_26164(class_3481.field_33719) ? "Stone" : "None";
      }
   }

   private String formatHardness(class_2680 state, class_2338 pos) {
      try {
         float hardness = state.method_26214(mc.field_1687, pos);
         if (hardness < 0.0F) {
            return "None";
         } else {
            return hardness == (float)((int)hardness) ? String.valueOf((int)hardness) : String.valueOf((float)Math.round(hardness * 10.0F) / 10.0F);
         }
      } catch (Throwable var4) {
         return "None";
      }
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
}
