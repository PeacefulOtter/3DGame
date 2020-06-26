#version 130

in vec3 position;
in vec2 textureCoord;

out vec2 textCoord;

uniform mat4 transform;


void main()
{
	gl_Position = transform * vec4(position, 1.0);
	textCoord = textureCoord;
}
