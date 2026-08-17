package com.example.client.hud;

@FunctionalInterface
public interface HudElementFactory<T extends HudElement> {
   T create(String var1);
}
