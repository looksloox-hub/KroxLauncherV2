package com.example.client.renderer.vulkan.pipeline;

import java.nio.LongBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkGraphicsPipelineCreateInfo;
import org.lwjgl.vulkan.VkOffset2D;
import org.lwjgl.vulkan.VkPipelineColorBlendAttachmentState;
import org.lwjgl.vulkan.VkPipelineColorBlendStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineDynamicStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineInputAssemblyStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineLayoutCreateInfo;
import org.lwjgl.vulkan.VkPipelineMultisampleStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineRasterizationStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkPipelineVertexInputStateCreateInfo;
import org.lwjgl.vulkan.VkPipelineViewportStateCreateInfo;
import org.lwjgl.vulkan.VkRect2D;
import org.lwjgl.vulkan.VkViewport;

public final class VulkanTerrainPipeline {
   private final VkDevice device;
   private long pipelineLayout;
   private long graphicsPipeline;

   public VulkanTerrainPipeline(VkDevice device) {
      this.device = device;
   }

   public void create(long renderPass, long vertShaderModule, long fragShaderModule) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkPipelineShaderStageCreateInfo.Buffer stages = VkPipelineShaderStageCreateInfo.calloc(2, stack);
         ((VkPipelineShaderStageCreateInfo)stages.get(0)).sType$Default().stage(1).module(vertShaderModule).pName(stack.UTF8("main"));
         ((VkPipelineShaderStageCreateInfo)stages.get(1)).sType$Default().stage(16).module(fragShaderModule).pName(stack.UTF8("main"));
         VkPipelineVertexInputStateCreateInfo vertexInput = VkPipelineVertexInputStateCreateInfo.calloc(stack).sType$Default();
         VkPipelineInputAssemblyStateCreateInfo inputAssembly = VkPipelineInputAssemblyStateCreateInfo.calloc(stack).sType$Default().topology(3).primitiveRestartEnable(false);
         VkViewport.Buffer viewports = VkViewport.calloc(1, stack);
         ((VkViewport)viewports.get(0)).x(0.0F).y(0.0F).width(1.0F).height(1.0F).minDepth(0.0F).maxDepth(1.0F);
         VkRect2D.Buffer scissors = VkRect2D.calloc(1, stack);
         ((VkRect2D)scissors.get(0)).offset(VkOffset2D.calloc(stack).set(0, 0)).extent(VkExtent2D.calloc(stack).set(1, 1));
         VkPipelineViewportStateCreateInfo viewportState = VkPipelineViewportStateCreateInfo.calloc(stack).sType$Default().pViewports(viewports).pScissors(scissors);
         VkPipelineRasterizationStateCreateInfo rasterizer = VkPipelineRasterizationStateCreateInfo.calloc(stack).sType$Default().depthClampEnable(false).rasterizerDiscardEnable(false).polygonMode(0).lineWidth(1.0F).cullMode(2).frontFace(0).depthBiasEnable(false);
         VkPipelineMultisampleStateCreateInfo multisampling = VkPipelineMultisampleStateCreateInfo.calloc(stack).sType$Default().sampleShadingEnable(false).rasterizationSamples(1);
         VkPipelineColorBlendAttachmentState.Buffer colorBlendAttachment = VkPipelineColorBlendAttachmentState.calloc(1, stack);
         ((VkPipelineColorBlendAttachmentState)colorBlendAttachment.get(0)).blendEnable(false).colorWriteMask(15);
         VkPipelineColorBlendStateCreateInfo colorBlending = VkPipelineColorBlendStateCreateInfo.calloc(stack).sType$Default().pAttachments(colorBlendAttachment);
         VkPipelineDynamicStateCreateInfo dynamicState = VkPipelineDynamicStateCreateInfo.calloc(stack).sType$Default().pDynamicStates(stack.ints(0, 1));
         VkPipelineLayoutCreateInfo layoutInfo = VkPipelineLayoutCreateInfo.calloc(stack).sType$Default();
         LongBuffer pLayout = stack.mallocLong(1);
         if (VK10.vkCreatePipelineLayout(this.device, layoutInfo, (VkAllocationCallbacks)null, pLayout) != 0) {
            throw new IllegalStateException("vkCreatePipelineLayout failed");
         }

         this.pipelineLayout = pLayout.get(0);
         VkGraphicsPipelineCreateInfo.Buffer pipelineInfo = VkGraphicsPipelineCreateInfo.calloc(1, stack);
         ((VkGraphicsPipelineCreateInfo)pipelineInfo.get(0)).sType$Default().pStages(stages).pVertexInputState(vertexInput).pInputAssemblyState(inputAssembly).pViewportState(viewportState).pRasterizationState(rasterizer).pMultisampleState(multisampling).pColorBlendState(colorBlending).pDynamicState(dynamicState).layout(this.pipelineLayout).renderPass(renderPass).subpass(0);
         LongBuffer pPipeline = stack.mallocLong(1);
         if (VK10.vkCreateGraphicsPipelines(this.device, 0L, pipelineInfo, (VkAllocationCallbacks)null, pPipeline) != 0) {
            throw new IllegalStateException("vkCreateGraphicsPipelines failed");
         }

         this.graphicsPipeline = pPipeline.get(0);
      } catch (Throwable var24) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var23) {
               var24.addSuppressed(var23);
            }
         }

         throw var24;
      }

      if (stack != null) {
         stack.close();
      }

   }

   public long pipeline() {
      return this.graphicsPipeline;
   }

   public long layout() {
      return this.pipelineLayout;
   }

   public void destroy() {
      if (this.graphicsPipeline != 0L) {
         VK10.vkDestroyPipeline(this.device, this.graphicsPipeline, (VkAllocationCallbacks)null);
      }

      if (this.pipelineLayout != 0L) {
         VK10.vkDestroyPipelineLayout(this.device, this.pipelineLayout, (VkAllocationCallbacks)null);
      }

   }
}
