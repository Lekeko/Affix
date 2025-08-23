#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NoiseTexture;
uniform sampler2D DistortTexture;

uniform float GameTime;
uniform vec3 primaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float amp;

float sphIntersect(vec3 ro, vec3 rd, vec4 sph){
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w* sph.w;
    float h = b*b - c;
    if( h<0.0 ){
        return -1.0;
    }
    return -b + sqrt(h);
}

vec3 viewPosFromDepth(float depth, vec2 uv) {
    float z = depth * 2.0 - 1.0;
    vec4 positionCS = vec4(uv * 2.0 - 1.0, z, 1.0);
    vec4 positionVS = VeilCamera.IProjMat * positionCS;
    positionVS /= positionVS.w;
    return positionVS.xyz;
}

vec3 viewDirFromUv(vec2 uv) {
    vec3 pointOnFarPlaneVS = viewPosFromDepth(1.0, uv);
    return (VeilCamera.IViewMat * vec4(normalize(pointOnFarPlaneVS), 0.0)).xyz;
}

float atan2(float y, float x){
    if (y > 0.0){
        return atan(y, x) + PI / 2;
    }else{
        return PI - atan(y, -x) + PI / 2;
    }
}

vec2 sphereTexture(vec3 coord){
    return vec2(
        atan2(coord.x, coord.z) / (PI * 2),
        acos(coord.y) / PI + (GameTime * 222)
    );
}
float random (vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))*
    43758.5453123);
}

void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;

    if (sceneDepthSample < 1.0) {
        fragColor.xyz = baseColor;
        return;
    }

    vec3 sceneWorldPos = screenToWorldSpace(texCoord, sceneDepthSample).xyz;

    vec3 rayOrigin = VeilCamera.CameraPosition ;
    vec3 rayDir = viewDirFromUv(texCoord) ;
    float speed = GameTime  * 1000;

    vec3 spherePos = VeilCamera.CameraBobOffset + VeilCamera.CameraPosition;
    float sphereRadius = 1000.1;

    float rayHit = sphIntersect(VeilCamera.CameraPosition, rayDir, vec4(rayOrigin, sphereRadius));



    fragColor.xyz = baseColor;


    if (rayHit > 0.0) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHit;
        float hitY = hitPointWorld.y - spherePos.y;

        float slice = (hitY + sphereRadius) / ((1 + amp) * sphereRadius);
        vec3 normal = normalize(hitPointWorld - spherePos);
        vec2 sphereTexture = sphereTexture(normal);
        vec4 actualSphereTexture = texture(DistortTexture, sphereTexture * 2);



        float sphereAlpha = 1 - slice;

        fragColor.xyz = mix(baseColor, primaryColor/255,  clamp(sphereAlpha, 0, 1));
    }
}