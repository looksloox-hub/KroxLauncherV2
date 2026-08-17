package com.example.client.render.shader.framebuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class FramebufferHandle implements AutoCloseable {
   private int framebufferId;
   private int colorTextureId;
   private int depthStencilRenderbufferId;
   private int width;
   private int height;
   private final boolean withDepth;
   private final boolean linearFiltering;

   public FramebufferHandle(int width, int height, boolean withDepth, boolean linearFiltering) {
      this.withDepth = withDepth;
      this.linearFiltering = linearFiltering;
      this.resize(width, height);
   }

   public int framebufferId() {
      return this.framebufferId;
   }

   public int colorTextureId() {
      return this.colorTextureId;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   public boolean withDepth() {
      return this.withDepth;
   }

   public boolean linearFiltering() {
      return this.linearFiltering;
   }

   public void resize(int width, int height) {
      if (width > 0 && height > 0) {
         this.width = width;
         this.height = height;
         this.destroyAttachments();
         this.framebufferId = GL30.glGenFramebuffers();
         GL30.glBindFramebuffer(36160, this.framebufferId);
         this.colorTextureId = GL11.glGenTextures();
         GL11.glBindTexture(3553, this.colorTextureId);
         GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 32993, 5121, 0L);
         int filter = this.linearFiltering ? 9729 : 9728;
         GL11.glTexParameteri(3553, 10241, filter);
         GL11.glTexParameteri(3553, 10240, filter);
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
         GL30.glFramebufferTexture2D(36160, 36064, 3553, this.colorTextureId, 0);
         if (this.withDepth) {
            this.depthStencilRenderbufferId = GL30.glGenRenderbuffers();
            GL30.glBindRenderbuffer(36161, this.depthStencilRenderbufferId);
            GL30.glRenderbufferStorage(36161, 35056, width, height);
            GL30.glFramebufferRenderbuffer(36160, 33306, 36161, this.depthStencilRenderbufferId);
         }

         int status = GL30.glCheckFramebufferStatus(36160);
         if (status != 36053) {
            GL30.glBindFramebuffer(36160, 0);
            throw new IllegalStateException("Incomplete framebuffer: 0x" + Integer.toHexString(status));
         } else {
            GL30.glBindFramebuffer(36160, 0);
            GL11.glBindTexture(3553, 0);
            GL30.glBindRenderbuffer(36161, 0);
         }
      } else {
         throw new IllegalArgumentException("Framebuffer dimensions must be positive");
      }
   }

   public void bind() {
      GL30.glBindFramebuffer(36160, this.framebufferId);
      GL11.glViewport(0, 0, this.width, this.height);
   }

   public static void unbind(int viewportWidth, int viewportHeight) {
      GL30.glBindFramebuffer(36160, 0);
      GL11.glViewport(0, 0, viewportWidth, viewportHeight);
   }

   public void clear(float red, float green, float blue, float alpha) {
      this.bind();
      GL11.glClearColor(red, green, blue, alpha);
      GL11.glClear(16384 | (this.withDepth ? 256 : 0));
   }

   public void blitTo(FramebufferHandle destination) {
      GL30.glBindFramebuffer(36008, this.framebufferId);
      GL30.glBindFramebuffer(36009, destination.framebufferId);
      GL30.glBlitFramebuffer(0, 0, this.width, this.height, 0, 0, destination.width, destination.height, 16384, 9728);
      GL30.glBindFramebuffer(36160, 0);
   }

   private void destroyAttachments() {
      if (this.colorTextureId != 0) {
         GL11.glDeleteTextures(this.colorTextureId);
         this.colorTextureId = 0;
      }

      if (this.depthStencilRenderbufferId != 0) {
         GL30.glDeleteRenderbuffers(this.depthStencilRenderbufferId);
         this.depthStencilRenderbufferId = 0;
      }

      if (this.framebufferId != 0) {
         GL30.glDeleteFramebuffers(this.framebufferId);
         this.framebufferId = 0;
      }

   }

   public void close() {
      this.destroyAttachments();
   }
}
