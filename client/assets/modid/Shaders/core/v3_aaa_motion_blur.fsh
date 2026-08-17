#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform float MotionX;
uniform float MotionY;
uniform float Strength;

in vec2 texCoord;
out vec4 fragColor;

void main() {

    vec2 motion = vec2(MotionX, MotionY) * Strength;

    float depth = texture(DepthSampler, texCoord).r;

    // depth-based separation (foreground protected)
    float depthFactor = smoothstep(0.15, 1.0, depth);

    vec2 offset = motion * depthFactor;

    vec4 color = vec4(0.0);

    // =============================
    // AAA temporal reprojection blur
    // =============================

    color += texture(DiffuseSampler, texCoord) * 0.30;
    color += texture(DiffuseSampler, texCoord + offset * 0.25) * 0.22;
    color += texture(DiffuseSampler, texCoord + offset * 0.50) * 0.18;
    color += texture(DiffuseSampler, texCoord + offset * 0.75) * 0.15;
    color += texture(DiffuseSampler, texCoord + offset) * 0.15;

    fragColor = color;
}