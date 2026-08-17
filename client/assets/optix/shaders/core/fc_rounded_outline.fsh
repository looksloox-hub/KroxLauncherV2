#version 330

layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};

in vec2 localPos;
in vec4 vertexColor;
in float vRadius;

out vec4 fragColor;

const float THICKNESS = 1.5;

float sdRoundBox(vec2 p, vec2 b, float r) {
    vec2 q = abs(p) - b + r;
    return min(max(q.x, q.y), 0.0) + length(max(q, vec2(0.0))) - r;
}

void main() {
    vec2 grad = vec2(length(vec2(dFdx(localPos.x), dFdy(localPos.x))),
                     length(vec2(dFdx(localPos.y), dFdy(localPos.y))));
    vec2 he = vec2(1.0 / max(grad.x, 1e-6), 1.0 / max(grad.y, 1e-6));
    vec2 p = localPos * he;
    float r = clamp(vRadius, 0.0, min(he.x, he.y));

    float d = sdRoundBox(p, he, r);
    float aa = max(fwidth(d), 1e-4);

    float outer = 1.0 - smoothstep(-aa, aa, d);
    float inner = 1.0 - smoothstep(-aa, aa, d + THICKNESS);
    float alpha = clamp(outer - inner, 0.0, 1.0);

    vec4 color = vertexColor;
    color.a *= alpha;
    if (color.a < 0.001) {
        discard;
    }
    fragColor = color * ColorModulator;
}
