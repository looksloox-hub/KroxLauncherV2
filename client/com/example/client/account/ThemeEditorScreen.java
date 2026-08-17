package com.example.client.account;

import com.example.client.ui.render.RoundedRectRenderer;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_437;

public class ThemeEditorScreen extends class_437 {
   private final class_437 parent;
   private static final int BG_TOP = -16314342;
   private static final int BG_BOTTOM = -15722197;
   private static final int PANEL = -753789149;
   private static final int PANEL_HOVER = -535420366;
   private static final int BORDER = 796693976;
   private static final int BORDER_HOVER = 1719440856;
   private static final int TEXT = -1;
   private static final int TEXT_DIM = -690033409;
   private static final int TEXT_FAINT = -2002341177;
   private static final int ACCENT_SOFT = 864986623;
   private float openAnim = 0.0F;
   private float hoverOcean = 0.0F;
   private float hoverViolet = 0.0F;
   private float hoverMint = 0.0F;
   private float hoverRose = 0.0F;
   private float hoverAmber = 0.0F;
   private float hoverMidnight = 0.0F;
   private float hoverBack = 0.0F;
   private int mouseX;
   private int mouseY;

   public ThemeEditorScreen(class_437 parent) {
      super(class_2561.method_43470("Theme Editor"));
      this.parent = parent;
   }

   protected void method_25426() {
      this.openAnim = 0.0F;
   }

   private static boolean over(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private static float clamp01(float v) {
      return Math.max(0.0F, Math.min(1.0F, v));
   }

   private static float lerp(float current, float target, float speed) {
      return current + (target - current) * speed;
   }

   private static float easeOutCubic(float t) {
      t = clamp01(t);
      float p = 1.0F - t;
      return 1.0F - p * p * p;
   }

   private void drawGlow(class_332 context, int x, int y, int w, int h, int radius, int color, int layers) {
      if (w > 0 && h > 0 && layers > 0) {
         int safeLayers = Math.min(layers, 3);

         for(int i = safeLayers; i >= 1; --i) {
            int pad = i * 2;
            int alpha = (int)((float)(color >>> 24 & 255) * 0.1F / (float)i);
            int c = alpha << 24 | color & 16777215;
            RoundedRectRenderer.outline(context, (float)(x - pad), (float)(y - pad), (float)(w + pad * 2), (float)(h + pad * 2), Math.max(4.0F, (float)(radius + pad)), 1.0F, c, 0);
         }

      }
   }

   private void drawCard(class_332 context, int x, int y, int w, int h, int fill, int border) {
      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, 16.0F, 1.0F, border, fill);
   }

   private void drawButton(class_332 context, int x, int y, int w, int h, String title, String subtitle, int accent, float hover) {
      int fill = this.lerpColor(-753789149, -535420366, hover * 0.55F);
      int border = hover > 0.01F ? this.lerpColor(796693976, accent, hover) : 796693976;
      if (hover > 0.01F) {
         this.drawGlow(context, x, y, w, h, 16, accent, 2);
      }

      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, 16.0F, 1.0F, border, fill);
      context.method_27534(this.field_22793, class_2561.method_43470(title), x + w / 2, y + 13, -1);
      context.method_27534(this.field_22793, class_2561.method_43470(subtitle), x + w / 2, y + 26, -2002341177);
   }

   private int lerpColor(int a, int b, float t) {
      t = clamp01(t);
      int aa = a >>> 24 & 255;
      int ar = a >>> 16 & 255;
      int ag = a >>> 8 & 255;
      int ab = a & 255;
      int ba = b >>> 24 & 255;
      int br = b >>> 16 & 255;
      int bg = b >>> 8 & 255;
      int bb = b & 255;
      int ra = (int)((float)aa + (float)(ba - aa) * t);
      int rr = (int)((float)ar + (float)(br - ar) * t);
      int rg = (int)((float)ag + (float)(bg - ag) * t);
      int rb = (int)((float)ab + (float)(bb - ab) * t);
      return ra << 24 | rr << 16 | rg << 8 | rb;
   }

   private void applyTheme(String preset) {
      ThemeConfig.applyPreset(preset);
      ThemeConfig.save();
      class_310.method_1551().method_1507(new AccountSwitcherScreen(this.parent));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      float dt = Math.max(0.0F, Math.min(delta, 0.05F));
      this.openAnim = class_3532.method_15363(this.openAnim + dt * 7.0F, 0.0F, 1.0F);
      int panelX = this.field_22789 / 2 - 190;
      int panelY = 28;
      int panelW = 380;
      int panelH = 228;
      float ui = easeOutCubic(this.openAnim);
      float scale = 0.975F + ui * 0.025F;
      float lift = (1.0F - ui) * 10.0F;
      this.drawBackground(context);
      int drawW = Math.round((float)panelW * scale);
      int drawH = Math.round((float)panelH * scale);
      int drawX = Math.round((float)panelX + (float)(panelW - drawW) / 2.0F);
      int drawY = Math.round((float)panelY + (float)(panelH - drawH) / 2.0F + lift);
      this.drawGlow(context, drawX, drawY, drawW, drawH, 18, 271268711, 2);
      this.drawCard(context, drawX, drawY, drawW, drawH, ThemeConfig.PANEL, ThemeConfig.ACCENT);
      context.method_27534(this.field_22793, class_2561.method_43470("Theme Editor"), this.field_22789 / 2, drawY + 12, -1);
      context.method_27534(this.field_22793, class_2561.method_43470("Pick a preset that fits your client style"), this.field_22789 / 2, drawY + 25, -690033409);
      int left = drawX + 18;
      int top = drawY + 44;
      int tileW = 157;
      int tileH = 42;
      int gapX = 18;
      int gapY = 10;
      boolean hovOcean = over((double)mouseX, (double)mouseY, left, top, tileW, tileH);
      boolean hovViolet = over((double)mouseX, (double)mouseY, left + tileW + gapX, top, tileW, tileH);
      boolean hovMint = over((double)mouseX, (double)mouseY, left, top + tileH + gapY, tileW, tileH);
      boolean hovRose = over((double)mouseX, (double)mouseY, left + tileW + gapX, top + tileH + gapY, tileW, tileH);
      boolean hovAmber = over((double)mouseX, (double)mouseY, left, top + (tileH + gapY) * 2, tileW, tileH);
      boolean hovMidnight = over((double)mouseX, (double)mouseY, left + tileW + gapX, top + (tileH + gapY) * 2, tileW, tileH);
      this.hoverOcean = lerp(this.hoverOcean, hovOcean ? 1.0F : 0.0F, 0.18F);
      this.hoverViolet = lerp(this.hoverViolet, hovViolet ? 1.0F : 0.0F, 0.18F);
      this.hoverMint = lerp(this.hoverMint, hovMint ? 1.0F : 0.0F, 0.18F);
      this.hoverRose = lerp(this.hoverRose, hovRose ? 1.0F : 0.0F, 0.18F);
      this.hoverAmber = lerp(this.hoverAmber, hovAmber ? 1.0F : 0.0F, 0.18F);
      this.hoverMidnight = lerp(this.hoverMidnight, hovMidnight ? 1.0F : 0.0F, 0.18F);
      this.drawButton(context, left, top, tileW, tileH, "Ocean", "Blue glass", -13058568, this.hoverOcean);
      this.drawButton(context, left + tileW + gapX, top, tileW, tileH, "Violet", "Purple neon", -5745161, this.hoverViolet);
      this.drawButton(context, left, top + tileH + gapY, tileW, tileH, "Mint", "Clean green", -13315175, this.hoverMint);
      this.drawButton(context, left + tileW + gapX, top + tileH + gapY, tileW, tileH, "Rose", "Soft pink", -757066, this.hoverRose);
      this.drawButton(context, left, top + (tileH + gapY) * 2, tileW, tileH, "Amber", "Warm gold", -680437, this.hoverAmber);
      this.drawButton(context, left + tileW + gapX, top + (tileH + gapY) * 2, tileW, tileH, "Midnight", "Deep blue", -12877066, this.hoverMidnight);
      int backW = 104;
      int backH = 24;
      int backX = this.field_22789 / 2 - backW / 2;
      int backY = drawY + drawH - 34;
      boolean hovBack = over((double)mouseX, (double)mouseY, backX, backY, backW, backH);
      this.hoverBack = lerp(this.hoverBack, hovBack ? 1.0F : 0.0F, 0.18F);
      int backFill = hovBack ? -535420366 : -753789149;
      int backBorder = hovBack ? 1719440856 : 796693976;
      if (hovBack) {
         this.drawGlow(context, backX, backY, backW, backH, 12, 864986623, 2);
      }

      RoundedRectRenderer.outline(context, (float)backX, (float)backY, (float)backW, (float)backH, 12.0F, 1.0F, backBorder, backFill);
      context.method_27534(this.field_22793, class_2561.method_43470("Back"), this.field_22789 / 2, backY + 8, -1);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   private void drawBackground(class_332 context) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -16314342, -15722197);
      context.method_25294(0, 0, this.field_22789, this.field_22790, 1510082578);
      this.drawGlow(context, 0, 0, this.field_22789, this.field_22790, 0, 271268711, 1);
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mx = click.comp_4798();
      double my = click.comp_4799();
      int panelX = this.field_22789 / 2 - 190;
      int panelY = 28;
      int panelW = 380;
      int panelH = 228;
      float ui = easeOutCubic(this.openAnim);
      float scale = 0.975F + ui * 0.025F;
      float lift = (1.0F - ui) * 10.0F;
      int drawW = Math.round((float)panelW * scale);
      int drawH = Math.round((float)panelH * scale);
      int drawX = Math.round((float)panelX + (float)(panelW - drawW) / 2.0F);
      int drawY = Math.round((float)panelY + (float)(panelH - drawH) / 2.0F + lift);
      int left = drawX + 18;
      int top = drawY + 44;
      int tileW = 157;
      int tileH = 42;
      int gapX = 18;
      int gapY = 10;
      if (over(mx, my, left, top, tileW, tileH)) {
         this.applyTheme("Ocean");
         return true;
      } else if (over(mx, my, left + tileW + gapX, top, tileW, tileH)) {
         this.applyTheme("Violet");
         return true;
      } else if (over(mx, my, left, top + tileH + gapY, tileW, tileH)) {
         this.applyTheme("Mint");
         return true;
      } else if (over(mx, my, left + tileW + gapX, top + tileH + gapY, tileW, tileH)) {
         this.applyTheme("Rose");
         return true;
      } else if (over(mx, my, left, top + (tileH + gapY) * 2, tileW, tileH)) {
         this.applyTheme("Amber");
         return true;
      } else if (over(mx, my, left + tileW + gapX, top + (tileH + gapY) * 2, tileW, tileH)) {
         this.applyTheme("Midnight");
         return true;
      } else {
         int backW = 104;
         int backH = 24;
         int backX = this.field_22789 / 2 - backW / 2;
         int backY = drawY + drawH - 34;
         if (over(mx, my, backX, backY, backW, backH)) {
            ThemeConfig.save();
            class_310.method_1551().method_1507(new AccountSwitcherScreen(this.parent));
            return true;
         } else {
            return super.method_25402(click, doubled);
         }
      }
   }

   public boolean method_25404(class_11908 input) {
      int keyCode = input.comp_4795();
      if (keyCode == 256) {
         ThemeConfig.save();
         class_310.method_1551().method_1507(this.parent);
         return true;
      } else {
         return super.method_25404(input);
      }
   }

   public boolean method_25400(class_11905 input) {
      return super.method_25400(input);
   }

   public void method_25419() {
      ThemeConfig.save();
      class_310.method_1551().method_1507(this.parent);
   }

   public boolean method_25421() {
      return false;
   }
}
