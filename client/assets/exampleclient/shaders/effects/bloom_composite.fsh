#version 150 core

uniform sampler2D uTexture;
uniform sampler2D uTexture1;
uniform vec2 uResolution;
uniform float uExposure;

in vec2 vUv;
out vec4 fragColor;

void main() {
    vec4 base = texture(uTexture, vUv);
    vec4 bloom = texture(uTexture1, vUv);
    vec3 result = base.rgb + bloom.rgb * uExposure;
    fragColor = vec4(result, base.a);
}
