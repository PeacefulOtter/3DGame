#version 130
#include "sampling.glh";

in vec2 texCoordOut;
in vec3 worldPosOut;
in mat3 tbnMatrix;

out vec4 fragColor;

uniform vec3 R_ambient;
uniform vec3 C_eyePos;

uniform sampler2D R_diffuse;
uniform sampler2D R_dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPosOut);
	vec2 texCoords = calcParallaxTextCoord(R_dispMap, tbnMatrix, directionToEye, texCoordOut, dispMapScale, dispMapBias); 

	fragColor = texture(R_diffuse, texCoords) * vec4(R_ambient, 1); 
}
