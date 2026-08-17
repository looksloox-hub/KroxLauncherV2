#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform float uRadius;
uniform float uSoftness;

in vec2 vUv;
out vec4 fragColor;

float roundedRectSDF(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + vec2(r);
    return length(max(q, 0.0)) - r + min(max(q.x, q.y), 0.0);
}

void main() {
    vec2 size = uResolution;
    vec2 uv = vUv * size;
    vec2 center = size * 0.5;
    vec2 halfSize = size * 0.5;
    float radius = min(uRadius, min(halfSize.x, halfSize.y));
    float dist = roundedRectSDF(uv - center, halfSize, radius);
    float alpha = 1.0 - smoothstep(0.0, max(uSoftness, 0.0001), dist);
    vec4 color = texture(uTexture, vUv);
    fragColor = vec4(color.rgb, color.a * alpha);
}
