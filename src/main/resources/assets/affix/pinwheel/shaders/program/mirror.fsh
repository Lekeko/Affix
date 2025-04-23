#include veil:deferred_utils

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform float GameTime;
uniform float mirrorOffset;

uniform float PI = 3.141592653;

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


float gouIntersect( in vec3 ro, in vec3 rd, in float ka, float kb )
{
    float po = 1.0;
    vec3 rd2 = rd*rd; vec3 rd3 = rd2*rd;
    vec3 ro2 = ro*ro; vec3 ro3 = ro2*ro;
    float k4 = dot(rd2,rd2);
    float k3 = dot(ro ,rd3);
    float k2 = dot(ro2,rd2) - kb/6.0;
    float k1 = dot(ro3,rd ) - kb*dot(rd,ro)/2.0;
    float k0 = dot(ro2,ro2) + ka - kb*dot(ro,ro);
    k3 /= k4;
    k2 /= k4;
    k1 /= k4;
    k0 /= k4;
    float c2 = k2 - k3*(k3);
    float c1 = k1 + k3*(2.0*k3*k3-3.0*k2);
    float c0 = k0 + k3*(k3*(c2+k2)*3.0-4.0*k1);

    if( abs(c1) < 0.1*abs(c2) )
    {
        po = -1.0;
        float tmp=k1; k1=k3; k3=tmp;
        k0 = 1.0/k0;
        k1 = k1*k0;
        k2 = k2*k0;
        k3 = k3*k0;
        c2 = k2 - k3*(k3);
        c1 = k1 + k3*(2.0*k3*k3-3.0*k2);
        c0 = k0 + k3*(k3*(c2+k2)*3.0-4.0*k1);
    }

    c0 /= 3.0;
    float Q = c2*c2 + c0;
    float R = c2*c2*c2 - 3.0*c0*c2 + c1*c1;
    float h = R*R - Q*Q*Q;

    if( h>0.0 )
    {
        h = sqrt(h);
        float s = sign(R+h)*pow(abs(R+h),1.0/3.0);
        float u = sign(R-h)*pow(abs(R-h),1.0/3.0);
        float x = s+u+4.0*c2;
        float y = s-u;
        float ks = x*x + y*y*3.0;
        float k = sqrt(ks);
        float t = -0.5*po*abs(y)*sqrt(6.0/(k+x)) - 2.0*c1*(k+x)/(ks+x*k) - k3;
        return (po<0.0)?1.0/t:t;
    }

    float sQ = sqrt(Q);
    float w = sQ*cos(acos(-R/(sQ*Q))/3.0);
    float d2 = -w - c2;
    if( d2<0.0 ) return -1.0;
    float d1 = sqrt(d2);
    float h1 = sqrt(w - 2.0*c2 + c1/d1);
    float h2 = sqrt(w - 2.0*c2 - c1/d1);
    float t1 = -d1 - h1 - k3; t1 = (po<0.0)?1.0/t1:t1;
    float t2 = -d1 + h1 - k3; t2 = (po<0.0)?1.0/t2:t2;
    float t3 =  d1 - h2 - k3; t3 = (po<0.0)?1.0/t3:t3;
    float t4 =  d1 + h2 - k3; t4 = (po<0.0)?1.0/t4:t4;
    float t = 1e20;
    if( t1>0.0 ) t=t1;
    if( t2>0.0 ) t=min(t,t2);
    if( t3>0.0 ) t=min(t,t3);
    if( t4>0.0 ) t=min(t,t4);
    return t;
}
float sph4Intersect( in vec3 ro, in vec3 rd, in float ra )
{
    float r2 = ra*ra;
    vec3 d2 = rd*rd; vec3 d3 = d2*rd;
    vec3 o2 = ro*ro; vec3 o3 = o2*ro;
    float ka = 1.0/dot(d2,d2);
    float k3 = ka* dot(ro,d3);
    float k2 = ka* dot(o2,d2);
    float k1 = ka* dot(o3,rd);
    float k0 = ka*(dot(o2,o2) - r2*r2);
    float c2 = k2 - k3*k3;
    float c1 = k1 + 2.0*k3*k3*k3 - 3.0*k3*k2;
    float c0 = k0 - 3.0*k3*k3*k3*k3 + 6.0*k3*k3*k2 - 4.0*k3*k1;
    float p = c2*c2 + c0/3.0;
    float q = c2*c2*c2 - c2*c0 + c1*c1;
    float h = q*q - p*p*p;
    if( h<0.0 ) return -1.0;
    float sh = sqrt(h);
    float s = sign(q+sh)*pow(abs(q+sh),1.0/3.0);
    float t = sign(q-sh)*pow(abs(q-sh),1.0/3.0);
    vec2  w = vec2( s+t,s-t );
    vec2  v = vec2( w.x+c2*4.0, w.y*sqrt(3.0) )*0.5;
    float r = length(v);
    return -abs(v.y)/sqrt(r+v.x) - c1/r - k3;
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




void main() {
    float speed = GameTime * 1400;
    vec4 baseColor = texture(DiffuseSampler0, texCoord);
    float depthSample = texture(DiffuseDepthSampler, texCoord).r;
    float worldDepth = depthSampleToWorldDepth(depthSample) ;

    float LR =  worldDepth;
    float UD = cos(speed);
    float maybe = -sin(speed);

    vec3 viewPos = viewPosFromDepthSample(depthSample, texCoord);
    vec3 rayDir = viewDirFromUv(texCoord) * rotatorLR(LR) * rotatorUD(UD) * rotatorMaybe(maybe);



    vec3 camPos = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;


    vec3 handOffset = vec3(0,mirrorOffset,0);
    vec3 direction = vec3(
    -VeilCamera.IViewMat[2][0] + handOffset.x,
    -VeilCamera.IViewMat[2][1],
    -VeilCamera.IViewMat[2][2] + handOffset.z)

    * rotatorLR(LR) * rotatorUD(UD) * rotatorMaybe(maybe);

    vec3 offsetDistance = vec3(3,3,3);
    vec3 posWorld = direction * offsetDistance + camPos;

    vec3 boxPosition = camPos - posWorld;



    vec3 size = vec3(0.5, 0.5, 0.5) + sin(speed) * 2 * atan(mirrorOffset);
    float rayHitSmall = sph4Intersect(boxPosition, rayDir, size.x );
    float rayHitLarge = sph4Intersect(boxPosition, rayDir,  size.x * 1.22);

    bool hitSmall = rayHitSmall > 0.0;
    bool hitLarge = rayHitLarge > 0.0;


    if (hitSmall) {
        fragColor = vec4(1 - vec3(worldDepth) * 0.022, 1);
    } else if (hitLarge) {
        fragColor = vec4(vec3(vec3(worldDepth) * 0.022)*1.3  , 1.0);
    }
    else {
        fragColor = vec4(vec3(baseColor * (1 - mirrorOffset * 1.06) ), 1.0);
    }
}