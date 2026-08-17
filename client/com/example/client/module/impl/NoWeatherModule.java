package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class NoWeatherModule extends Module {
   public NoWeatherModule() {
      super("NoWeather", Category.RENDER, (class_2960)null);
   }

   public boolean shouldRenderRain() {
      return !this.isEnabled();
   }
}
