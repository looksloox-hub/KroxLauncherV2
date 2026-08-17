package com.example.client.renderer.vulkan.command;

import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;

public final class VulkanCommandRecorder {
   private final VkDevice device;

   public VulkanCommandRecorder(VkDevice device) {
      this.device = device;
   }

   public void recordTerrainDraw(VkCommandBuffer cmd, long pipeline, long vertexBuffer, long indexBuffer, int indexCount) {
      VK10.vkCmdBindPipeline(cmd, 0, pipeline);
      MemoryStack stack = MemoryStack.stackPush();

      try {
         LongBuffer vertexBuffers = stack.longs(vertexBuffer);
         LongBuffer offsets = stack.longs(0L);
         VK10.vkCmdBindVertexBuffers(cmd, 0, vertexBuffers, offsets);
      } catch (Throwable var13) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var12) {
               var13.addSuppressed(var12);
            }
         }

         throw var13;
      }

      if (stack != null) {
         stack.close();
      }

      VK10.vkCmdBindIndexBuffer(cmd, indexBuffer, 0L, 1);
      VK10.vkCmdDrawIndexed(cmd, indexCount, 1, 0, 0, 0);
   }
}
