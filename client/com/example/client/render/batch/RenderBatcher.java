package com.example.client.render.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RenderBatcher {
   private final Map<BatchKey, List<ChunkDrawCommand>> batches = new HashMap();

   public void submit(BatchKey key, ChunkDrawCommand command) {
      ((List)this.batches.computeIfAbsent(key, (k) -> new ArrayList())).add(command);
   }

   public List<ChunkDrawCommand> drain(BatchKey key) {
      List<ChunkDrawCommand> out = (List)this.batches.remove(key);
      return out == null ? List.of() : out;
   }

   public void clear() {
      this.batches.clear();
   }

   public static record BatchKey(int materialId, int layer, int shaderId) {
   }
}
