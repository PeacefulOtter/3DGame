#include "sampling.glh";

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPosOut);
	vec2 texCoords = calcParallaxTextCoord(R_dispMap, tbnMatrix, directionToEye, texCoordOut, dispMapScale, dispMapBias); 
	
	vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture(R_normalMap, texCoords).xyz - 1));
    	fragColor = texture(R_diffuse, texCoords) * calcLightingEffect(normal, worldPosOut);
}
