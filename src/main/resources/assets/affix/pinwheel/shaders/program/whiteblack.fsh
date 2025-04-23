#include veil:deferred_utils

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform float GameTime;
uniform float transition;

uniform float PI = 3.141592653;

in vec2 texCoord;
out vec4 fragColor;

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

vec2 sphIntersect( in vec3 ro, in vec3 rd, in vec3 ce, float ra )
{
    vec3 oc = ro - ce;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - ra*ra;
    float h = b*b - c;
    if( h<0.0 ) return vec2(-1.0);
    h = sqrt( h );
    return vec2( -b-h, -b+h );
}
vec2 boxIntersection1(in vec3 ro, in vec3 rd, vec3 boxSize, out vec3 outNormal) {
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

float hash21(vec2 p) {
    p = fract(p * vec2(11231.34, 416.78));
    p += dot(p, p + 3.45);
    return fract(p.x * p.y);
}

void main() {
    vec4 baseColor = texture(DiffuseSampler0, texCoord);
    float depthSample = texture(DiffuseDepthSampler, texCoord).r;
    float worldDepth = depthSampleToWorldDepth(depthSample) ;



    float t = 1.0 - transition;
    float power = 2.50;

    float easedT = pow(t, power);
    float easedTransition = 1.0 - easedT;
    float distanceFactor = 221 / transition;

    vec3 color1 = baseColor.xyz;
    vec3 color2 = vec3(worldDepth / (211 * (1-transition)));
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    float speed = GameTime * 1000;

    float LR =  worldDepth;
    float UD = cos(speed);
    float maybe = sin(speed);

    vec3 viewPos = viewPosFromDepthSample(depthSample, texCoord);
    vec3 rayDir = viewDirFromUv(texCoord)* rotatorLR(LR) * rotatorUD(UD) * rotatorMaybe(maybe);

    fragColor = vec4(mix(color1, (1-color2 )/ 3, easedTransition), 1.1);
}