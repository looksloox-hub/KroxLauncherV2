package com.example.client.renderer.vulkan;

public record VulkanFrame(long commandBuffer, long renderFence, long imageAvailableSemaphore, long renderFinishedSemaphore) {
}
