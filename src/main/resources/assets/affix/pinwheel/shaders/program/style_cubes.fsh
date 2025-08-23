#include veil:space_helper
#define M_PI 3.1415926535897932384626433832795 // big ass

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NoiseTexture;
uniform sampler2D DistortTexture;

uniform float GameTime;
uniform float cubeCount;
uniform vec3 primaryColor;
uniform vec3 secondaryColor;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;
uniform float amp;

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

float random (vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))*
    43758.5453123);
}

mat3 rotatorUD(float angle){
    return mat3(
    1.0, 0.0, 0.0,
    0.0, cos(angle), sin(angle),
    0.0 , sin(angle),  -cos(angle)

    );
}

mat3 rotatorMaybe(float angle){
    return mat3(
    cos(angle), sin(angle), 0,
    sin(angle),  -cos(angle), 0,
    0 ,0 ,1
    );
}

mat3 rotatorLR(float angle){
    return mat3(
    cos(angle), 0.0, sin(angle),
    0.0, 1.0, 0.0,
    sin(angle) , 0.0,  -cos(angle)

    );
}

vec2 boxIntersection(in vec3 ro, in vec3 rd, vec3 boxSize) {
    vec3 m = 1.0 / rd;
    vec3 n = m * ro;
    vec3 k = abs(m) * boxSize;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max(max(t1.x, t1.y), t1.z);
    float tF = min(min(t2.x, t2.y), t2.z);

    if (tN > tF || tF < 0.0) {
        return vec2(-1.0);
    }

    return vec2(tN, tF);
}


void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;

    if (sceneDepthSample < 1.0) {
        fragColor.xyz = baseColor;
        return; //this just optimized this shit by 40 % wtf
    }

    float speed = GameTime  * 300;

    vec3 rayOrigin = VeilCamera.CameraPosition;
    vec3 rayDir = viewDirFromUv(texCoord) * rotatorUD(speed) * rotatorMaybe(speed) * rotatorLR(speed);


    vec3 clusterOrigin = VeilCamera.CameraBobOffset + rayOrigin;
    float clusterSize = cubeCount;

    vec3 colorBig = primaryColor/255;
    vec3 colorSmall = secondaryColor * (1+amp* 2)/255;

    float dispersion = 1000;

    vec3 finalColor = vec3(0,0,0);

    for (float i = 1; i <= clusterSize; ++i) {

        float boxSize = 1 + amp * 40 - random(vec2(i, -i)) * 2;

        vec3 posOffset = (vec3(
            random(vec2(i/clusterSize, 1)),
            random(vec2(i/clusterSize, 2)),
            random(vec2(i/clusterSize, 3))
        ) - 0.5) * dispersion * rotatorUD(speed * 1.04) * rotatorMaybe(speed * (int(i) % 2 == 0 ? 1 : -1)) * rotatorLR(speed);

        float rayHitSmall = boxIntersection(posOffset , rayDir , vec3(boxSize-0.7)).x;
        float rayHitBig = boxIntersection(posOffset, rayDir, vec3(boxSize)).x;


        if (rayHitSmall > 1.0){
            finalColor = vec3(colorSmall);
        }
        else if (rayHitBig > 1.0){
            finalColor = vec3(colorBig);
        }

    }


    finalColor += baseColor;



    fragColor.xyz = finalColor;

}