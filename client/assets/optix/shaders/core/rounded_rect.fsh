#version 150

in vec4 vColor;
in vec2 vUv;

out vec4 fragColor;

uniform sampler2D DiffuseSampler;

float sdRoundRect(vec2 p, vec2 halfSize, float radius) {
    vec2 q = abs(p - halfSize) - (halfSize - vec2(radius));
    return length(max(q, vec2(0.0))) + min(max(q.x, q.y), 0.0) - radius;
}

void main() {
    vec2 size = vec2(textureSize(DiffuseSampler, 0));
    vec2 halfSize = size * 0.5;
    float radius = min(size.x, size.y) * 0.5;

    vec2 p = vUv * size;
    float dist = sdRoundRect(p, halfSize, radius);

    float aa = max(fwidth(dist), 1.0);
    float alpha = 1.0 - smoothstep(-aa, aa, dist);

    if (alpha <= 0.0) {
        discard;
    }

    fragColor = vec4(vColor.rgb, vColor.a * alpha);
}