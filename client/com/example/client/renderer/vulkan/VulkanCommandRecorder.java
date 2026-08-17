package com.example.client.renderer.vulkan;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkCommandBufferBeginInfo;
import org.lwjgl.vulkan.VkDevice;

public final class VulkanCommandRecorder {
   private final VkDevice device;
   private final long commandPool;
   private final VkCommandBuffer commandBuffer;

   public VulkanCommandRecorder(VkDevice device, long commandPool) {
      this.device = device;
      this.commandPool = commandPool;
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkCommandBufferAllocateInfo allocInfo = VkCommandBufferAllocateInfo.calloc(stack).sType$Default().commandPool(commandPool).level(0).commandBufferCount(1);
         PointerBuffer out = stack.mallocPointer(1);
         if (VK10.vkAllocateCommandBuffers(device, allocInfo, out) != 0) {
            throw new IllegalStateException("vkAllocateCommandBuffers failed");
         }

         this.commandBuffer = new VkCommandBuffer(out.get(0), device);
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

   }

   public VkCommandBuffer beginOneTime() {
      MemoryStack stack = MemoryStack.stackPush();

      VkCommandBuffer var3;
      try {
         VkCommandBufferBeginInfo begin = VkCommandBufferBeginInfo.calloc(stack).sType$Default().flags(1);
         VK10.vkResetCommandBuffer(this.commandBuffer, 0);
         if (VK10.vkBeginCommandBuffer(this.commandBuffer, begin) != 0) {
            throw new IllegalStateException("vkBeginCommandBuffer failed");
         }

         var3 = this.commandBuffer;
      } catch (Throwable var5) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (stack != null) {
         stack.close();
      }

      return var3;
   }

   public void end() {
      if (VK10.vkEndCommandBuffer(this.commandBuffer) != 0) {
         throw new IllegalStateException("vkEndCommandBuffer failed");
      }
   }

   public VkCommandBuffer buffer() {
      return this.commandBuffer;
   }

   public void destroy() {
      VK10.vkFreeCommandBuffers(this.device, this.commandPool, this.commandBuffer);
   }
}
