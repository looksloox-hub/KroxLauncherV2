package com.example.client.render.shader.pipeline;

import com.example.client.render.shader.framebuffer.FramebufferHandle;

public interface ShaderPass {
   void apply(ShaderContext var1, FramebufferHandle var2, FramebufferHandle var3);
}
