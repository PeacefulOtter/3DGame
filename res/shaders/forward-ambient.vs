#version 130

in vec3 position;
in vec2 textureCoord;

out vec2 textCoord;

uniform mat4 T_MVP;


void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	textCoord = textureCoord;
}
