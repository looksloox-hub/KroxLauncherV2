#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;
uniform float uThreshold;

in vec2 vUv;
out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vUv);
    float brightness = max(max(color.r, color.g), color.b);
    if (brightness <= uThreshold) {
        fragColor = vec4(0.0);
    } else {
        fragColor = color;
    }
}
