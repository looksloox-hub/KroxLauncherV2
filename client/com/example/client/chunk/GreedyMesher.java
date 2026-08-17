package com.example.client.chunk;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_2350.class_2352;

public final class GreedyMesher {
   private static final int SIZE = 16;

   public ChunkMesh mesh(class_1922 world, int baseX, int baseY, int baseZ) {
      List<Float> positions = new ArrayList();
      List<Float> uvs = new ArrayList();

      for(class_2350 face : class_2350.values()) {
         this.meshFace(world, baseX, baseY, baseZ, face, positions, uvs);
      }

      return new ChunkMesh(toArray(positions), toArray(uvs), positions.size() / 3);
   }

   private void meshFace(class_1922 world, int baseX, int baseY, int baseZ, class_2350 face, List<Float> positions, List<Float> uvs) {
      int axis = face.method_10166().ordinal();
      int uAxis = (axis + 1) % 3;
      int vAxis = (axis + 2) % 3;
      int sign = face.method_10171() == class_2352.field_11056 ? 1 : -1;

      for(int slice = 0; slice < 16; ++slice) {
         class_2680[][] states = new class_2680[16][16];
         boolean[][] visible = new boolean[16][16];

         for(int u = 0; u < 16; ++u) {
            for(int v = 0; v < 16; ++v) {
               int[] pos = toXYZ(baseX, baseY, baseZ, axis, slice, uAxis, u, vAxis, v);
               int x = pos[0];
               int y = pos[1];
               int z = pos[2];
               class_2680 state = world.method_8320(new class_2338(x, y, z));
               class_2680 neighbor = world.method_8320(new class_2338(x + face.method_10148(), y + face.method_10164(), z + face.method_10165()));
               if (!state.method_26215() && neighbor.method_26215()) {
                  visible[u][v] = true;
                  states[u][v] = state;
               }
            }
         }

         for(int u = 0; u < 16; ++u) {
            for(int v = 0; v < 16; ++v) {
               if (visible[u][v]) {
                  class_2680 state = states[u][v];

                  int w;
                  for(w = 1; u + w < 16 && visible[u + w][v] && state.equals(states[u + w][v]); ++w) {
                  }

                  int h;
                  label98:
                  for(h = 1; v + h < 16; ++h) {
                     for(int i = 0; i < w; ++i) {
                        if (!visible[u + i][v + h] || !state.equals(states[u + i][v + h])) {
                           break label98;
                        }
                     }
                  }

                  for(int i = 0; i < w; ++i) {
                     for(int j = 0; j < h; ++j) {
                        visible[u + i][v + j] = false;
                     }
                  }

                  emitQuad(baseX, baseY, baseZ, face, axis, slice, uAxis, u, w, vAxis, v, h, sign, positions, uvs);
               }
            }
         }
      }

   }

   private static void emitQuad(int baseX, int baseY, int baseZ, class_2350 face, int axis, int slice, int uAxis, int u, int w, int vAxis, int v, int h, int sign, List<Float> positions, List<Float> uvs) {
      float[] p0 = new float[3];
      float[] p1 = new float[3];
      float[] p2 = new float[3];
      float[] p3 = new float[3];
      fillPoint(p0, baseX, baseY, baseZ, axis, slice, uAxis, u, vAxis, v, sign);
      fillPoint(p1, baseX, baseY, baseZ, axis, slice, uAxis, u + w, vAxis, v, sign);
      fillPoint(p2, baseX, baseY, baseZ, axis, slice, uAxis, u + w, vAxis, v + h, sign);
      fillPoint(p3, baseX, baseY, baseZ, axis, slice, uAxis, u, vAxis, v + h, sign);
      if (face.method_10171() == class_2352.field_11056) {
         pushVertex(positions, uvs, p0, 0.0F, 0.0F);
         pushVertex(positions, uvs, p1, 1.0F, 0.0F);
         pushVertex(positions, uvs, p2, 1.0F, 1.0F);
         pushVertex(positions, uvs, p0, 0.0F, 0.0F);
         pushVertex(positions, uvs, p2, 1.0F, 1.0F);
         pushVertex(positions, uvs, p3, 0.0F, 1.0F);
      } else {
         pushVertex(positions, uvs, p0, 0.0F, 0.0F);
         pushVertex(positions, uvs, p3, 0.0F, 1.0F);
         pushVertex(positions, uvs, p2, 1.0F, 1.0F);
         pushVertex(positions, uvs, p0, 0.0F, 0.0F);
         pushVertex(positions, uvs, p2, 1.0F, 1.0F);
         pushVertex(positions, uvs, p1, 1.0F, 0.0F);
      }

   }

   private static void fillPoint(float[] out, int baseX, int baseY, int baseZ, int axis, int slice, int uAxis, int u, int vAxis, int v, int sign) {
      float plane = (float)slice + (sign > 0 ? 1.0F : 0.0F);
      out[0] = (float)baseX;
      out[1] = (float)baseY;
      out[2] = (float)baseZ;
      out[axis] = plane;
      out[uAxis] = (float)((uAxis == 0 ? baseX : (uAxis == 1 ? baseY : baseZ)) + u);
      out[vAxis] = (float)((vAxis == 0 ? baseX : (vAxis == 1 ? baseY : baseZ)) + v);
   }

   private static int[] toXYZ(int baseX, int baseY, int baseZ, int axis, int slice, int uAxis, int u, int vAxis, int v) {
      int[] p = new int[]{baseX, baseY, baseZ};
      p[axis] += slice;
      p[uAxis] += u;
      p[vAxis] += v;
      return p;
   }

   private static void pushVertex(List<Float> positions, List<Float> uvs, float[] p, float u, float v) {
      positions.add(p[0]);
      positions.add(p[1]);
      positions.add(p[2]);
      uvs.add(u);
      uvs.add(v);
   }

   private static float[] toArray(List<Float> list) {
      float[] out = new float[list.size()];

      for(int i = 0; i < list.size(); ++i) {
         out[i] = (Float)list.get(i);
      }

      return out;
   }
}
