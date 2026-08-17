package com.example.client.ui.core;

import net.minecraft.class_332;

public class GlassModuleCardComponent extends GlassComponent {
   private String title;
   private String category;
   private String iconGlyph;
   private boolean enabled;
   private Runnable onToggle;
   private Runnable onSettings;

   public GlassModuleCardComponent(int x, int y, int width, int height, String title, String category, String iconGlyph, boolean enabled, Runnable onToggle, Runnable onSettings) {
      super(x, y, width, height);
      this.title = title;
      this.category = category;
      this.iconGlyph = iconGlyph;
      this.enabled = enabled;
      this.onToggle = onToggle;
      this.onSettings = onSettings;
   }

   public void render(class_332 context, int mouseX, int mouseY, float delta) {
   }

   public boolean enabled() {
      return this.enabled;
   }

   public GlassModuleCardComponent setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }
}
