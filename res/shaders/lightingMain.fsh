#include "sampling.glh";

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = calcParallaxTexCoords(R_dispMap, tbnMatrix, directionToEye, texCoord0, dispMapScale, dispMapBias);
	
	vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture(R_normalMap, texCoords).xyz - 1));

	vec4 textureColor = texture(R_diffuse, texCoords);
	if ( textureColor.a < 0.5 )
	{
	    discard;
	}
	fragColor = textureColor * calcLightingEffect(normal, worldPos0);
	fragColor = mix(vec4(R_skyColor, 1.0), fragColor, visibility);
}
