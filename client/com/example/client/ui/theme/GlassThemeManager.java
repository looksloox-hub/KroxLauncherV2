package com.example.client.ui.theme;

public final class GlassThemeManager {
   private static GlassTheme active = new GlassTheme();

   private GlassThemeManager() {
   }

   public static GlassTheme active() {
      return active;
   }

   public static void setActive(GlassTheme theme) {
      active = theme == null ? new GlassTheme() : theme;
   }

   public static void usePreset(GlassPreset preset) {
      active().applyPreset(preset);
   }

   public static void usePreset(GlassPreset preset, float globalOpacity, float panelOpacity) {
      active().applyPreset(preset).setGlobalOpacity(globalOpacity).setPanelOpacity(panelOpacity);
   }
}
