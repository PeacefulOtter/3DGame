#version 130

in vec2 position;

out vec2 texCoord0;

// uniform mat4 T_transformationMatrix;

void main()
{
	gl_Position = vec4(position, 0.0, 1.0);
	texCoord0 = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}