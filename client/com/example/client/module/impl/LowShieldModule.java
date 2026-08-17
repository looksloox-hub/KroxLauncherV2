package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.NumberSetting;
import net.minecraft.class_2960;

public class LowShieldModule extends Module {
   public final NumberSetting yOffset = new NumberSetting("Shield Y", 0.28, (double)0.0F, (double)1.0F, 0.01);
   public final NumberSetting scale = new NumberSetting("Shield Scale", 0.78, (double)0.5F, (double)1.0F, 0.01);

   public LowShieldModule() {
      super("LowShield", Category.MISC, (class_2960)null);
      this.addSetting(this.yOffset);
      this.addSetting(this.scale);
   }
}
