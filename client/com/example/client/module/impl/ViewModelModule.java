package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.setting.NumberSetting;
import net.minecraft.class_2960;
import net.minecraft.class_4587;

public class ViewModelModule extends Module {
   public NumberSetting offsetX = new NumberSetting("Offset X", (double)0.0F, (double)-1.0F, (double)1.0F, 0.01);
   public NumberSetting offsetY = new NumberSetting("Offset Y", 0.28, (double)-1.0F, (double)1.0F, 0.01);
   public NumberSetting offsetZ = new NumberSetting("Offset Z", 0.2, (double)-1.0F, (double)1.0F, 0.01);
   public NumberSetting scale = new NumberSetting("Scale", 0.8, (double)0.5F, (double)1.5F, 0.01);

   public ViewModelModule() {
      super("ViewModel", Category.MISC, (class_2960)null);
      this.addSetting(this.offsetX);
      this.addSetting(this.offsetY);
      this.addSetting(this.offsetZ);
      this.addSetting(this.scale);
   }

   public void applyTransform(class_4587 matrices, boolean mainHand, boolean isBlocking) {
      if (isBlocking) {
         float x = (float)this.offsetX.getValue();
         float y = (float)this.offsetY.getValue();
         float z = (float)this.offsetZ.getValue();
         float s = (float)this.scale.getValue();
         matrices.method_46416(x, y, z);
         matrices.method_22905(s, s, s);
      }
   }
}
