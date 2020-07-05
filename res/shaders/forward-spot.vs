#version 130

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 textCoord;
out vec3 normalOut;
out vec3 worldPosOut;

uniform mat4 T_model;
uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4( position, 1.0 );
	textCoord = textureCoord;
	normalOut = ( T_model * vec4( normal, 0.0 ) ).xyz;
	worldPosOut = ( T_model * vec4( position, 1.0 ) ).xyz;
}
