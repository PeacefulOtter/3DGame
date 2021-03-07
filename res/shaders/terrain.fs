#version 130
#include "sampling.glh";

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in float visibility;

out vec4 fragColor;

uniform vec3 R_skyColor;
uniform vec3 C_eyePos;

uniform sampler2D R_blendMap;

uniform sampler2D R_aTexture;
// uniform sampler2D R_aNormalMap;
uniform sampler2D R_aDispMap;

uniform sampler2D R_rTexture;
// uniform sampler2D R_rNormalMap;
uniform sampler2D R_rDispMap;

uniform sampler2D R_gTexture;
// uniform sampler2D R_gNormalMap;
uniform sampler2D R_gDispMap;

uniform sampler2D R_bTexture;
// uniform sampler2D R_bNormalMap;
uniform sampler2D R_bDispMap;

uniform float dispMapScale;
uniform float dispMapBias;

void main()
{
	vec4 blendMapColor = texture(R_blendMap, texCoord0);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);

    // NORMAL
    // vec4 aNormalColor = texture(R_anormalMap, tiledCoords) * backTextureAmount;
    // vec4 rNormalColor = texture(R_rnormalMap, tiledCoords) * blendMapColor.r;
    // vec4 gNormalColor = texture(R_gnormalMap, tiledCoords) * blendMapColor.g;
    // vec4 bNormalColor = texture(R_bnormalMap, tiledCoords) * blendMapColor.b;

    vec2 tiledCoords = texCoord0 * 80.0;
    vec3 directionToEye = normalize(C_eyePos - worldPos0);
    vec2 aTexCoords = calcParallaxTexCoords(R_aDispMap, tbnMatrix, directionToEye, tiledCoords, dispMapScale, dispMapBias);
    vec2 rTexCoords = calcParallaxTexCoords(R_rDispMap, tbnMatrix, directionToEye, tiledCoords, dispMapScale, dispMapBias);
    vec2 gTexCoords = calcParallaxTexCoords(R_gDispMap, tbnMatrix, directionToEye, tiledCoords, dispMapScale, dispMapBias);
    vec2 bTexCoords = calcParallaxTexCoords(R_bDispMap, tbnMatrix, directionToEye, tiledCoords, dispMapScale, dispMapBias);

    vec4 aDispColor = texture(R_aTexture, aTexCoords) * backTextureAmount;
    vec4 rDispColor = texture(R_rTexture, rTexCoords) * blendMapColor.r;
    vec4 gDispColor = texture(R_gTexture, gTexCoords) * blendMapColor.g;
    vec4 bDispColor = texture(R_bTexture, bTexCoords) * blendMapColor.b;

    // vec4 normalColor = aNormalColor + rNormalColor + gNormalColor + bNormalColor;
    vec4 dispColor = aDispColor + rDispColor + gDispColor + bDispColor;
	fragColor = mix(vec4(R_skyColor, 1.0), dispColor, visibility);
}






