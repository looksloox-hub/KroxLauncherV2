package com.example.client.hud.snap;

public record SnapGuide(SnapKind kind, String sourceId, String axis, double target, double distance) {
}
