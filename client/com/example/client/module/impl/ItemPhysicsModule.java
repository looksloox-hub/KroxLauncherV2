package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import net.minecraft.class_2960;

public class ItemPhysicsModule extends Module {
   public static ItemPhysicsModule INSTANCE;

   public ItemPhysicsModule() {
      super("OptiDrops", Category.RENDER, (class_2960)null);
      INSTANCE = this;
   }
}
