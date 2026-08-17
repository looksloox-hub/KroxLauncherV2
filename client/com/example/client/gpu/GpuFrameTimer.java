package com.example.client.gpu;

import com.example.client.renderer.vulkan.VulkanRuntime;
import org.lwjgl.vulkan.VkCommandBuffer;

public final class GpuFrameTimer {
   private final VulkanRuntime vk;
   private long lastGpuFrameNanos = -1L;

   public GpuFrameTimer(VulkanRuntime vk) {
      this.vk = vk;
   }

   public void writeBegin(VkCommandBuffer commandBuffer) {
   }

   public void writeEnd(VkCommandBuffer commandBuffer) {
   }

   public long pollGpuFrameNanos() {
      return this.lastGpuFrameNanos;
   }
}
