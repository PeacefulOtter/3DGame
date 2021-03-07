#version 130

in vec2 texCoord0;

out vec4 fragColor;

uniform sampler2D R_diffuse;

void main()
{
	fragColor = texture(R_diffuse, texCoord0);
}