package com.example.client.culling;

import net.minecraft.class_238;
import net.minecraft.class_4604;

public final class FrustumCuller {
   private volatile class_4604 frustum;

   public void capture(class_4604 frustum) {
      this.frustum = frustum;
   }

   public boolean visible(class_238 box) {
      class_4604 f = this.frustum;
      return f == null || f.method_23093(box);
   }
}
