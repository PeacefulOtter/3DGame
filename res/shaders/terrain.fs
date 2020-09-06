#version 130
#include "sampling.glh";

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in float visibility;

out vec4 fragColor;

uniform vec3 R_skyColor;
uniform vec3 C_eyePos;

uniform sampler2D R_diffuse;
uniform sampler2D R_dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

uniform sampler2D R_aTexture;
uniform sampler2D R_rTexture;
uniform sampler2D R_gTexture;
uniform sampler2D R_bTexture;
uniform sampler2D R_blendMap;

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(R_dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);

	vec4 blendMapColor = texture(R_blendMap, texCoords);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);

    vec4 aTextureColor = texture(R_aTexture, texCoords) * backTextureAmount;
    vec4 rTextureColor = texture(R_rTexture, texCoords) * blendMapColor.r;
    vec4 gTextureColor = texture(R_gTexture, texCoords) * blendMapColor.g;
    vec4 bTextureColor = texture(R_bTexture, texCoords) * blendMapColor.b;

    vec4 totalColor = aTextureColor + rTextureColor + gTextureColor + bTextureColor;

	fragColor = texture(R_diffuse, texCoords) * totalColor;
	fragColor = mix(vec4(R_skyColor, 1.0), fragColor, visibility);
}






