package com.example.client.mixin;

import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import net.minecraft.class_6379;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_437.class})
public interface ScreenAccessor {
   @Invoker("clearChildren")
   void optix$clearChildren();

   @Invoker("addDrawable")
   <T extends class_4068> T optix$addDrawable(T var1);

   @Invoker("addDrawableChild")
   <T extends class_364 & class_4068 & class_6379> T optix$addDrawableChild(T var1);
}
