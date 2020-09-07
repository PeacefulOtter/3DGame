#version 130

in vec2 texCoord0;

out vec4 out_Color;

uniform sampler2D R_diffuse;

void main()
{
	out_Color = texture(R_diffuse,texCoord0);
}