#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform float uTime;
uniform float uNoiseScale;
uniform float uStrength;

in vec2 vUv;
out vec4 fragColor;

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

void main() {
    vec4 color = texture(uTexture, vUv);
    float n = hash(floor(vUv * uResolution * uNoiseScale) + vec2(uTime * 60.0, uTime * 37.0));
    float noise = (n - 0.5) * 2.0 * uStrength;
    fragColor = vec4(color.rgb + noise, color.a);
}
