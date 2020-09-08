#version 130

in vec3 texCoord0;
out vec4 fragColor;

uniform samplerCube R_cubeMap;

void main()
{
    fragColor = texture(R_cubeMap, texCoord0);
}
