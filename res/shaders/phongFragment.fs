#version 130

in vec2 textCoord;
in vec3 normalOut;

out vec4 fragColor;

struct BaseLight
{
	vec3 color;
	float intensity;
};

struct DirectionalLight
{
	BaseLight base;
	vec3 direction;
};



uniform vec3 baseColor;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform DirectionalLight dirLight;



vec4 calcLight( BaseLight base, vec3 direction, vec3 normal )
{
	float diffuseFactor = dot( normal, -direction );
	vec4 diffuseColor = vec4(0, 0, 0, 0);

	if (diffuseFactor > 0)
	{
		diffuseColor = vec4( base.color, 1.0 ) * base.intensity * diffuseFactor;
	}
	
	return diffuseColor;
}

vec4 calclDirectionalLight( DirectionalLight dirLight, vec3 normal )
{
	return calcLight( dirLight.base, -dirLight.direction, normal );
}


void main()
{	
	vec4 totalLight = vec4( ambientLight, 1 );

	vec4 color = vec4(baseColor, 1);

	vec4 textureColor = texture(sampler, textCoord.xy);

	if (textureColor != vec4(0, 0, 0, 0))
		color *= textureColor;
		
	
	vec3 normal = normalize( normalOut );

	totalLight += calclDirectionalLight( dirLight, normal );	

	fragColor = color * totalLight;	
}





