#version 130

in vec2 textCoord;

out vec4 fragColor;

uniform vec3 color;
uniform sampler2D sampler;

void main()
{
	fragColor = texture(sampler, textCoord.xy) * vec4(color, 0); 
}
