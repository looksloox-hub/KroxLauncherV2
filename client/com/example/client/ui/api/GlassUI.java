package com.example.client.ui.api;

import com.example.client.ui.core.GlassButtonComponent;
import com.example.client.ui.core.GlassCardComponent;
import com.example.client.ui.core.GlassModuleCardComponent;
import com.example.client.ui.core.GlassPanelComponent;
import com.example.client.ui.core.GlassScrollPanelComponent;
import com.example.client.ui.core.GlassSearchBarComponent;
import com.example.client.ui.core.GlassTextFieldComponent;
import com.example.client.ui.core.GlassToggleComponent;
import com.example.client.ui.render.GlassBackdropRenderer;
import com.example.client.ui.render.GlassRenderer;
import com.example.client.ui.theme.GlassPreset;
import com.example.client.ui.theme.GlassTheme;
import com.example.client.ui.theme.GlassThemeManager;
import net.minecraft.class_332;

public final class GlassUI {
   private static final ThreadLocal<class_332> CURRENT = new ThreadLocal();

   private GlassUI() {
   }

   public static void begin(class_332 context) {
      CURRENT.set(context);
   }

   public static void end() {
      CURRENT.remove();
   }

   public static class_332 current() {
      class_332 context = (class_332)CURRENT.get();
      if (context == null) {
         throw new IllegalStateException("GlassUI.begin(context) must be called before using the current-context API.");
      } else {
         return context;
      }
   }

   public static GlassTheme theme() {
      return GlassThemeManager.active();
   }

   public static void setTheme(GlassTheme theme) {
      GlassThemeManager.setActive(theme);
   }

   public static void usePreset(GlassPreset preset) {
      GlassThemeManager.usePreset(preset);
   }

   public static void usePreset(GlassPreset preset, float globalOpacity, float panelOpacity) {
      GlassThemeManager.usePreset(preset, globalOpacity, panelOpacity);
   }

   public static void blurBackground() {
      GlassBackdropRenderer.blurBackground(current());
   }

   public static void blurBackground(class_332 context) {
      GlassBackdropRenderer.blurBackground(context);
   }

   public static void blurAndTint(int tintColor) {
      GlassBackdropRenderer.blurAndTint(current(), tintColor);
   }

   public static void blurAndTint(class_332 context, int tintColor) {
      GlassBackdropRenderer.blurAndTint(context, tintColor);
   }

   public static void panel(float x, float y, float width, float height) {
      GlassRenderer.drawPanel(current(), x, y, width, height);
   }

   public static void panel(class_332 context, float x, float y, float width, float height) {
      GlassRenderer.drawPanel(context, x, y, width, height);
   }

   public static void card(float x, float y, float width, float height) {
      GlassRenderer.drawCard(current(), x, y, width, height);
   }

   public static void card(class_332 context, float x, float y, float width, float height) {
      GlassRenderer.drawCard(context, x, y, width, height);
   }

   public static void button(float x, float y, float width, float height, String label, boolean hovered, boolean pressed) {
      GlassRenderer.drawButton(current(), x, y, width, height, label, hovered, pressed);
   }

   public static void button(class_332 context, float x, float y, float width, float height, String label, boolean hovered, boolean pressed) {
      GlassRenderer.drawButton(context, x, y, width, height, label, hovered, pressed);
   }

   public static void toggle(float x, float y, float width, float height, String label, boolean value, float progress) {
      GlassRenderer.drawToggle(current(), x, y, width, height, label, value, progress);
   }

   public static void toggle(class_332 context, float x, float y, float width, float height, String label, boolean value, float progress) {
      GlassRenderer.drawToggle(context, x, y, width, height, label, value, progress);
   }

   public static void searchBar(float x, float y, float width, float height, String text, String placeholder, boolean focused, boolean cursorVisible) {
      GlassRenderer.drawSearchBar(current(), x, y, width, height, text, placeholder, focused, cursorVisible);
   }

   public static void searchBar(class_332 context, float x, float y, float width, float height, String text, String placeholder, boolean focused, boolean cursorVisible) {
      GlassRenderer.drawSearchBar(context, x, y, width, height, text, placeholder, focused, cursorVisible);
   }

   public static void text(float x, float y, String text) {
      GlassRenderer.drawText(current(), text, x, y);
   }

   public static void text(class_332 context, float x, float y, String text) {
      GlassRenderer.drawText(context, text, x, y);
   }

   public static void text(float x, float y, String text, int color) {
      GlassRenderer.drawText(current(), text, x, y, color);
   }

   public static void text(class_332 context, float x, float y, String text, int color) {
      GlassRenderer.drawText(context, text, x, y, color);
   }

   public static void centeredText(float centerX, float y, String text, int color) {
      GlassRenderer.drawTextCentered(current(), text, centerX, y, color);
   }

   public static void centeredText(class_332 context, float centerX, float y, String text, int color) {
      GlassRenderer.drawTextCentered(context, text, centerX, y, color);
   }

   public static void moduleCard(float x, float y, float width, float height, String title, String category, String iconGlyph, boolean enabled, boolean hovered) {
      GlassRenderer.drawModuleCard(current(), x, y, width, height, title, category, iconGlyph, enabled, hovered);
   }

   public static void moduleCard(class_332 context, float x, float y, float width, float height, String title, String category, String iconGlyph, boolean enabled, boolean hovered) {
      GlassRenderer.drawModuleCard(context, x, y, width, height, title, category, iconGlyph, enabled, hovered);
   }

   public static GlassPanelComponent panelComponent(int x, int y, int width, int height) {
      return new GlassPanelComponent(x, y, width, height);
   }

   public static GlassPanelComponent panelComponent(String title, int x, int y, int width, int height) {
      return new GlassPanelComponent(title, x, y, width, height);
   }

   public static GlassCardComponent cardComponent(int x, int y, int width, int height, String title, String subtitle, String iconGlyph) {
      return new GlassCardComponent(x, y, width, height, title, subtitle, iconGlyph);
   }

   public static GlassModuleCardComponent moduleCardComponent(int x, int y, int width, int height, String title, String category, String iconGlyph, boolean enabled, Runnable onToggle, Runnable onSettings) {
      return new GlassModuleCardComponent(x, y, width, height, title, category, iconGlyph, enabled, onToggle, onSettings);
   }

   public static GlassButtonComponent buttonComponent(int x, int y, int width, int height, String label, Runnable action) {
      return new GlassButtonComponent(x, y, width, height, label, action);
   }

   public static GlassToggleComponent toggleComponent(int x, int y, int width, int height, String label, boolean enabled, Runnable onChange) {
      return new GlassToggleComponent(x, y, width, height, label, enabled, onChange);
   }

   public static GlassTextFieldComponent textFieldComponent(int x, int y, int width, int height, String placeholder) {
      return new GlassTextFieldComponent(x, y, width, height, placeholder);
   }

   public static GlassSearchBarComponent searchBarComponent(int x, int y, int width, int height, String placeholder) {
      return new GlassSearchBarComponent(x, y, width, height, placeholder);
   }

   public static GlassScrollPanelComponent scrollPanelComponent(int x, int y, int width, int height) {
      return new GlassScrollPanelComponent(x, y, width, height);
   }

   public static GlassScrollPanelComponent scrollPanelComponent(String title, int x, int y, int width, int height) {
      return (new GlassScrollPanelComponent(x, y, width, height)).setTitle(title);
   }
}
