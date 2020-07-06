#version 130
#include "lighting.fsh";

uniform DirectionalLight R_dirLight;


vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcDirectionalLight(R_dirLight, normal, worldPos);
}

#include "lightingMain.fsh";




