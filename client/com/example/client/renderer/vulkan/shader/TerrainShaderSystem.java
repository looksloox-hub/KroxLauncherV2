package com.example.client.renderer.vulkan.shader;

import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;

public final class TerrainShaderSystem {
   private final VkDevice device;

   public TerrainShaderSystem(VkDevice device) {
      this.device = device;
   }

   public long createShaderModule(byte[] spirv) {
      MemoryStack stack = MemoryStack.stackPush();

      long var5;
      try {
         VkShaderModuleCreateInfo info = VkShaderModuleCreateInfo.calloc(stack).sType$Default().pCode(stack.bytes(spirv));
         LongBuffer out = stack.mallocLong(1);
         if (VK10.vkCreateShaderModule(this.device, info, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("vkCreateShaderModule failed");
         }

         var5 = out.get(0);
      } catch (Throwable var8) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (stack != null) {
         stack.close();
      }

      return var5;
   }
}
