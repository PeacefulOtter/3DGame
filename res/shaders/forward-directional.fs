#version 130
#include "lighting.glh";

in vec2 textCoord;
in vec3 normalOut;
in vec3 worldPosOut;

out vec4 fragColor;


uniform sampler2D R_diffuse;
uniform DirectionalLight R_dirLight;




void main()
{	

	fragColor = texture(R_diffuse, textCoord.xy) * calclDirectionalLight(R_dirLight, normalize(normalOut), worldPosOut );	
}





