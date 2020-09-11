#version 130

in vec3 texCoord0;
out vec4 fragColor;

uniform vec3 R_fogColor;
uniform samplerCube R_diffuse;

const float lowerLimit = 0.0;
const float upperLimit = 100.0;

void main()
{
    float factor = (texCoord0.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);

    fragColor = mix(vec4(R_fogColor, 1.0), texture(R_diffuse, texCoord0), factor);
}
