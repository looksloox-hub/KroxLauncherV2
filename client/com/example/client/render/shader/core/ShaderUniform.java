package com.example.client.render.shader.core;

public enum ShaderUniform {
   TEX0("uTexture"),
   TEX1("uTexture1"),
   TEX2("uTexture2"),
   RESOLUTION("uResolution"),
   SIZE("uSize"),
   TIME("uTime"),
   DELTA("uDelta"),
   RADIUS("uRadius"),
   SOFTNESS("uSoftness"),
   STRENGTH("uStrength"),
   THRESHOLD("uThreshold"),
   EXPOSURE("uExposure"),
   OFFSET("uOffset"),
   COLOR_A("uColorA"),
   COLOR_B("uColorB"),
   NOISE_SCALE("uNoiseScale"),
   REFRACTION("uRefraction"),
   BLUR_SAMPLES("uBlurSamples"),
   KERNEL_INDEX("uKernelIndex"),
   DIRECTION("uDirection");

   private final String name;

   private ShaderUniform(String name) {
      this.name = name;
   }

   public String nameId() {
      return this.name;
   }

   // $FF: synthetic method
   private static ShaderUniform[] $values() {
      return new ShaderUniform[]{TEX0, TEX1, TEX2, RESOLUTION, SIZE, TIME, DELTA, RADIUS, SOFTNESS, STRENGTH, THRESHOLD, EXPOSURE, OFFSET, COLOR_A, COLOR_B, NOISE_SCALE, REFRACTION, BLUR_SAMPLES, KERNEL_INDEX, DIRECTION};
   }
}
