package com.example.client.render.shader.engine;

import com.example.client.render.shader.core.ShaderManager;
import com.example.client.render.shader.effect.BloomPass;
import com.example.client.render.shader.effect.CopyPass;
import com.example.client.render.shader.effect.DualKawaseBlurPass;
import com.example.client.render.shader.effect.GaussianBlurPass;
import com.example.client.render.shader.effect.GlassRefractionPass;
import com.example.client.render.shader.effect.GlowPass;
import com.example.client.render.shader.effect.GradientPass;
import com.example.client.render.shader.effect.KawaseBlurPass;
import com.example.client.render.shader.effect.NoisePass;
import com.example.client.render.shader.effect.RoundedMaskPass;
import com.example.client.render.shader.framebuffer.FramebufferHandle;
import com.example.client.render.shader.framebuffer.FramebufferPool;
import com.example.client.render.shader.pipeline.PostProcessPipeline;
import com.example.client.render.shader.pipeline.ShaderContext;
import java.util.Objects;

public final class ShaderEngine implements AutoCloseable {
   private final ShaderEngineConfig config;
   private final ShaderManager shaderManager;
   private final FramebufferPool framebufferPool;
   private final PostProcessPipeline pipeline;

   public ShaderEngine(int viewportWidth, int viewportHeight, ShaderEngineConfig config) {
      this.config = (ShaderEngineConfig)Objects.requireNonNull(config, "config");
      this.shaderManager = new ShaderManager(config.externalShaderRoot());
      this.framebufferPool = new FramebufferPool();
      this.pipeline = new PostProcessPipeline(this.framebufferPool, viewportWidth, viewportHeight);
   }

   public ShaderManager shaderManager() {
      return this.shaderManager;
   }

   public FramebufferPool framebufferPool() {
      return this.framebufferPool;
   }

   public PostProcessPipeline pipeline() {
      return this.pipeline;
   }

   public ShaderContext context(float timeSeconds, float deltaSeconds, int width, int height) {
      return new ShaderContext(timeSeconds, deltaSeconds, width, height, this.config.defaultScale());
   }

   public CopyPass copy() {
      return new CopyPass(this.shaderManager);
   }

   public GaussianBlurPass gaussianBlur(int iterations, float radius, float softness) {
      return new GaussianBlurPass(this.shaderManager, iterations, radius, softness);
   }

   public KawaseBlurPass kawaseBlur(int iterations, float offset) {
      return new KawaseBlurPass(this.shaderManager, iterations, offset);
   }

   public DualKawaseBlurPass dualKawaseBlur(int iterations, float baseOffset) {
      return new DualKawaseBlurPass(this.shaderManager, iterations, baseOffset);
   }

   public BloomPass bloom(int blurIterations, float blurRadius, float threshold, float exposure) {
      return new BloomPass(this.shaderManager, blurIterations, blurRadius, threshold, exposure);
   }

   public GlowPass glow(int blurIterations, float blurRadius, float strength, float r, float g, float b, float a) {
      return new GlowPass(this.shaderManager, blurIterations, blurRadius, strength, r, g, b, a);
   }

   public RoundedMaskPass roundedMask(float radius, float softness) {
      return new RoundedMaskPass(this.shaderManager, radius, softness);
   }

   public GradientPass gradient(float[] colorA, float[] colorB, float angle) {
      return new GradientPass(this.shaderManager, colorA, colorB, angle);
   }

   public NoisePass noise(float intensity, float scale) {
      return new NoisePass(this.shaderManager, intensity, scale);
   }

   public GlassRefractionPass glass(float refraction, float noiseScale, float distortion) {
      return new GlassRefractionPass(this.shaderManager, refraction, noiseScale, distortion);
   }

   public FramebufferHandle renderThroughPipeline(ShaderContext context, FramebufferHandle input) {
      return this.pipeline.render(context, input);
   }

   public void close() {
      this.pipeline.close();
      this.framebufferPool.close();
      this.shaderManager.close();
   }
}
