#version 150 core

uniform sampler2D uTexture;
uniform vec2 uResolution;

in vec2 vUv;
out vec4 fragColor;

void main() {
    fragColor = texture(uTexture, vUv);
}
