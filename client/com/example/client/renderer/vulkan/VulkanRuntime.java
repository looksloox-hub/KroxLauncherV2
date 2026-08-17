package com.example.client.renderer.vulkan;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.KHRSwapchain;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkAllocationCallbacks;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkExtent2D;
import org.lwjgl.vulkan.VkFenceCreateInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkQueryPoolCreateInfo;
import org.lwjgl.vulkan.VkQueue;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
import org.lwjgl.vulkan.VkSurfaceFormatKHR;
import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;

public final class VulkanRuntime implements AutoCloseable {
   private long windowHandle;
   private long surface;
   private VkInstance instance;
   private VkPhysicalDevice physicalDevice;
   private VkDevice device;
   private VkQueue graphicsQueue;
   private VkQueue presentQueue;
   private int graphicsQueueFamily = -1;
   private int presentQueueFamily = -1;
   private long commandPool;
   private long swapchain;
   private final List<Long> swapchainImages = new ArrayList();
   private long imageAvailableSemaphore;
   private long renderFinishedSemaphore;
   private long inFlightFence;
   private long timestampQueryPool;

   public void init(long windowHandle, int width, int height) {
      this.windowHandle = windowHandle;
      this.createInstance();
      this.createSurface();
      this.pickPhysicalDevice();
      this.createLogicalDevice();
      this.createCommandPool();
      this.createSyncObjects();
      this.createSwapchain(Math.max(1, width), Math.max(1, height));
      this.createTimestampQueryPool();
   }

   public void beginFrame() {
   }

   public void endFrame() {
   }

   public VkInstance instance() {
      return this.instance;
   }

   public VkDevice device() {
      return this.device;
   }

   public long surface() {
      return this.surface;
   }

   public long swapchain() {
      return this.swapchain;
   }

   public VkQueue graphicsQueue() {
      return this.graphicsQueue;
   }

   public VkQueue presentQueue() {
      return this.presentQueue;
   }

   public long commandPool() {
      return this.commandPool;
   }

   public long timestampQueryPool() {
      return this.timestampQueryPool;
   }

   private void createInstance() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         PointerBuffer requiredExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
         if (requiredExtensions == null) {
            throw new IllegalStateException("No Vulkan extensions");
         }

         VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack).sType$Default().pApplicationName(stack.UTF8("Optix")).applicationVersion(VK10.VK_MAKE_VERSION(1, 0, 0)).pEngineName(stack.UTF8("Optix Vulkan")).engineVersion(VK10.VK_MAKE_VERSION(1, 0, 0)).apiVersion(VK10.VK_API_VERSION_1_0);
         VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.calloc(stack).sType$Default().pApplicationInfo(appInfo).ppEnabledExtensionNames(requiredExtensions);
         PointerBuffer pInstance = stack.mallocPointer(1);
         if (VK10.vkCreateInstance(createInfo, (VkAllocationCallbacks)null, pInstance) != 0) {
            throw new IllegalStateException("vkCreateInstance failed");
         }

         this.instance = new VkInstance(pInstance.get(0), createInfo);
      } catch (Throwable var7) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (stack != null) {
         stack.close();
      }

   }

   private void createSurface() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         LongBuffer pSurface = stack.mallocLong(1);
         if (GLFWVulkan.glfwCreateWindowSurface(this.instance, this.windowHandle, (VkAllocationCallbacks)null, pSurface) != 0) {
            throw new IllegalStateException("glfwCreateWindowSurface failed");
         }

         this.surface = pSurface.get(0);
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

   }

   private void pickPhysicalDevice() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         IntBuffer count = stack.mallocInt(1);
         VK10.vkEnumeratePhysicalDevices(this.instance, count, (PointerBuffer)null);
         if (count.get(0) <= 0) {
            throw new IllegalStateException("No Vulkan GPUs found");
         }

         PointerBuffer devices = stack.mallocPointer(count.get(0));
         VK10.vkEnumeratePhysicalDevices(this.instance, count, devices);
         int i = 0;

         while(true) {
            if (i >= devices.capacity()) {
               throw new IllegalStateException("No suitable Vulkan device found");
            }

            VkPhysicalDevice candidate = new VkPhysicalDevice(devices.get(i), this.instance);
            QueueFamilies families = this.findQueueFamilies(candidate, stack);
            if (families.graphics >= 0 && families.present >= 0) {
               this.physicalDevice = candidate;
               this.graphicsQueueFamily = families.graphics;
               this.presentQueueFamily = families.present;
               break;
            }

            ++i;
         }
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

   private void createLogicalDevice() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         float priority = 1.0F;
         VkDeviceQueueCreateInfo.Buffer queueInfos;
         if (this.graphicsQueueFamily == this.presentQueueFamily) {
            queueInfos = VkDeviceQueueCreateInfo.calloc(1, stack);
            ((VkDeviceQueueCreateInfo)queueInfos.get(0)).sType$Default().queueFamilyIndex(this.graphicsQueueFamily).pQueuePriorities(stack.floats(priority));
         } else {
            queueInfos = VkDeviceQueueCreateInfo.calloc(2, stack);
            ((VkDeviceQueueCreateInfo)queueInfos.get(0)).sType$Default().queueFamilyIndex(this.graphicsQueueFamily).pQueuePriorities(stack.floats(priority));
            ((VkDeviceQueueCreateInfo)queueInfos.get(1)).sType$Default().queueFamilyIndex(this.presentQueueFamily).pQueuePriorities(stack.floats(priority));
         }

         PointerBuffer extensions = stack.pointers(stack.UTF8("VK_KHR_swapchain"));
         VkDeviceCreateInfo createInfo = VkDeviceCreateInfo.calloc(stack).sType$Default().pQueueCreateInfos(queueInfos).ppEnabledExtensionNames(extensions);
         PointerBuffer pDevice = stack.mallocPointer(1);
         if (VK10.vkCreateDevice(this.physicalDevice, createInfo, (VkAllocationCallbacks)null, pDevice) != 0) {
            throw new IllegalStateException("vkCreateDevice failed");
         }

         this.device = new VkDevice(pDevice.get(0), this.physicalDevice, createInfo, 0);
         PointerBuffer pQueue = stack.mallocPointer(1);
         VK10.vkGetDeviceQueue(this.device, this.graphicsQueueFamily, 0, pQueue);
         this.graphicsQueue = new VkQueue(pQueue.get(0), this.device);
         VK10.vkGetDeviceQueue(this.device, this.presentQueueFamily, 0, pQueue);
         this.presentQueue = new VkQueue(pQueue.get(0), this.device);
      } catch (Throwable var9) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (stack != null) {
         stack.close();
      }

   }

   private void createCommandPool() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkCommandPoolCreateInfo poolInfo = VkCommandPoolCreateInfo.calloc(stack).sType$Default().flags(2).queueFamilyIndex(this.graphicsQueueFamily);
         LongBuffer out = stack.mallocLong(1);
         if (VK10.vkCreateCommandPool(this.device, poolInfo, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("vkCreateCommandPool failed");
         }

         this.commandPool = out.get(0);
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

   }

   private void createSyncObjects() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkSemaphoreCreateInfo semInfo = VkSemaphoreCreateInfo.calloc(stack).sType$Default();
         VkFenceCreateInfo fenceInfo = VkFenceCreateInfo.calloc(stack).sType$Default().flags(1);
         LongBuffer out = stack.mallocLong(1);
         if (VK10.vkCreateSemaphore(this.device, semInfo, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("imageAvailable semaphore");
         }

         this.imageAvailableSemaphore = out.get(0);
         if (VK10.vkCreateSemaphore(this.device, semInfo, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("renderFinished semaphore");
         }

         this.renderFinishedSemaphore = out.get(0);
         if (VK10.vkCreateFence(this.device, fenceInfo, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("fence");
         }

         this.inFlightFence = out.get(0);
      } catch (Throwable var6) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (stack != null) {
         stack.close();
      }

   }

   private void createSwapchain(int width, int height) {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkSurfaceCapabilitiesKHR caps = VkSurfaceCapabilitiesKHR.calloc(stack);
         KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(this.physicalDevice, this.surface, caps);
         IntBuffer fmtCount = stack.mallocInt(1);
         KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(this.physicalDevice, this.surface, fmtCount, (VkSurfaceFormatKHR.Buffer)null);
         VkSurfaceFormatKHR.Buffer formats = VkSurfaceFormatKHR.calloc(fmtCount.get(0), stack);
         KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(this.physicalDevice, this.surface, fmtCount, formats);
         VkSurfaceFormatKHR chosenFormat = (VkSurfaceFormatKHR)formats.get(0);
         IntBuffer modeCount = stack.mallocInt(1);
         KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(this.physicalDevice, this.surface, modeCount, (IntBuffer)null);
         IntBuffer modes = stack.mallocInt(modeCount.get(0));
         KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(this.physicalDevice, this.surface, modeCount, modes);
         int presentMode = 2;

         for(int i = 0; i < modeCount.get(0); ++i) {
            if (modes.get(i) == 1) {
               presentMode = 1;
               break;
            }
         }

         int imageCount = Math.max(2, caps.minImageCount() + 1);
         if (caps.maxImageCount() > 0 && imageCount > caps.maxImageCount()) {
            imageCount = caps.maxImageCount();
         }

         VkExtent2D extent = VkExtent2D.calloc(stack).width(width).height(height);
         VkSwapchainCreateInfoKHR swapInfo = VkSwapchainCreateInfoKHR.calloc(stack).sType$Default().surface(this.surface).minImageCount(imageCount).imageFormat(chosenFormat.format()).imageColorSpace(chosenFormat.colorSpace()).imageExtent(extent).imageArrayLayers(1).imageUsage(16).imageSharingMode(this.graphicsQueueFamily == this.presentQueueFamily ? 0 : 1).pQueueFamilyIndices(this.graphicsQueueFamily == this.presentQueueFamily ? null : stack.ints(this.graphicsQueueFamily, this.presentQueueFamily)).preTransform(caps.currentTransform()).compositeAlpha(1).presentMode(presentMode).clipped(true);
         LongBuffer out = stack.mallocLong(1);
         if (KHRSwapchain.vkCreateSwapchainKHR(this.device, swapInfo, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("vkCreateSwapchainKHR failed");
         }

         this.swapchain = out.get(0);
         IntBuffer imgCount = stack.mallocInt(1);
         KHRSwapchain.vkGetSwapchainImagesKHR(this.device, this.swapchain, imgCount, (LongBuffer)null);
         LongBuffer imgs = stack.mallocLong(imgCount.get(0));
         KHRSwapchain.vkGetSwapchainImagesKHR(this.device, this.swapchain, imgCount, imgs);
         this.swapchainImages.clear();

         for(int i = 0; i < imgs.capacity(); ++i) {
            this.swapchainImages.add(imgs.get(i));
         }
      } catch (Throwable var19) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var18) {
               var19.addSuppressed(var18);
            }
         }

         throw var19;
      }

      if (stack != null) {
         stack.close();
      }

   }

   private void createTimestampQueryPool() {
      MemoryStack stack = MemoryStack.stackPush();

      try {
         VkQueryPoolCreateInfo info = VkQueryPoolCreateInfo.calloc(stack).sType$Default().queryType(2).queryCount(2);
         LongBuffer out = stack.mallocLong(1);
         if (VK10.vkCreateQueryPool(this.device, info, (VkAllocationCallbacks)null, out) != 0) {
            throw new IllegalStateException("vkCreateQueryPool failed");
         }

         this.timestampQueryPool = out.get(0);
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

   }

   private QueueFamilies findQueueFamilies(VkPhysicalDevice gpu, MemoryStack stack) {
      QueueFamilies families = new QueueFamilies();
      IntBuffer count = stack.mallocInt(1);
      VK10.vkGetPhysicalDeviceQueueFamilyProperties(gpu, count, (VkQueueFamilyProperties.Buffer)null);
      VkQueueFamilyProperties.Buffer props = VkQueueFamilyProperties.calloc(count.get(0), stack);
      VK10.vkGetPhysicalDeviceQueueFamilyProperties(gpu, count, props);

      for(int i = 0; i < props.capacity(); ++i) {
         if ((((VkQueueFamilyProperties)props.get(i)).queueFlags() & 1) != 0) {
            families.graphics = i;
         }

         IntBuffer supported = stack.mallocInt(1);
         KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(gpu, i, this.surface, supported);
         if (supported.get(0) == 1) {
            families.present = i;
         }
      }

      return families;
   }

   public void close() {
      if (this.device != null) {
         if (this.timestampQueryPool != 0L) {
            VK10.vkDestroyQueryPool(this.device, this.timestampQueryPool, (VkAllocationCallbacks)null);
         }

         if (this.renderFinishedSemaphore != 0L) {
            VK10.vkDestroySemaphore(this.device, this.renderFinishedSemaphore, (VkAllocationCallbacks)null);
         }

         if (this.imageAvailableSemaphore != 0L) {
            VK10.vkDestroySemaphore(this.device, this.imageAvailableSemaphore, (VkAllocationCallbacks)null);
         }

         if (this.inFlightFence != 0L) {
            VK10.vkDestroyFence(this.device, this.inFlightFence, (VkAllocationCallbacks)null);
         }

         if (this.commandPool != 0L) {
            VK10.vkDestroyCommandPool(this.device, this.commandPool, (VkAllocationCallbacks)null);
         }

         if (this.swapchain != 0L) {
            KHRSwapchain.vkDestroySwapchainKHR(this.device, this.swapchain, (VkAllocationCallbacks)null);
         }

         VK10.vkDestroyDevice(this.device, (VkAllocationCallbacks)null);
      }

      if (this.instance != null) {
         if (this.surface != 0L) {
            KHRSurface.vkDestroySurfaceKHR(this.instance, this.surface, (VkAllocationCallbacks)null);
         }

         VK10.vkDestroyInstance(this.instance, (VkAllocationCallbacks)null);
      }

   }

   private static final class QueueFamilies {
      int graphics = -1;
      int present = -1;
   }
}
