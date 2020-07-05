#version 130
#include "lighting.glh";

in vec2 textCoord;
in vec3 normalOut;
in vec3 worldPosOut;
in vec3 tangentOut;

out vec4 fragColor;


uniform sampler2D R_diffuse;
uniform DirectionalLight R_dirLight;


void main()
{	

	fragColor = texture(R_diffuse, textCoord.xy) * calclDirectionalLight(R_dirLight, normalize(normalOut), worldPosOut );
	//fragColor = vec4(tangentOut, 1);
}





