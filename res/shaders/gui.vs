#version 130

in vec2 position;

out vec2 texCoord0;

uniform mat4 T_transformationMatrix;
uniform mat4 T_viewMatrix;

void main()
{
	gl_Position = T_viewMatrix * T_transformationMatrix * vec4(position, 0.0, 1.0);
	texCoord0 = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}