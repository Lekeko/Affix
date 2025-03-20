#include veil:deferred_utils

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform float GameTime;
uniform vec3 heartPos;
uniform vec3 color;
uniform float timerOffset;
uniform float leftHanded;

in vec2 texCoord;
out vec4 fragColor;

vec2 boxIntersection(in vec3 ro, in vec3 rd, vec3 boxSize, out vec3 outNormal) {
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

    outNormal = (tN > 0.0) ? step(vec3(tN), t1) : step(t2, vec3(tF));
    outNormal *= -sign(rd);

    return vec2(tN, tF);
}

mat3 rotatorUD(float angle){
    return mat3(
    1.0, 0.0, 0.0,
    0.0, cos(angle), sin(angle),
    0.0 , sin(angle),  -cos(angle)

    );
}

mat3 rotatorLR(float angle){
    return mat3(
    cos(angle), 0.0, sin(angle),
    0.0, 1.0, 0.0,
    sin(angle) , 0.0,  -cos(angle)

    );
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


void main() {
    float speed = GameTime * 2000;
    vec4 baseColor = texture(DiffuseSampler0, texCoord);
    float depthSample = texture(DiffuseDepthSampler, texCoord).r;
    float worldDepth = depthSampleToWorldDepth(depthSample);

    vec3 viewPos = viewPosFromDepthSample(depthSample, texCoord);
    vec3 rayDir = viewDirFromUv(texCoord) * rotatorUD(speed) * rotatorLR(speed);


    vec3 camPos = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;


    vec3 handOffset = vec3(0,0,0);
    vec3 direction = vec3(-VeilCamera.IViewMat[2][0] + handOffset.x, -VeilCamera.IViewMat[2][1], -VeilCamera.IViewMat[2][2] + handOffset.z) * rotatorUD(speed) * rotatorLR(speed);
    vec3 offsetDistance = vec3(3,3,3);
    vec3 posWorld = direction * offsetDistance + camPos;

    vec3 boxPosition = camPos - posWorld;


    vec3 iDontKnowWhatTheFuckIsANormal;

    float size = 1.0f;
    vec2 rayHitSmall = boxIntersection(boxPosition, rayDir, vec3(size, size, size), iDontKnowWhatTheFuckIsANormal);
    vec2 rayHitLarge = boxIntersection(boxPosition, rayDir, vec3(size * 1.1, size * 1.1, size * 1.1), iDontKnowWhatTheFuckIsANormal);

    bool hitSmall = rayHitSmall.x > 0.0;
    bool hitLarge = rayHitLarge.x > -1.0;

    float average = (baseColor.x + baseColor.y + baseColor.z) / 3;
    average = average > 0.5 ? average * 0.7 : average * 0.5;

    if (hitSmall) {
        fragColor = vec4(1 - vec3(worldDepth) * 0.022, 1);
    } else if (hitLarge) {
        fragColor = vec4(vec3(vec3(worldDepth) * 0.022)*1.3  , 1.0);
    }
    else {
        fragColor = vec4(vec3(baseColor), 1.0);
    }
}