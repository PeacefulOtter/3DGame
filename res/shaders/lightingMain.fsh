#include "sampling.glh";

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(R_dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);
	
	vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture(R_normalMap, texCoords).xyz - 1));
	fragColor = texture(R_diffuse, texCoords) * calcLightingEffect(normal, worldPos0);
}
