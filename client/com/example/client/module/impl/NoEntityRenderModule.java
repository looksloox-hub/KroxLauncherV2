package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class NoEntityRenderModule extends Module {
   public NoEntityRenderModule() {
      super("NoEntityRender", Category.RENDER, (class_2960)null);
   }

   public boolean shouldRenderEntity() {
      return !this.isEnabled();
   }
}
