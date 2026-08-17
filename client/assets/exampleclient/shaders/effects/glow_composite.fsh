#version 150 core

uniform sampler2D uTexture;
uniform sampler2D uTexture1;
uniform vec2 uResolution;
uniform float uStrength;
uniform vec4 uGlowColor;

in vec2 vUv;
out vec4 fragColor;

void main() {
    vec4 base = texture(uTexture, vUv);
    vec4 blur = texture(uTexture1, vUv);
    vec3 glow = blur.rgb * uGlowColor.rgb * uStrength;
    fragColor = vec4(base.rgb + glow, base.a);
}
