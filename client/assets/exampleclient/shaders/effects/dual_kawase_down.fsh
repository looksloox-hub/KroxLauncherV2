#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform float uOffset;

in vec2 vUv;
out vec4 fragColor;

void main() {
    vec2 texel = 1.0 / max(uResolution, vec2(1.0));
    float o = max(uOffset, 0.5);
    vec4 color = vec4(0.0);
    color += texture(uTexture, vUv + texel * vec2(-o, -o));
    color += texture(uTexture, vUv + texel * vec2( o, -o));
    color += texture(uTexture, vUv + texel * vec2(-o,  o));
    color += texture(uTexture, vUv + texel * vec2( o,  o));
    fragColor = color * 0.25;
}
