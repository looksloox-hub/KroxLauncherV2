package com.example.client.ui.theme;

public enum GlassPreset {
   HYPER(-871428076, -871099110, -871362284, -870901732, -870375129, -871362284, -14012358, -11688961, -722949, -4735287, -11688961, 1716364287, -1728053248, 1716364287);

   private final int background;
   private final int panel;
   private final int card;
   private final int button;
   private final int buttonHover;
   private final int buttonPressed;
   private final int border;
   private final int borderStrong;
   private final int text;
   private final int textDim;
   private final int accent;
   private final int accentSoft;
   private final int shadow;
   private final int glow;

   private GlassPreset(int background, int panel, int card, int button, int buttonHover, int buttonPressed, int border, int borderStrong, int text, int textDim, int accent, int accentSoft, int shadow, int glow) {
      this.background = background;
      this.panel = panel;
      this.card = card;
      this.button = button;
      this.buttonHover = buttonHover;
      this.buttonPressed = buttonPressed;
      this.border = border;
      this.borderStrong = borderStrong;
      this.text = text;
      this.textDim = textDim;
      this.accent = accent;
      this.accentSoft = accentSoft;
      this.shadow = shadow;
      this.glow = glow;
   }

   public int background() {
      return this.background;
   }

   public int panel() {
      return this.panel;
   }

   public int card() {
      return this.card;
   }

   public int button() {
      return this.button;
   }

   public int buttonHover() {
      return this.buttonHover;
   }

   public int buttonPressed() {
      return this.buttonPressed;
   }

   public int border() {
      return this.border;
   }

   public int borderStrong() {
      return this.borderStrong;
   }

   public int text() {
      return this.text;
   }

   public int textDim() {
      return this.textDim;
   }

   public int accent() {
      return this.accent;
   }

   public int accentSoft() {
      return this.accentSoft;
   }

   public int shadow() {
      return this.shadow;
   }

   public int glow() {
      return this.glow;
   }

   // $FF: synthetic method
   private static GlassPreset[] $values() {
      return new GlassPreset[]{HYPER};
   }
}
