#version 130

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 textCoord;
out vec3 normalOut;
out vec3 worldPosOut;

uniform mat4 transform;
uniform mat4 transformProjected;

void main()
{
	gl_Position = transformProjected * vec4( position, 1.0 );
	textCoord = textureCoord;
	normalOut = ( transform * vec4( normal, 0.0 ) ).xyz;
	worldPosOut = ( transform * vec4( position, 1.0 ) ).xyz;
}
