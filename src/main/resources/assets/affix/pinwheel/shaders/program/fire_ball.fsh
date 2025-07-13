#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NoiseTexture;
uniform sampler2D DistortTexture;

uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

uniform float PI = 3.14159265359;

float sphIntersect(vec3 ro, vec3 rd, vec4 sph){
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w* sph.w;
    float h = b*b - c;
    if( h<0.0 ){
        return -1.0;
    }
    return -b - sqrt(h);
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
        acos(coord.y) / PI + (GameTime * 512)
    );
}

vec2 boxIntersection( in vec3 ro, in vec3 rd, vec3 boxSize,  vec3 outNormal )
{
    vec3 m = 1.0/rd; // can precompute if traversing a set of aligned boxes
    vec3 n = m*ro;   // can precompute if traversing a set of aligned boxes
    vec3 k = abs(m)*boxSize;
    vec3 t1 = -n - k;
    vec3 t2 = -n + k;
    float tN = max( max( t1.x, t1.y ), t1.z );
    float tF = min( min( t2.x, t2.y ), t2.z );
    if( tN>tF || tF<0.0) return vec2(-1.0); // no intersection
    outNormal = (tN>0.0) ? step(vec3(tN),t1) : step(t2,vec3(tF));  // ro inside the box
    outNormal *= -sign(rd);
    return vec2( tN, tF );
}

vec2 eliIntersect( in vec3 ro, in vec3 rd, in vec3 ra )
{
    vec3 ocn = ro/ra;
    vec3 rdn = rd/ra;
    float a = dot( rdn, rdn );
    float b = dot( ocn, rdn );
    float c = dot( ocn, ocn );
    float h = b*b - a*(c-1.0);
    if( h<0.0 ) return vec2(-1.0); //no intersection
    h = sqrt(h);
    return vec2(-b-h,-b+h)/a;
}


void main() {
    vec3 baseColor = texture(DiffuseSampler0, texCoord).rgb;
    float sceneDepthSample = texture(DiffuseDepthSampler, texCoord).x;


    vec2 st = texCoord.xy;
    st.x /= VeilCamera.ProjMat[0][0] / VeilCamera.ProjMat[1][1];

    float distort = texture(NoiseTexture, st).a ;

    vec3 sceneWorldPos = screenToWorldSpace(texCoord, sceneDepthSample).xyz;

    vec3 rayOrigin = VeilCamera.CameraPosition;
    vec3 rayDir = viewDirFromUv(texCoord);
    float speed = GameTime  * 1000;

    vec3 spherePos = vec3(0.0, 200 + sin(speed) * 1010, 0.0);
    float sphereRadius = 50.0;

    float rayHit = boxIntersection(rayOrigin - spherePos, rayDir, vec3(10, 70, 10), vec3(0)).x;

    float distToScene = length(sceneWorldPos - rayOrigin);

    fragColor.xyz = baseColor;


    if (rayHit > 0.0 && rayHit < distToScene) {
        vec3 hitPointWorld = rayOrigin + rayDir * rayHit;
        float hitY = hitPointWorld.y - spherePos.y;
        float normalizedHeight = (hitY + sphereRadius) / ((3 + sin(speed * 1)) / 2 * sphereRadius);
        normalizedHeight = clamp(normalizedHeight, 0.0, 1.0);
        vec3 normal = normalize(hitPointWorld - spherePos);
        vec2 sphereTexture = sphereTexture(normal);
        vec4 actualSphereTexture = texture(DistortTexture, sphereTexture * 1);


        float sphereAlpha = 0.6 +  - normalizedHeight;

        fragColor.xyz = mix(baseColor, actualSphereTexture.xyz * 4 + vec3(1,0,0) * 6,  clamp(sphereAlpha, 0, 1));
    }
}