#version 130

in vec2 texCoord0;

out vec4 fragColor;

uniform sampler2D R_diffuse;

void main()
{
    vec4 textureColor = texture(R_diffuse, texCoord0);
	if ( textureColor.a < 0.5 )
	{
	    discard;
	}
	fragColor = textureColor;
}