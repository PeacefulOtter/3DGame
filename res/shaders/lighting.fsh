in vec2 texCoordOut;
in vec3 worldPosOut;
in mat3 tbnMatrix;

out vec4 fragColor;

uniform sampler2D R_diffuse;
uniform sampler2D R_normalMap;
uniform sampler2D R_dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

#include "lighting.glh";
