in vec3 position;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;

out vec2 texCoordOut;
out vec3 worldPosOut;
out mat3 tbnMatrix;

uniform mat4 T_model;
uniform mat4 T_MVP;

void main()
{
	gl_Position = T_MVP * vec4(position, 1.0);
	texCoordOut = texCoord;
	worldPosOut = (T_model * vec4(position, 1.0)).xyz;


    	vec3 n = normalize((T_model * vec4(normal, 0.0)).xyz);
    	vec3 t = normalize((T_model * vec4(tangent, 0.0)).xyz);
    	t = normalize(t - dot(t, n) * n);

    	vec3 biTangent = cross(t, n);
    	tbnMatrix = mat3(t, biTangent, n);
}
