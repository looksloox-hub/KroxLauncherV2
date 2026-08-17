package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.ui.render.RoundedRectRenderer;
import java.awt.Color;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.joml.Matrix3x2fStack;

public class KeystrokesModule extends Module {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/keyboard.png");
   private static final class_2561 TXT_W = class_2561.method_43470("W");
   private static final class_2561 TXT_A = class_2561.method_43470("A");
   private static final class_2561 TXT_S = class_2561.method_43470("S");
   private static final class_2561 TXT_D = class_2561.method_43470("D");
   private static final class_2561 TXT_LMB = class_2561.method_43470("LMB");
   private static final class_2561 TXT_RMB = class_2561.method_43470("RMB");
   private static final class_2561 TXT_SPACE = class_2561.method_43470("Space");
   private final BooleanSetting roundedCorners = new BooleanSetting("Rounded Corner", true);
   private final NumberSetting cornerRadius = new NumberSetting("Corner Radius", (double)5.0F, (double)0.0F, (double)12.0F, (double)1.0F);
   private final BooleanSetting darkTheme = new BooleanSetting("Dark Theme", true);
   private final BooleanSetting extraDarkTheme = new BooleanSetting("Extra Dark Theme", false);
   private final NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", (double)204.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundRed = new NumberSetting("Background Red", (double)18.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundGreen = new NumberSetting("Background Green", (double)20.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting backgroundBlue = new NumberSetting("Background Blue", (double)28.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting borderRed = new NumberSetting("Border Red", (double)55.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting borderGreen = new NumberSetting("Border Green", (double)65.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting borderBlue = new NumberSetting("Border Blue", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting borderOpacity = new NumberSetting("Border Opacity", (double)90.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting pressedRed = new NumberSetting("Pressed Red", (double)235.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting pressedGreen = new NumberSetting("Pressed Green", (double)235.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting pressedBlue = new NumberSetting("Pressed Blue", (double)245.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting pressedOpacity = new NumberSetting("Pressed Opacity", (double)214.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting accentRed = new NumberSetting("Accent Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting accentGreen = new NumberSetting("Accent Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting accentBlue = new NumberSetting("Accent Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textRed = new NumberSetting("Text Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textGreen = new NumberSetting("Text Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting textBlue = new NumberSetting("Text Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final BooleanSetting glowEnabled = new BooleanSetting("Glow Toggle", false);
   private final BooleanSetting shadowEnabled = new BooleanSetting("Shadow Toggle", true);
   private final BooleanSetting glassEnabled = new BooleanSetting("Glass Toggle", true);
   private final BooleanSetting pressedAnimation = new BooleanSetting("Pressed Animation", true);
   private final NumberSetting scale = new NumberSetting("Scale", (double)1.0F, (double)0.5F, (double)2.0F, 0.05);
   private final NumberSetting padding = new NumberSetting("Padding", (double)0.0F, (double)0.0F, (double)12.0F, (double)1.0F);
   private final NumberSetting gap = new NumberSetting("Gap", (double)2.0F, (double)0.0F, (double)10.0F, (double)1.0F);
   private final NumberSetting keySize = new NumberSetting("Key Size", (double)38.0F, (double)18.0F, (double)64.0F, (double)1.0F);
   private final NumberSetting mouseKeyWidth = new NumberSetting("Mouse Key Width", (double)58.0F, (double)24.0F, (double)96.0F, (double)1.0F);
   private final NumberSetting mouseKeyHeight = new NumberSetting("Mouse Key Height", (double)20.0F, (double)14.0F, (double)36.0F, (double)1.0F);
   private final NumberSetting spaceWidth = new NumberSetting("Space Width", (double)118.0F, (double)60.0F, (double)180.0F, (double)1.0F);
   private final NumberSetting spaceHeight = new NumberSetting("Space Height", (double)20.0F, (double)14.0F, (double)36.0F, (double)1.0F);
   private final NumberSetting textScale = new NumberSetting("Font Scale", (double)1.0F, (double)0.5F, (double)2.0F, 0.05);
   private final BooleanSetting rainbowMode = new BooleanSetting("Rainbow Mode", false);
   private final BooleanSetting rgbMode = new BooleanSetting("RGB Mode", false);
   private final NumberSetting pressedGlow = new NumberSetting("Pressed Glow", (double)24.0F, (double)0.0F, (double)80.0F, (double)1.0F);
   private final NumberSetting glowRadius = new NumberSetting("Glow Radius", (double)5.0F, (double)0.0F, (double)24.0F, (double)1.0F);
   private final NumberSetting shadowStrength = new NumberSetting("Shadow Strength", (double)54.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting innerHighlightStrength = new NumberSetting("Inner Highlight Strength", (double)12.0F, (double)0.0F, (double)80.0F, (double)1.0F);
   private final float[] pressAnim = new float[7];
   private long lastFrameNs = -1L;

   public KeystrokesModule() {
      super("Keystrokes", Category.HUD, ICON);
      this.x = 18;
      this.y = 18;
      this.addSetting(this.roundedCorners);
      this.addSetting(this.cornerRadius);
      this.addSetting(this.darkTheme);
      this.addSetting(this.extraDarkTheme);
      this.addSetting(this.backgroundOpacity);
      this.addSetting(this.backgroundRed);
      this.addSetting(this.backgroundGreen);
      this.addSetting(this.backgroundBlue);
      this.addSetting(this.borderRed);
      this.addSetting(this.borderGreen);
      this.addSetting(this.borderBlue);
      this.addSetting(this.borderOpacity);
      this.addSetting(this.pressedRed);
      this.addSetting(this.pressedGreen);
      this.addSetting(this.pressedBlue);
      this.addSetting(this.pressedOpacity);
      this.addSetting(this.accentRed);
      this.addSetting(this.accentGreen);
      this.addSetting(this.accentBlue);
      this.addSetting(this.textRed);
      this.addSetting(this.textGreen);
      this.addSetting(this.textBlue);
      this.addSetting(this.glowEnabled);
      this.addSetting(this.shadowEnabled);
      this.addSetting(this.glassEnabled);
      this.addSetting(this.pressedAnimation);
      this.addSetting(this.scale);
      this.addSetting(this.padding);
      this.addSetting(this.gap);
      this.addSetting(this.keySize);
      this.addSetting(this.mouseKeyWidth);
      this.addSetting(this.mouseKeyHeight);
      this.addSetting(this.spaceWidth);
      this.addSetting(this.spaceHeight);
      this.addSetting(this.textScale);
      this.addSetting(this.rainbowMode);
      this.addSetting(this.rgbMode);
      this.addSetting(this.pressedGlow);
      this.addSetting(this.glowRadius);
      this.addSetting(this.shadowStrength);
      this.addSetting(this.innerHighlightStrength);
      this.refreshSize();
   }

   public void onEnable() {
      this.refreshSize();

      for(int i = 0; i < this.pressAnim.length; ++i) {
         this.pressAnim[i] = 0.0F;
      }

      this.lastFrameNs = -1L;
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null && mc.field_1772 != null) {
         float hudScale = this.getHudScale();
         float dt = this.getDeltaTime();
         int pad = scaled(this.getPadding(), hudScale);
         int g = scaled(this.getGap(), hudScale);
         int k = scaled(this.getKeySize(), hudScale);
         int mouseW = scaled(this.getMouseKeyWidth(), hudScale);
         int mouseH = scaled(this.getMouseKeyHeight(), hudScale);
         int spaceW = scaled(this.getSpaceWidth(), hudScale);
         int spaceH = scaled(this.getSpaceHeight(), hudScale);
         int row2W = k * 3 + g * 2;
         int row3W = mouseW * 2 + g;
         int contentW = Math.max(Math.max(k, row2W), Math.max(row3W, spaceW));
         this.width = contentW + pad * 2;
         this.height = pad + k + g + k + g + mouseH + g + spaceH + pad;
         int row1X = pad + (contentW - k) / 2;
         int row2X = pad + (contentW - row2W) / 2;
         int row3X = pad + (contentW - row3W) / 2;
         int row4X = pad + (contentW - spaceW) / 2;
         int y2 = pad + k + g;
         int y3 = y2 + k + g;
         int y4 = y3 + mouseH + g;
         this.updateAnim(0, mc.field_1690.field_1894.method_1434(), dt);
         this.updateAnim(1, mc.field_1690.field_1913.method_1434(), dt);
         this.updateAnim(2, mc.field_1690.field_1881.method_1434(), dt);
         this.updateAnim(3, mc.field_1690.field_1849.method_1434(), dt);
         this.updateAnim(4, mc.field_1690.field_1886.method_1434(), dt);
         this.updateAnim(5, mc.field_1690.field_1904.method_1434(), dt);
         this.updateAnim(6, mc.field_1690.field_1903.method_1434(), dt);
         int idleText = this.resolveTextColor();
         int accentText = this.resolveAccentColor();
         this.renderKey(context, TXT_W, row1X, pad, k, k, 0, idleText, accentText);
         this.renderKey(context, TXT_A, row2X, y2, k, k, 1, idleText, accentText);
         this.renderKey(context, TXT_S, row2X + k + g, y2, k, k, 2, idleText, accentText);
         this.renderKey(context, TXT_D, row2X + (k + g) * 2, y2, k, k, 3, idleText, accentText);
         this.renderKey(context, TXT_LMB, row3X, y3, mouseW, mouseH, 4, idleText, accentText);
         this.renderKey(context, TXT_RMB, row3X + mouseW + g, y3, mouseW, mouseH, 5, idleText, accentText);
         this.renderKey(context, TXT_SPACE, row4X, y4, spaceW, spaceH, 6, idleText, accentText);
      }
   }

   private void renderKey(class_332 context, class_2561 label, int x, int y, int w, int h, int index, int idleTextColor, int accentTextColor) {
      float p = this.pressAnim[index];
      float eased = easeOutCubic(p);
      float radius = this.roundedCorners.getValue() ? this.getRadiusValue() : 0.0F;
      int bg = this.resolveBackgroundColor();
      int pressedBg = this.resolvePressedBackgroundColor();
      int keyColor = lerpColor(bg, pressedBg, eased * 0.8F);
      if (this.glassEnabled.getValue()) {
         keyColor = blendTowards(keyColor, -1, 0.03F);
      }

      if (this.extraDarkTheme.getValue()) {
         keyColor = darken(keyColor, 0.05F);
      }

      float scaleMul = 1.0F;
      if (this.pressedAnimation.getValue()) {
         scaleMul = 1.0F - 0.03F * eased;
      }

      int drawW = Math.max(1, Math.round((float)w * scaleMul));
      int drawH = Math.max(1, Math.round((float)h * scaleMul));
      int drawX = x + (w - drawW) / 2;
      int drawY = y + (h - drawH) / 2;
      float drawRadius = Math.max(0.0F, radius);
      if (this.shadowEnabled.getValue()) {
         int shadowAlpha = scaledAlpha((int)this.shadowStrength.getValue(), eased);
         int shadowColor = applyAlpha(-16777216, shadowAlpha);
         RoundedRectRenderer.fill(context, (float)(drawX + 1), (float)(drawY + 1), (float)drawW, (float)drawH, drawRadius, shadowColor);
      }

      if (this.glowEnabled.getValue() && eased > 0.001F) {
         int gr = scaled((int)this.glowRadius.getValue(), this.getHudScale());
         int glowAlpha = scaledAlpha((int)this.pressedGlow.getValue(), eased);
         int glowColor = applyAlpha(this.resolveAccentColor(), glowAlpha);
         RoundedRectRenderer.glow(context, (float)(drawX - gr), (float)(drawY - gr), (float)(drawW + gr * 2), (float)(drawH + gr * 2), drawRadius + (float)gr * 0.35F, glowColor);
      }

      RoundedRectRenderer.fill(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, drawRadius, keyColor);
      int highlightAlpha = Math.max(4, Math.round((float)((int)this.innerHighlightStrength.getValue()) * (0.35F + 0.65F * (1.0F - eased))));
      int highlightColor = applyAlpha(-1, highlightAlpha);
      RoundedRectRenderer.fill(context, (float)(drawX + 1), (float)(drawY + 1), (float)Math.max(0, drawW - 2), (float)Math.max(0, Math.max(1, drawH / 2) - 1), Math.max(0.0F, drawRadius - 1.0F), highlightColor);
      int borderColor = applyAlpha(rgbColor((int)this.borderRed.getValue(), (int)this.borderGreen.getValue(), (int)this.borderBlue.getValue()), (int)this.borderOpacity.getValue());
      RoundedRectRenderer.outline(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, drawRadius, 1.0F, borderColor, keyColor);
      int textColor = lerpColor(idleTextColor, accentTextColor, eased);
      int textW = mc.field_1772.method_27525(label);
      int textX = drawX + (drawW - textW) / 2;
      Objects.requireNonNull(mc.field_1772);
      int textY = drawY + (drawH - 9) / 2 - 1;
      float fontScale = (float)this.textScale.getValue();
      if (Math.abs(fontScale - 1.0F) > 0.001F) {
         Matrix3x2fStack matrices = context.method_51448();
         matrices.pushMatrix();
         matrices.scale(fontScale, fontScale);
         context.method_51439(mc.field_1772, label, Math.round((float)textX / fontScale), Math.round((float)textY / fontScale), textColor, false);
         matrices.popMatrix();
      } else {
         context.method_51439(mc.field_1772, label, textX, textY, textColor, false);
      }

   }

   private void updateAnim(int index, boolean pressed, float dt) {
      if (!this.pressedAnimation.getValue()) {
         this.pressAnim[index] = pressed ? 1.0F : 0.0F;
      } else {
         float target = pressed ? 1.0F : 0.0F;
         float speed = 14.0F;
         float factor = 1.0F - (float)Math.exp((double)(-speed * Math.max(0.0F, dt)));
         this.pressAnim[index] = lerp(this.pressAnim[index], target, factor);
         if (this.pressAnim[index] < 1.0E-4F) {
            this.pressAnim[index] = 0.0F;
         } else if (this.pressAnim[index] > 0.9999F) {
            this.pressAnim[index] = 1.0F;
         }

      }
   }

   private float getDeltaTime() {
      long now = System.nanoTime();
      if (this.lastFrameNs < 0L) {
         this.lastFrameNs = now;
         return 1.0F;
      } else {
         float dt = (float)(now - this.lastFrameNs) / 1.6666667E7F;
         this.lastFrameNs = now;
         if (dt < 0.0F) {
            dt = 0.0F;
         }

         if (dt > 4.0F) {
            dt = 4.0F;
         }

         return dt;
      }
   }

   private void refreshSize() {
      if (mc != null && mc.field_1772 != null) {
         float hudScale = this.getHudScale();
         int pad = scaled(this.getPadding(), hudScale);
         int g = scaled(this.getGap(), hudScale);
         int k = scaled(this.getKeySize(), hudScale);
         int mouseW = scaled(this.getMouseKeyWidth(), hudScale);
         int mouseH = scaled(this.getMouseKeyHeight(), hudScale);
         int spaceW = scaled(this.getSpaceWidth(), hudScale);
         int spaceH = scaled(this.getSpaceHeight(), hudScale);
         int row2W = k * 3 + g * 2;
         int row3W = mouseW * 2 + g;
         int contentW = Math.max(Math.max(k, row2W), Math.max(row3W, spaceW));
         this.width = contentW + pad * 2;
         this.height = pad + k + g + k + g + mouseH + g + spaceH + pad;
      } else {
         this.width = 120;
         this.height = 120;
      }
   }

   private float getHudScale() {
      return (float)this.scale.getValue();
   }

   private int getPadding() {
      return Math.max(0, (int)this.padding.getValue());
   }

   private int getGap() {
      return Math.max(0, (int)this.gap.getValue());
   }

   private int getKeySize() {
      return Math.max(18, (int)this.keySize.getValue());
   }

   private int getMouseKeyWidth() {
      return Math.max(24, (int)this.mouseKeyWidth.getValue());
   }

   private int getMouseKeyHeight() {
      return Math.max(14, (int)this.mouseKeyHeight.getValue());
   }

   private int getSpaceWidth() {
      return Math.max(60, (int)this.spaceWidth.getValue());
   }

   private int getSpaceHeight() {
      return Math.max(14, (int)this.spaceHeight.getValue());
   }

   private float getRadiusValue() {
      return Math.max(0.0F, (float)this.cornerRadius.getValue());
   }

   private int resolveBackgroundColor() {
      int alpha = (int)this.backgroundOpacity.getValue();
      int r;
      int g;
      int b;
      if (this.extraDarkTheme.getValue()) {
         r = 12;
         g = 14;
         b = 18;
      } else if (this.darkTheme.getValue()) {
         r = (int)this.backgroundRed.getValue();
         g = (int)this.backgroundGreen.getValue();
         b = (int)this.backgroundBlue.getValue();
      } else {
         r = 18;
         g = 20;
         b = 28;
      }

      return applyAlpha(rgbColor(r, g, b), alpha);
   }

   private int resolvePressedBackgroundColor() {
      return applyAlpha(rgbColor((int)this.pressedRed.getValue(), (int)this.pressedGreen.getValue(), (int)this.pressedBlue.getValue()), (int)this.pressedOpacity.getValue());
   }

   private int resolveTextColor() {
      if (this.rainbowMode.getValue()) {
         return this.rainbowColor();
      } else {
         return this.rgbMode.getValue() ? this.getHudColor() : rgbColor((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue());
      }
   }

   private int resolveAccentColor() {
      return this.rgbMode.getValue() ? this.getHudColor() : rgbColor((int)this.accentRed.getValue(), (int)this.accentGreen.getValue(), (int)this.accentBlue.getValue());
   }

   private int rainbowColor() {
      float time = (float)(System.currentTimeMillis() % 6000L) / 6000.0F;
      return Color.HSBtoRGB(time, 0.75F, 1.0F) | -16777216;
   }

   private static float easeOutCubic(float t) {
      float inv = 1.0F - clamp01(t);
      return 1.0F - inv * inv * inv;
   }

   private static int rgbColor(int r, int g, int b) {
      return -16777216 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(b);
   }

   private static int applyAlpha(int color, int alpha) {
      return clamp255(alpha) << 24 | color & 16777215;
   }

   private static int lerpColor(int a, int b, float t) {
      t = clamp01(t);
      int ar = a >> 16 & 255;
      int ag = a >> 8 & 255;
      int ab = a & 255;
      int aa = a >> 24 & 255;
      int br = b >> 16 & 255;
      int bg = b >> 8 & 255;
      int bb = b & 255;
      int ba = b >> 24 & 255;
      int r = Math.round((float)ar + (float)(br - ar) * t);
      int g = Math.round((float)ag + (float)(bg - ag) * t);
      int bl = Math.round((float)ab + (float)(bb - ab) * t);
      int al = Math.round((float)aa + (float)(ba - aa) * t);
      return clamp255(al) << 24 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(bl);
   }

   private static int blendTowards(int baseColor, int targetColor, float amount) {
      return lerpColor(baseColor, targetColor, amount);
   }

   private static int darken(int color, float amount) {
      amount = clamp01(amount);
      int a = color >> 24 & 255;
      int r = (int)((float)(color >> 16 & 255) * (1.0F - amount));
      int g = (int)((float)(color >> 8 & 255) * (1.0F - amount));
      int b = (int)((float)(color & 255) * (1.0F - amount));
      return clamp255(a) << 24 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(b);
   }

   private static int scaled(int value, float scale) {
      return Math.max(1, Math.round((float)value * scale));
   }

   private static int scaledAlpha(int alpha, float eased) {
      float boost = 0.25F + 0.75F * clamp01(eased);
      return clamp255(Math.round((float)alpha * boost));
   }

   private static float lerp(float a, float b, float t) {
      return a + (b - a) * t;
   }

   private static float clamp01(float value) {
      if (value < 0.0F) {
         return 0.0F;
      } else {
         return value > 1.0F ? 1.0F : value;
      }
   }

   private static int clamp255(int value) {
      return Math.max(0, Math.min(255, value));
   }
}
