
in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;
in float visibility;

out vec4 fragColor;

uniform sampler2D R_diffuse;
uniform sampler2D R_normalMap;
uniform sampler2D R_dispMap;

uniform float dispMapScale;
uniform float dispMapBias;

uniform vec3 R_skyColor;

#include "lighting.glh";
