package com.example.client.renderer.renderpass;

import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkAttachmentDescription;
import org.lwjgl.vulkan.VkAttachmentReference;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkRenderPassCreateInfo;
import org.lwjgl.vulkan.VkSubpassDescription;

public final class TerrainRenderPass {
   private final VkDevice device;
   private long renderPass;

   public TerrainRenderPass(VkDevice device) {
      this.device = device;
   }

   public void create(int colorFormat) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkAttachmentDescription.Buffer attachments = VkAttachmentDescription.calloc(1, stack);
         ((VkAttachmentDescription)attachments.get(0)).format(colorFormat).samples(1).loadOp(1).storeOp(0).stencilLoadOp(2).stencilStoreOp(1).initialLayout(0).finalLayout(1000001002);
         VkAttachmentReference.Buffer colorRef = VkAttachmentReference.calloc(1, stack);
         ((VkAttachmentReference)colorRef.get(0)).attachment(0).layout(2);
         VkSubpassDescription.Buffer subpass = VkSubpassDescription.calloc(1, stack);
         ((VkSubpassDescription)subpass.get(0)).pipelineBindPoint(0).colorAttachmentCount(1).pColorAttachments(colorRef);
         VkRenderPassCreateInfo renderPassInfo = VkRenderPassCreateInfo.calloc(stack).sType(38).pAttachments(attachments).pSubpasses(subpass);
         LongBuffer pRenderPass = stack.mallocLong(1);
         int err = VK10.vkCreateRenderPass(this.device, renderPassInfo, (VkAllocationCallbacks)null, pRenderPass);
         if (err != 0) {
            throw new RuntimeException("Failed to create terrain render pass: " + err);
         }

         this.renderPass = pRenderPass.get(0);
      } catch (Throwable var10) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }
         }

         throw var10;
      }

      if (stack != null) {
         stack.close();
      }

   }

   public long handle() {
      return this.renderPass;
   }

   public void destroy() {
      if (this.renderPass != 0L) {
         VK10.vkDestroyRenderPass(this.device, this.renderPass, (VkAllocationCallbacks)null);
         this.renderPass = 0L;
      }

   }
}
