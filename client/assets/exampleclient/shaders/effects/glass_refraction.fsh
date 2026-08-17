#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform float uRefraction;
uniform float uNoiseScale;
uniform float uStrength;
uniform float uTime;

in vec2 vUv;
out vec4 fragColor;

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

void main() {
    vec2 texel = 1.0 / max(uResolution, vec2(1.0));
    vec2 noiseUV = vUv * uNoiseScale + vec2(uTime * 0.05, uTime * 0.08);
    float n1 = hash(noiseUV);
    float n2 = hash(noiseUV + 19.19);
    vec2 offset = (vec2(n1, n2) - 0.5) * uRefraction * uStrength * texel * 30.0;
    vec4 base = texture(uTexture, vUv + offset);
    fragColor = vec4(base.rgb, base.a);
}
