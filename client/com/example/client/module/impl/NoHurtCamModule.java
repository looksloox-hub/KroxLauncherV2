package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class NoHurtCamModule extends Module {
   public static NoHurtCamModule INSTANCE;

   public NoHurtCamModule() {
      super("NoHurtCam", Category.RENDER, (class_2960)null);
      INSTANCE = this;
   }
}
