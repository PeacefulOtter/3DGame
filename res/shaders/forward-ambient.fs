#version 130

in vec2 textCoord;

out vec4 fragColor;

uniform vec3 R_ambient;
uniform sampler2D R_diffuse;

void main()
{		
	fragColor = texture(R_diffuse, textCoord.xy) * vec4(R_ambient, 1); 
}
