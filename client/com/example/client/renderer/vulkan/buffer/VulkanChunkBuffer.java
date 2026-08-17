package com.example.client.renderer.vulkan.buffer;

import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkBufferCreateInfo;
import org.lwjgl.vulkan.VkDevice;

public final class VulkanChunkBuffer {
   private final VkDevice device;
   private long vertexBuffer;
   private long indexBuffer;
   private long vertexMemory;
   private long indexMemory;

   public VulkanChunkBuffer(VkDevice device) {
      this.device = device;
   }

   public void create(long vertexSize, long indexSize) {
      this.createVertexBuffer(vertexSize);
      this.createIndexBuffer(indexSize);
   }

   private void createVertexBuffer(long size) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkBufferCreateInfo info = VkBufferCreateInfo.calloc(stack).sType(12).size(size).usage(130).sharingMode(0);
         LongBuffer pBuffer = stack.mallocLong(1);
         int err = VK10.vkCreateBuffer(this.device, info, (VkAllocationCallbacks)null, pBuffer);
         if (err != 0) {
            throw new RuntimeException("Failed to create vertex buffer: " + err);
         }

         this.vertexBuffer = pBuffer.get(0);
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

   private void createIndexBuffer(long size) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkBufferCreateInfo info = VkBufferCreateInfo.calloc(stack).sType(12).size(size).usage(66).sharingMode(0);
         LongBuffer pBuffer = stack.mallocLong(1);
         int err = VK10.vkCreateBuffer(this.device, info, (VkAllocationCallbacks)null, pBuffer);
         if (err != 0) {
            throw new RuntimeException("Failed to create index buffer: " + err);
         }

         this.indexBuffer = pBuffer.get(0);
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

   public long vertexBuffer() {
      return this.vertexBuffer;
   }

   public long indexBuffer() {
      return this.indexBuffer;
   }

   public void destroy() {
      if (this.vertexBuffer != 0L) {
         VK10.vkDestroyBuffer(this.device, this.vertexBuffer, (VkAllocationCallbacks)null);
      }

      if (this.indexBuffer != 0L) {
         VK10.vkDestroyBuffer(this.device, this.indexBuffer, (VkAllocationCallbacks)null);
      }

   }
}
