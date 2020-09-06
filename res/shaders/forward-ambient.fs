#version 130
#include "sampling.glh";

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in float visibility;

out vec4 fragColor;

uniform vec3 R_ambient;
uniform vec3 R_skyColor;
uniform vec3 C_eyePos;

uniform sampler2D R_diffuse;
uniform sampler2D R_dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(R_dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);
	fragColor = texture(R_diffuse, texCoords) * vec4(R_ambient, 1);
	fragColor = mix(vec4(R_skyColor, 1.0), fragColor, visibility);
}
