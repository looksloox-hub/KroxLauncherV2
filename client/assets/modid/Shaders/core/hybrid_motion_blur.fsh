#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform float MotionX;
uniform float MotionY;
uniform float Strength;

in vec2 texCoord;
out vec4 fragColor;

float linearizeDepth(float depth) {
    return depth;
}

void main() {

    float depth = texture(DepthSampler, texCoord).r;
    vec2 motion = vec2(MotionX, MotionY) * Strength;

    // depth-based scaling (far objects smear more)
    float depthFactor = smoothstep(0.2, 1.0, depth);

    vec2 offset = motion * depthFactor;

    vec4 color = vec4(0.0);

    // multi-tap reprojection blur
    color += texture(DiffuseSampler, texCoord);
    color += texture(DiffuseSampler, texCoord + offset * 0.25);
    color += texture(DiffuseSampler, texCoord + offset * 0.5);
    color += texture(DiffuseSampler, texCoord + offset * 0.75);
    color += texture(DiffuseSampler, texCoord + offset);

    fragColor = color / 5.0;
}