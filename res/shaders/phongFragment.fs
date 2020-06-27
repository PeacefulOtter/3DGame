#version 130

const int MAX_POINT_LIGHTS = 4;
const int MAX_SPOT_LIGHTS = 4;

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

// Quadratic equation which describes how quickly a light fades out
struct Attenuation
{
	float constant;
	float linear;
	float exponent;
};

struct PointLight
{
	BaseLight base;
	Attenuation attenuation;
	vec3 position;
	float range;
};


struct SpotLight
{
	PointLight pointLight;
	vec3 direction;
	float cutoff;
};



uniform vec3 baseColor;
uniform vec3 eyePos;
uniform vec3 ambientLight;
uniform sampler2D sampler;

uniform float specularIntensity;
uniform float specularExponent;

uniform DirectionalLight dirLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];


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


vec4 calcPointLight( PointLight pointLight, vec3 normal )
{
	vec3 lightDirection = worldPosOut - pointLight.position;
	float distanceToPoint = length( lightDirection );

	if (distanceToPoint > pointLight.range)
		return vec4(0, 0, 0, 0);

	lightDirection = normalize( lightDirection );

	vec4 color = calcLight( pointLight.base, lightDirection, normal );
	float attenuation = pointLight.attenuation.constant + 
			    pointLight.attenuation.linear * distanceToPoint + 
			    pointLight.attenuation.exponent * distanceToPoint * distanceToPoint +
			    0.0001;
	
	return color / attenuation;
}


vec4 calcSpotLight( SpotLight spotLight, vec3 normal )
{
	vec3 lightDir = normalize( worldPosOut - spotLight.pointLight.position );
	float spotFactor = dot( lightDir, spotLight.direction );

	vec4 color = vec4( 0, 0, 0, 0 );
	
	if (spotFactor > spotLight.cutoff)
	{
		color = calcPointLight( spotLight.pointLight, normal ) *
			(1.0 - (1.0 - spotFactor) / (1.0 - spotLight.cutoff) );
	}

	return color;
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

	for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		if (pointLights[i].base.intensity > 0)
			totalLight += calcPointLight( pointLights[i], normal );

	for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
		if (spotLights[i].pointLight.base.intensity > 0)
			totalLight += calcSpotLight( spotLights[i], normal );

	fragColor = color * totalLight;	
}





