package com.example.client.ui.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1060;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_1011.class_1012;

public final class RoundedRectRenderer {
   private static final String MOD_ID = "optix";
   private static final int QUALITY_SCALE = 6;
   private static final int MAX_TEXTURE_DIMENSION = 4096;
   private static final float DEFAULT_FILL_FEATHER = 0.9F;
   private static final float DEFAULT_SHADOW_SPREAD = 8.0F;
   private static final float DEFAULT_GLOW_SPREAD = 6.0F;
   private static final Map<Key, TextureHandle> CACHE = new ConcurrentHashMap();

   private RoundedRectRenderer() {
   }

   public static void clearCache() {
      synchronized(CACHE) {
         class_310 client = class_310.method_1551();
         class_1060 textureManager = client != null ? client.method_1531() : null;

         for(TextureHandle handle : CACHE.values()) {
            if (handle != null) {
               try {
                  if (textureManager != null) {
                     textureManager.method_4615(handle.id());
                  }
               } catch (Throwable var8) {
               }

               try {
                  handle.texture().close();
               } catch (Throwable var7) {
               }
            }
         }

         CACHE.clear();
      }
   }

   public static void fill(class_332 context, float x, float y, float width, float height, int color) {
      fill(context, x, y, width, height, Math.min(width, height) * 0.25F, color);
   }

   public static void fill(class_332 context, float x, float y, float width, float height, float radius, int color) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F) && alpha(color) != 0) {
         int drawW = snapSize(width);
         int drawH = snapSize(height);
         float r = clampRadius(radius, (float)drawW, (float)drawH);
         TextureHandle handle = textureForFill(drawW, drawH, r, color);
         drawHandle(context, handle, floorPos(x), floorPos(y), drawW, drawH, color);
      }
   }

   public static void pill(class_332 context, float x, float y, float width, float height, int color) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F)) {
         fill(context, x, y, width, height, Math.min(width, height) * 0.5F, color);
      }
   }

   public static void circle(class_332 context, float centerX, float centerY, float diameter, int color) {
      if (context != null && !(diameter <= 0.0F)) {
         float r = diameter * 0.5F;
         fill(context, centerX - r, centerY - r, diameter, diameter, r, color);
      }
   }

   public static void outline(class_332 context, float x, float y, float width, float height, float radius, float thickness, int outerColor, int innerColor) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F)) {
         if (thickness <= 0.0F) {
            fill(context, x, y, width, height, radius, outerColor);
         } else {
            float outerRadius = clampRadius(radius, width, height);
            float innerX = x + thickness;
            float innerY = y + thickness;
            float innerW = width - thickness * 2.0F;
            float innerH = height - thickness * 2.0F;
            fill(context, x, y, width, height, outerRadius, outerColor);
            if (innerW > 0.0F && innerH > 0.0F) {
               float innerRadius = clampRadius(Math.max(0.0F, outerRadius - thickness), innerW, innerH);
               fill(context, innerX, innerY, innerW, innerH, innerRadius, innerColor);
            }

         }
      }
   }

   public static void pillOutline(class_332 context, float x, float y, float width, float height, float thickness, int outerColor, int innerColor) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F)) {
         outline(context, x, y, width, height, Math.min(width, height) * 0.5F, thickness, outerColor, innerColor);
      }
   }

   public static void shadow(class_332 context, float x, float y, float width, float height, float radius, int color) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F) && alpha(color) != 0) {
         int baseW = snapSize(width);
         int baseH = snapSize(height);
         float r = clampRadius(radius, (float)baseW, (float)baseH);
         TextureHandle handle = textureForShadow(baseW, baseH, r, color, 8.0F);
         drawHandle(context, handle, floorPos(x - 8.0F), floorPos(y - 8.0F), baseW, baseH, color);
      }
   }

   public static void glow(class_332 context, float x, float y, float width, float height, float radius, int color) {
      if (context != null && !(width <= 0.0F) && !(height <= 0.0F) && alpha(color) != 0) {
         int baseW = snapSize(width);
         int baseH = snapSize(height);
         float r = clampRadius(radius, (float)baseW, (float)baseH);
         TextureHandle handle = textureForGlow(baseW, baseH, r, color, 6.0F);
         drawHandle(context, handle, floorPos(x - 6.0F), floorPos(y - 6.0F), baseW, baseH, color);
      }
   }

   private static void drawHandle(class_332 context, TextureHandle handle, int x, int y, int fallbackW, int fallbackH, int fallbackColor) {
      if (handle == null) {
         context.method_25294(x, y, x + Math.max(1, fallbackW), y + Math.max(1, fallbackH), fallbackColor);
      } else {
         context.method_70845(handle.id(), x, y, x + handle.drawWidth(), y + handle.drawHeight(), 0.0F, 1.0F, 0.0F, 1.0F);
      }
   }

   private static TextureHandle textureForFill(int w, int h, float radius, int color) {
      int scale = chooseScale(w, h);
      Key key = new Key(RoundedRectRenderer.Kind.FILL, w, h, quarter(radius), color, 0, scale);
      return getOrCreate(key);
   }

   private static TextureHandle textureForShadow(int baseW, int baseH, float radius, int color, float spread) {
      int spreadQ = quarter(spread);
      int totalW = Math.max(1, Math.round((float)baseW + fromQuarter(spreadQ) * 2.0F));
      int totalH = Math.max(1, Math.round((float)baseH + fromQuarter(spreadQ) * 2.0F));
      int scale = chooseScale(totalW, totalH);
      Key key = new Key(RoundedRectRenderer.Kind.SHADOW, baseW, baseH, quarter(radius), color, spreadQ, scale);
      return getOrCreate(key);
   }

   private static TextureHandle textureForGlow(int baseW, int baseH, float radius, int color, float spread) {
      int spreadQ = quarter(spread);
      int totalW = Math.max(1, Math.round((float)baseW + fromQuarter(spreadQ) * 2.0F));
      int totalH = Math.max(1, Math.round((float)baseH + fromQuarter(spreadQ) * 2.0F));
      int scale = chooseScale(totalW, totalH);
      Key key = new Key(RoundedRectRenderer.Kind.GLOW, baseW, baseH, quarter(radius), color, spreadQ, scale);
      return getOrCreate(key);
   }

   private static TextureHandle getOrCreate(Key key) {
      TextureHandle cached = (TextureHandle)CACHE.get(key);
      if (cached != null) {
         return cached;
      } else {
         synchronized(CACHE) {
            cached = (TextureHandle)CACHE.get(key);
            if (cached != null) {
               return cached;
            } else {
               TextureHandle created = createTexture(key);
               if (created != null) {
                  CACHE.put(key, created);
               }

               return created;
            }
         }
      }
   }

   private static TextureHandle createTexture(Key key) {
      class_310 client = class_310.method_1551();
      if (client == null) {
         return null;
      } else {
         float radius = fromQuarter(key.radiusQ());
         float spread = fromQuarter(key.spreadQ());
         float logicalW = (float)key.w();
         float logicalH = (float)key.h();
         if (key.kind() == RoundedRectRenderer.Kind.SHADOW || key.kind() == RoundedRectRenderer.Kind.GLOW) {
            logicalW = (float)key.w() + spread * 2.0F;
            logicalH = (float)key.h() + spread * 2.0F;
         }

         int texW = Math.max(1, Math.round(logicalW * (float)key.scale()));
         int texH = Math.max(1, Math.round(logicalH * (float)key.scale()));
         if (texW <= 4096 && texH <= 4096) {
            class_1011 image = new class_1011(class_1012.field_4997, texW, texH, false);

            try {
               switch (key.kind().ordinal()) {
                  case 0 -> generateFill(image, texW, texH, logicalW, logicalH, radius, key.color1(), key.scale());
                  case 1 -> generateShadow(image, texW, texH, key.w(), key.h(), radius, spread, key.color1());
                  case 2 -> generateGlow(image, texW, texH, key.w(), key.h(), radius, spread, key.color1());
               }
            } catch (Throwable var17) {
               try {
                  image.close();
               } catch (Throwable var15) {
               }

               return null;
            }

            class_2960 id = textureId(key);
            class_1043 texture = new class_1043(() -> id.toString(), image);

            try {
               class_1060 textureManager = client.method_1531();
               textureManager.method_4616(id, texture);
               texture.method_4524();
            } catch (Throwable var16) {
               try {
                  client.method_1531().method_4615(id);
               } catch (Throwable var14) {
               }

               try {
                  texture.close();
               } catch (Throwable var13) {
               }

               return null;
            }

            int drawW = Math.max(1, Math.round(logicalW));
            int drawH = Math.max(1, Math.round(logicalH));
            return new TextureHandle(id, texture, drawW, drawH, texW, texH);
         } else {
            return null;
         }
      }
   }

   private static void generateFill(class_1011 image, int texW, int texH, float logicalW, float logicalH, float radius, int color, int scale) {
      for(int yy = 0; yy < texH; ++yy) {
         for(int xx = 0; xx < texW; ++xx) {
            image.method_61941(xx, yy, 0);
         }
      }

      float feather = Math.max(0.45F, 0.9F / (float)Math.max(1, scale));
      float baseA = (float)alpha(color) / 255.0F;
      int baseR = red(color);
      int baseG = green(color);
      int baseB = blue(color);

      for(int y = 0; y < texH; ++y) {
         float py = ((float)y + 0.5F) * logicalH / (float)texH;

         for(int x = 0; x < texW; ++x) {
            float px = ((float)x + 0.5F) * logicalW / (float)texW;
            float d = roundedRectDistance(px, py, 0.0F, 0.0F, logicalW, logicalH, radius);
            float coverage = 1.0F - smoothstep(0.0F, feather, d);
            if (!(coverage <= 0.0F)) {
               int a = clamp255(Math.round(255.0F * baseA * coverage));
               image.method_61941(x, y, argb(a, baseR, baseG, baseB));
            }
         }
      }

   }

   private static void generateShadow(class_1011 image, int texW, int texH, int baseW, int baseH, float radius, float spread, int color) {
      float logicalW = (float)baseW + spread * 2.0F;
      float logicalH = (float)baseH + spread * 2.0F;
      float sigma = Math.max(1.2F, spread * 0.3F);
      float baseA = (float)alpha(color) / 255.0F;
      int r = red(color);
      int g = green(color);
      int b = blue(color);

      for(int y = 0; y < texH; ++y) {
         float py = ((float)y + 0.5F) * logicalH / (float)texH;

         for(int x = 0; x < texW; ++x) {
            float px = ((float)x + 0.5F) * logicalW / (float)texW;
            float d = roundedRectDistance(px, py, spread, spread, (float)baseW, (float)baseH, radius);
            if (!(d < 0.0F)) {
               float a = (float)Math.exp((double)(-(d * d) / (2.0F * sigma * sigma)));
               a *= 0.65F;
               int alpha = clamp255(Math.round(255.0F * baseA * a));
               if (alpha > 0) {
                  image.method_61941(x, y, argb(alpha, r, g, b));
               }
            }
         }
      }

   }

   private static void generateGlow(class_1011 image, int texW, int texH, int baseW, int baseH, float radius, float spread, int color) {
      float logicalW = (float)baseW + spread * 2.0F;
      float logicalH = (float)baseH + spread * 2.0F;
      float sigma = Math.max(1.6F, spread * 0.26F);
      float baseA = (float)alpha(color) / 255.0F;
      int r = mixChannel(red(color), 255, 0.22F);
      int g = mixChannel(green(color), 255, 0.22F);
      int b = mixChannel(blue(color), 255, 0.22F);

      for(int y = 0; y < texH; ++y) {
         float py = ((float)y + 0.5F) * logicalH / (float)texH;

         for(int x = 0; x < texW; ++x) {
            float px = ((float)x + 0.5F) * logicalW / (float)texW;
            float d = roundedRectDistance(px, py, spread, spread, (float)baseW, (float)baseH, radius);
            if (!(d < 0.0F)) {
               float a = (float)Math.exp((double)(-(d * d) / (2.0F * sigma * sigma)));
               a *= 0.35F;
               int alpha = clamp255(Math.round(255.0F * baseA * a));
               if (alpha > 0) {
                  image.method_61941(x, y, argb(alpha, r, g, b));
               }
            }
         }
      }

   }

   private static class_2960 textureId(Key key) {
      String var10000 = key.kind().name().toLowerCase();
      String path = "rounded_cache/" + var10000 + "/" + Integer.toHexString(key.hashCode());
      return class_2960.method_60655("optix", path);
   }

   private static int chooseScale(int logicalW, int logicalH) {
      int max = Math.max(logicalW, logicalH);

      int scale;
      for(scale = 6; scale > 1 && max * scale > 4096; --scale) {
      }

      return Math.max(1, scale);
   }

   private static float roundedRectDistance(float px, float py, float x, float y, float width, float height, float radius) {
      float cx = x + width * 0.5F;
      float cy = y + height * 0.5F;
      float halfW = width * 0.5F;
      float halfH = height * 0.5F;
      float innerHalfW = Math.max(0.0F, halfW - radius);
      float innerHalfH = Math.max(0.0F, halfH - radius);
      float qx = Math.abs(px - cx) - innerHalfW;
      float qy = Math.abs(py - cy) - innerHalfH;
      float ax = Math.max(qx, 0.0F);
      float ay = Math.max(qy, 0.0F);
      return (float)Math.sqrt((double)(ax * ax + ay * ay)) + Math.min(Math.max(qx, qy), 0.0F) - radius;
   }

   private static float smoothstep(float edge0, float edge1, float x) {
      if (edge0 == edge1) {
         return x < edge0 ? 0.0F : 1.0F;
      } else {
         float t = clamp01((x - edge0) / (edge1 - edge0));
         return t * t * (3.0F - 2.0F * t);
      }
   }

   private static float clampRadius(float radius, float width, float height) {
      if (radius <= 0.0F) {
         return 0.0F;
      } else {
         float max = Math.max(0.0F, Math.min(width, height) * 0.5F);
         return Math.min(radius, max);
      }
   }

   private static int snapSize(float value) {
      return Math.max(1, (int)Math.ceil((double)value));
   }

   private static int floorPos(float value) {
      return (int)Math.floor((double)value);
   }

   private static int quarter(float value) {
      return Math.max(0, Math.round(value * 4.0F));
   }

   private static float fromQuarter(int valueQ) {
      return (float)valueQ / 4.0F;
   }

   private static int argb(int a, int r, int g, int b) {
      return clamp255(a) << 24 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(b);
   }

   private static int alpha(int argb) {
      return argb >>> 24 & 255;
   }

   private static int red(int argb) {
      return argb >>> 16 & 255;
   }

   private static int green(int argb) {
      return argb >>> 8 & 255;
   }

   private static int blue(int argb) {
      return argb & 255;
   }

   private static int clamp255(int value) {
      return Math.max(0, Math.min(255, value));
   }

   private static float clamp01(float value) {
      if (value <= 0.0F) {
         return 0.0F;
      } else {
         return value >= 1.0F ? 1.0F : value;
      }
   }

   private static int mixChannel(int from, int to, float t) {
      return clamp255(Math.round((float)from + (float)(to - from) * clamp01(t)));
   }

   private static enum Kind {
      FILL,
      SHADOW,
      GLOW;

      // $FF: synthetic method
      private static Kind[] $values() {
         return new Kind[]{FILL, SHADOW, GLOW};
      }
   }

   private static record Key(Kind kind, int w, int h, int radiusQ, int color1, int spreadQ, int scale) {
   }

   private static record TextureHandle(class_2960 id, class_1043 texture, int drawWidth, int drawHeight, int texWidth, int texHeight) {
   }
}
