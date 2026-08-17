package com.example.client.setting;

public class ModeSetting extends Setting {
   private final String[] modes;
   private int index;

   public ModeSetting(String name, String defaultMode, String... modes) {
      super(name);
      this.modes = modes;

      for(int i = 0; i < modes.length; ++i) {
         if (modes[i].equalsIgnoreCase(defaultMode)) {
            this.index = i;
            break;
         }
      }

   }

   public String getMode() {
      return this.modes[this.index];
   }

   public boolean is(String mode) {
      return this.getMode().equalsIgnoreCase(mode);
   }

   public void setMode(String mode) {
      for(int i = 0; i < this.modes.length; ++i) {
         if (this.modes[i].equalsIgnoreCase(mode)) {
            this.index = i;
            return;
         }
      }

   }

   public void next() {
      ++this.index;
      if (this.index >= this.modes.length) {
         this.index = 0;
      }

   }

   public void previous() {
      --this.index;
      if (this.index < 0) {
         this.index = this.modes.length - 1;
      }

   }

   public void cycle() {
      this.next();
   }

   public String[] getModes() {
      return this.modes;
   }

   public int getIndex() {
      return this.index;
   }
}
