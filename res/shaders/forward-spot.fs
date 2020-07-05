#version 130
#include "lighting.glh";

in vec2 textCoord;
in vec3 normalOut;
in vec3 worldPosOut;

out vec4 fragColor;


uniform sampler2D diffuse;
uniform SpotLight spotLight;


void main()
{	
	fragColor = texture(diffuse, textCoord.xy) * calcSpotLight(spotLight, normalize(normalOut), worldPosOut );	
}





