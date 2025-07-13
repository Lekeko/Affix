#include veil:space_helper
#define M_PI 3.1415926535897932384626433832795 // big ass

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform vec3 pos;
uniform float timer;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

float sphIntersect(vec3 ro, vec3 rd, vec4 sph){
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w*sph.w;
    float h = b*b - c;
    if( h<0.0 ){
        return - 1.0;
    }
    h = sqrt( h );
    return -b - h;
}
float depthSampleToWorldDepth(float depthSample) {
    float f = depthSample * 2.0 - 1.0;
    return 2.0 * VeilCamera.NearPlane * VeilCamera.FarPlane / (VeilCamera.FarPlane + VeilCamera.NearPlane - f * (VeilCamera.FarPlane - VeilCamera.NearPlane));
}

vec3 viewPosFromDepth(float depth, vec2 uv) {
    float z = depth * 2.0 - 1.0;

    vec4 positionCS = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}

vec3 viewDirFromUv(vec2 uv) {
    return (VeilCamera.IViewMat * vec4(normalize(viewPosFromDepth(1.0, uv)), 0.0)).xyz;
}

vec3 viewPosFromDepthSample(float depth, vec2 uv) {
    vec4 positionCS = vec4(uv, depth, 1.0) * 2.0 - 1.0;
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;

    return positionVS.xyz;
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sphereRadius = sin(min(timer / 20, M_PI/2)) * 14;
    float depthSample = texture(DiffuseDepthSampler, texCoord).r;

    vec3 sceneWorldPos = screenToWorldSpace(texCoord, depthSample).xyz;

    float distanceToSphereCenter = distance(sceneWorldPos, pos);

    if (distanceToSphereCenter <= sphereRadius) {
        fragColor = vec4(baseColor / (sphereRadius-distanceToSphereCenter), 1.0);
    } else {
        fragColor = vec4(baseColor, 1.0);
    }

}

