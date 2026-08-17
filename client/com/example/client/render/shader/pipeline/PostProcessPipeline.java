package com.example.client.render.shader.pipeline;

import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.framebuffer.FramebufferPool;
import com.example.client.render.shader.framebuffer.PingPongFramebuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class PostProcessPipeline implements AutoCloseable {
   private final List<ShaderPass> passes = new ArrayList();
   private final FramebufferPool framebufferPool;
   private PingPongFramebuffer pingPong;
   private int width;
   private int height;

   public PostProcessPipeline(FramebufferPool framebufferPool, int width, int height) {
      this.framebufferPool = (FramebufferPool)Objects.requireNonNull(framebufferPool, "framebufferPool");
      this.resize(width, height);
   }

   public void resize(int width, int height) {
      this.width = width;
      this.height = height;
      if (this.pingPong != null) {
         this.pingPong.close();
      }

      this.pingPong = new PingPongFramebuffer(width, height, false, true);
   }

   public void addPass(ShaderPass pass) {
      this.passes.add((ShaderPass)Objects.requireNonNull(pass, "pass"));
   }

   public void clearPasses() {
      this.passes.clear();
   }

   public FramebufferHandle render(ShaderContext context, FramebufferHandle input) {
      if (context.viewportWidth() != this.width || context.viewportHeight() != this.height) {
         this.resize(context.viewportWidth(), context.viewportHeight());
      }

      if (this.passes.isEmpty()) {
         return input;
      } else {
         FramebufferHandle currentInput = input;

         for(int i = 0; i < this.passes.size(); ++i) {
            ShaderPass pass = (ShaderPass)this.passes.get(i);
            FramebufferHandle currentOutput = i == this.passes.size() - 1 ? this.pingPong.write() : this.pingPong.write();
            currentOutput.resize(this.width, this.height);
            pass.apply(context, currentInput, currentOutput);
            currentInput = currentOutput;
            this.pingPong.swap();
         }

         return currentInput;
      }
   }

   public FramebufferPool framebufferPool() {
      return this.framebufferPool;
   }

   public void close() {
      if (this.pingPong != null) {
         this.pingPong.close();
         this.pingPong = null;
      }

      this.passes.clear();
   }
}
