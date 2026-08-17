#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform vec2 uDirection;
uniform float uRadius;
uniform float uSoftness;

in vec2 vUv;
out vec4 fragColor;

float gaussian(float x, float sigma) {
    return exp(-(x * x) / (2.0 * sigma * sigma));
}

void main() {
    vec2 texel = 1.0 / max(uResolution, vec2(1.0));
    vec2 dir = normalize(max(abs(uDirection), vec2(0.0001))) * texel;
    float radius = max(uRadius, 1.0);
    float sigma = max(uSoftness * radius, 0.5);
    vec4 sum = vec4(0.0);
    float weightSum = 0.0;
    int samples = int(clamp(radius, 1.0, 32.0));
    for (int i = -32; i <= 32; i++) {
        if (abs(i) > samples) continue;
        float w = gaussian(float(i), sigma);
        vec2 uv = vUv + dir * float(i) * radius;
        sum += texture(uTexture, uv) * w;
        weightSum += w;
    }
    fragColor = sum / max(weightSum, 0.0001);
}
