#version 130
#include "lighting.glh";

in vec2 textCoord;
in vec3 normalOut;
in vec3 worldPosOut;

out vec4 fragColor;


uniform sampler2D R_diffuse;
uniform SpotLight R_spotLight;


void main()
{	
	fragColor = texture(R_diffuse, textCoord.xy) * calcSpotLight(R_spotLight, normalize(normalOut), worldPosOut );	
}





