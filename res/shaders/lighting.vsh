in vec3 position;
in vec2 texCoord;
in vec3 normal;
in vec3 tangent;

out vec2 texCoord0;
out vec3 worldPos0;
out mat3 tbnMatrix;
out float visibility;

uniform mat4 T_transformationMatrix;
uniform mat4 T_projectionMatrix;
uniform mat4 T_viewMatrix;

// const float density = 0.0005;
const float density = 0.005;
const float gradient = 1.2;

void main()
{
    vec4 worldPosition = T_transformationMatrix * vec4(position, 1.0);
    vec4 positionRelativeToCam = T_viewMatrix * worldPosition;
    gl_Position = T_projectionMatrix * positionRelativeToCam;

    texCoord0 = texCoord;
    worldPos0 = (T_transformationMatrix * vec4(position, 1.0)).xyz;
    
    vec3 n = normalize((T_transformationMatrix * vec4(normal, 0.0)).xyz);
    vec3 t = normalize((T_transformationMatrix * vec4(tangent, 0.0)).xyz);
    t = normalize(t - dot(t, n) * n);
    vec3 b = cross(t, n);
    tbnMatrix = mat3(t, b, n);

    float distance = length( positionRelativeToCam.xyz );
    visibility = exp(-pow((distance*density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
