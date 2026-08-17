package com.example.client.render;

public interface RenderAbstractionLayer {
   void initialize();

   void beginFrame();

   void endFrame();

   void shutdown();
}
