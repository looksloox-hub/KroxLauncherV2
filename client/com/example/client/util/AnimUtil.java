package com.example.client.util;

public class AnimUtil {
   public static float smooth(float current, float target, float speed) {
      return current + (target - current) * speed;
   }
}
