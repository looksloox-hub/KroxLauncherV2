package com.example.client.mixin;

import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_310.class})
public interface MinecraftClientAccessor {
   @Accessor("session")
   @Mutable
   void setSession(class_320 var1);

   @Accessor("worldRenderer")
   class_761 optix$getWorldRenderer();

   @Accessor("worldRenderer")
   class_761 getWorldRenderer();
}
