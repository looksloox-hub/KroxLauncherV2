#version 150 core

in vec2 Position;
in vec2 UV0;

out vec2 vUv;

void main() {
    vUv = UV0;
    gl_Position = vec4(Position, 0.0, 1.0);
}
