package com.example.mixin;

import net.minecraft.class_2960;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({class_757.class})
public class GameRendererMixin {
   static {
      class_2960.method_60655("example", "shaders/post/blur.json");
   }
}
