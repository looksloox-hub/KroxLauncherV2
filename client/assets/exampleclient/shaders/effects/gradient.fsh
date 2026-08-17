#version 150 core

uniform vec2 uResolution;
uniform vec4 uColorA;
uniform vec4 uColorB;
uniform float uAngle;

in vec2 vUv;
out vec4 fragColor;

void main() {
    vec2 dir = vec2(cos(uAngle), sin(uAngle));
    float t = clamp(dot(vUv - vec2(0.5), dir) + 0.5, 0.0, 1.0);
    fragColor = mix(uColorA, uColorB, t);
}
