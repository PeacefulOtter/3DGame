#version 130

in vec3 position;
out vec3 texCoord0;

uniform mat4 T_projectionMatrix;
uniform mat4 T_viewMatrix;

void main()
{
	gl_Position = T_projectionMatrix * T_viewMatrix * vec4(position, 1.0);
	texCoord0 = position;
}