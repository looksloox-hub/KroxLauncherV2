package com.example.client.ui.theme;

import com.example.client.ui.util.ColorUtil;

public final class GlassTheme {
   private GlassPreset preset;
   private boolean rounded = true;
   private boolean glassEnabled = true;
   private boolean shadowEnabled = true;
   private float globalOpacity = 1.0F;
   private float panelOpacity = 0.85F;
   private float glowStrength = 0.6F;
   private float borderStrength = 0.45F;
   private float shadowStrength = 0.6F;
   private int background;
   private int panel;
   private int card;
   private int button;
   private int buttonHover;
   private int buttonPressed;
   private int border;
   private int borderStrong;
   private int text;
   private int textDim;
   private int accent;
   private int accentSoft;
   private int shadow;
   private int glow;
   private float panelRadius = 9999.0F;
   private float cardRadius = 9999.0F;
   private float buttonRadius = 9999.0F;
   private float fieldRadius = 9999.0F;
   private float popupRadius = 9999.0F;

   public GlassTheme() {
      this.applyPreset(GlassPreset.HYPER);
      this.setUltraRounded();
   }

   public static GlassTheme defaultTheme() {
      return new GlassTheme();
   }

   public GlassTheme copy() {
      GlassTheme t = new GlassTheme();
      t.preset = this.preset;
      t.rounded = this.rounded;
      t.glassEnabled = this.glassEnabled;
      t.shadowEnabled = this.shadowEnabled;
      t.globalOpacity = this.globalOpacity;
      t.panelOpacity = this.panelOpacity;
      t.glowStrength = this.glowStrength;
      t.borderStrength = this.borderStrength;
      t.shadowStrength = this.shadowStrength;
      t.background = this.background;
      t.panel = this.panel;
      t.card = this.card;
      t.button = this.button;
      t.buttonHover = this.buttonHover;
      t.buttonPressed = this.buttonPressed;
      t.border = this.border;
      t.borderStrong = this.borderStrong;
      t.text = this.text;
      t.textDim = this.textDim;
      t.accent = this.accent;
      t.accentSoft = this.accentSoft;
      t.shadow = this.shadow;
      t.glow = this.glow;
      t.panelRadius = this.panelRadius;
      t.cardRadius = this.cardRadius;
      t.buttonRadius = this.buttonRadius;
      t.fieldRadius = this.fieldRadius;
      t.popupRadius = this.popupRadius;
      return t;
   }

   public GlassTheme applyPreset(GlassPreset preset) {
      if (preset == null) {
         return this;
      } else {
         this.preset = preset;
         this.background = preset.background();
         this.panel = preset.panel();
         this.card = preset.card();
         this.text = preset.text();
         this.textDim = preset.textDim();
         this.accent = preset.accent();
         this.accentSoft = preset.accentSoft();
         this.shadow = preset.shadow();
         this.glow = preset.glow();
         this.button = -1340993006;
         this.buttonHover = -803332578;
         this.buttonPressed = -267777526;
         this.border = 637534207;
         this.borderStrong = 1090519039;
         this.setUltraRounded();
         return this;
      }
   }

   public GlassTheme setUltraRounded() {
      this.panelRadius = 9999.0F;
      this.cardRadius = 9999.0F;
      this.buttonRadius = 9999.0F;
      this.fieldRadius = 9999.0F;
      this.popupRadius = 9999.0F;
      return this;
   }

   public GlassPreset preset() {
      return this.preset;
   }

   public boolean rounded() {
      return this.rounded;
   }

   public GlassTheme setRounded(boolean rounded) {
      this.rounded = rounded;
      return this;
   }

   public boolean glassEnabled() {
      return this.glassEnabled;
   }

   public GlassTheme setGlassEnabled(boolean glassEnabled) {
      this.glassEnabled = glassEnabled;
      return this;
   }

   public boolean shadowEnabled() {
      return this.shadowEnabled;
   }

   public GlassTheme setShadowEnabled(boolean shadowEnabled) {
      this.shadowEnabled = shadowEnabled;
      return this;
   }

   public float globalOpacity() {
      return this.globalOpacity;
   }

   public GlassTheme setGlobalOpacity(float globalOpacity) {
      this.globalOpacity = clamp01(globalOpacity);
      return this;
   }

   public float panelOpacity() {
      return this.panelOpacity;
   }

   public GlassTheme setPanelOpacity(float panelOpacity) {
      this.panelOpacity = clamp01(panelOpacity);
      return this;
   }

   public float glowStrength() {
      return this.glowStrength;
   }

   public GlassTheme setGlowStrength(float glowStrength) {
      this.glowStrength = Math.max(0.0F, glowStrength);
      return this;
   }

   public float borderStrength() {
      return this.borderStrength;
   }

   public GlassTheme setBorderStrength(float borderStrength) {
      this.borderStrength = Math.max(0.0F, borderStrength);
      return this;
   }

   public float shadowStrength() {
      return this.shadowStrength;
   }

   public GlassTheme setShadowStrength(float shadowStrength) {
      this.shadowStrength = Math.max(0.0F, shadowStrength);
      return this;
   }

   public float panelRadius() {
      return this.panelRadius;
   }

   public GlassTheme setPanelRadius(float panelRadius) {
      this.panelRadius = panelRadius;
      return this;
   }

   public float cardRadius() {
      return this.cardRadius;
   }

   public GlassTheme setCardRadius(float cardRadius) {
      this.cardRadius = cardRadius;
      return this;
   }

   public float buttonRadius() {
      return this.buttonRadius;
   }

   public GlassTheme setButtonRadius(float buttonRadius) {
      this.buttonRadius = buttonRadius;
      return this;
   }

   public float fieldRadius() {
      return this.fieldRadius;
   }

   public GlassTheme setFieldRadius(float fieldRadius) {
      this.fieldRadius = fieldRadius;
      return this;
   }

   public float popupRadius() {
      return this.popupRadius;
   }

   public GlassTheme setPopupRadius(float popupRadius) {
      this.popupRadius = popupRadius;
      return this;
   }

   public int background() {
      return this.applyGlobalOpacity(this.background);
   }

   public int panel() {
      return this.applyPanelOpacity(this.panel);
   }

   public int card() {
      return this.applyPanelOpacity(this.card);
   }

   public int button() {
      return this.applyPanelOpacity(this.button);
   }

   public int button(boolean hovered, boolean pressed) {
      int color = pressed ? this.buttonPressed : (hovered ? this.buttonHover : this.button);
      return this.applyPanelOpacity(color);
   }

   public int border() {
      return this.applyGlobalOpacity(ColorUtil.scaleAlpha(this.border, this.borderStrength));
   }

   public int borderStrong() {
      return this.applyGlobalOpacity(ColorUtil.scaleAlpha(this.borderStrong, this.borderStrength));
   }

   public int text() {
      return this.applyGlobalOpacity(this.text);
   }

   public int textDim() {
      return this.applyGlobalOpacity(this.textDim);
   }

   public int accent() {
      return this.applyGlobalOpacity(this.accent);
   }

   public int accentSoft() {
      return this.applyGlobalOpacity(this.accentSoft);
   }

   public int shadow() {
      return this.applyGlobalOpacity(ColorUtil.scaleAlpha(this.shadow, this.shadowStrength));
   }

   public int glow() {
      return this.applyGlobalOpacity(ColorUtil.scaleAlpha(this.glow, this.glowStrength));
   }

   public float resolveRadius(float requestedRadius, float width, float height) {
      return !this.rounded ? 0.0F : GlassRadius.resolve(requestedRadius, width, height);
   }

   public float resolvePanelRadius(float width, float height) {
      return this.resolveRadius(this.panelRadius, width, height);
   }

   public float resolveCardRadius(float width, float height) {
      return this.resolveRadius(this.cardRadius, width, height);
   }

   public float resolveButtonRadius(float width, float height) {
      return this.resolveRadius(this.buttonRadius, width, height);
   }

   public float resolveFieldRadius(float width, float height) {
      return this.resolveRadius(this.fieldRadius, width, height);
   }

   public float resolvePopupRadius(float width, float height) {
      return this.resolveRadius(this.popupRadius, width, height);
   }

   public int applyGlobalOpacity(int color) {
      return ColorUtil.scaleAlpha(color, this.globalOpacity);
   }

   public int applyPanelOpacity(int color) {
      return ColorUtil.scaleAlpha(color, this.panelOpacity);
   }

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
