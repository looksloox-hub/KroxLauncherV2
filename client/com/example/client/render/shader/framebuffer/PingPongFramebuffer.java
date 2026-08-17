package com.example.client.render.shader.framebuffer;

public final class PingPongFramebuffer implements AutoCloseable {
   private FramebufferHandle read;
   private FramebufferHandle write;
   private final boolean withDepth;
   private final boolean linearFiltering;

   public PingPongFramebuffer(int width, int height, boolean withDepth, boolean linearFiltering) {
      this.withDepth = withDepth;
      this.linearFiltering = linearFiltering;
      this.read = new FramebufferHandle(width, height, withDepth, linearFiltering);
      this.write = new FramebufferHandle(width, height, withDepth, linearFiltering);
   }

   public FramebufferHandle read() {
      return this.read;
   }

   public FramebufferHandle write() {
      return this.write;
   }

   public void swap() {
      FramebufferHandle temp = this.read;
      this.read = this.write;
      this.write = temp;
   }

   public void resize(int width, int height) {
      this.read.resize(width, height);
      this.write.resize(width, height);
   }

   public FramebufferHandle ensureWrite(int width, int height) {
      if (this.write.width() != width || this.write.height() != height) {
         this.resize(width, height);
      }

      return this.write;
   }

   public void close() {
      this.read.close();
      this.write.close();
   }
}
