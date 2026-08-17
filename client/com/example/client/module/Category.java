package com.example.client.module;

public enum Category {
   ALL("All", "★"),
   HUD("HUD", "▣"),
   RENDER("Render", "◈"),
   MECHANICS("Mechanics", "⌁"),
   MISC("Misc", "⋯");

   private final String displayName;
   private final String icon;

   private Category(String displayName, String icon) {
      this.displayName = displayName;
      this.icon = icon;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getIcon() {
      return this.icon;
   }

   // $FF: synthetic method
   private static Category[] $values() {
      return new Category[]{ALL, HUD, RENDER, MECHANICS, MISC};
   }
}
