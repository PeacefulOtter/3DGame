#version 130

in vec2 textCoord;
in vec3 normalOut;
in vec3 worldPosOut;

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
uniform vec3 eyePos;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform float specularIntensity;
uniform float specularExponent;

uniform DirectionalLight dirLight;



vec4 calcLight( BaseLight base, vec3 direction, vec3 normal )
{
	float diffuseFactor = dot( normal, -direction );

	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);

	if (diffuseFactor > 0)
	{
		diffuseColor = vec4( base.color, 1.0 ) * base.intensity * diffuseFactor;
		
		vec3 directionToEye = normalize(eyePos - worldPosOut);
		vec3 reflectDir = normalize( reflect( direction, normal ) );

		float specularFactor = dot( directionToEye, reflectDir );
		specularFactor = pow( specularFactor, specularExponent );
		
		if (specularFactor > 0)
		{
			specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
		}
	}
	
	return diffuseColor + specularColor;
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





