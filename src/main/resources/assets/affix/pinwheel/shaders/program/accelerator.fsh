#include veil:deferred_utils

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform float GameTime;

uniform float tranzitioner;

in vec2 texCoord;
out vec4 fragColor;

uniform float outlineThreshold = 0.1;
uniform float outlineSmoothness = 0.05;

float plot(vec2 st) {
    return smoothstep(tranzitioner * 2.4, tranzitioner* 2.4 -0.5, abs(st.y));
}

void main() {
    float speed = GameTime * 1400;
    vec4 baseColor = texture(DiffuseSampler0, texCoord);
    vec3 outlineColor = vec3(1.0, 1.0, 1.0);
    vec2 texelSize = 0.5 / vec2(textureSize(DiffuseDepthSampler, 0)) ;
    float pula = 1.00202;

    float depthL = texture(DiffuseDepthSampler, texCoord - vec2(texelSize.x, 0.0)).r / pula;
    float depthR = texture(DiffuseDepthSampler, texCoord + vec2(texelSize.x, 0.0)).r / pula;
    float depthUp = texture(DiffuseDepthSampler, texCoord + vec2(0.0, texelSize.y)).r / pula;
    float depthDown = texture(DiffuseDepthSampler, texCoord - vec2(0.0, texelSize.y)).r / pula;

    float worldDepthL = depthSampleToWorldDepth(depthL);
    float worldDepthR = depthSampleToWorldDepth(depthR);
    float worldDepthUp = depthSampleToWorldDepth(depthUp);
    float worldDepthDown = depthSampleToWorldDepth(depthDown);

    float deltaX = abs(worldDepthR - worldDepthL);
    float deltaY = abs(worldDepthUp - worldDepthDown);

    float edgeStrength = length(vec2(deltaX, deltaY));

    float outlineFactor = smoothstep(outlineThreshold - outlineSmoothness,
    outlineThreshold + outlineSmoothness,
    edgeStrength) ;
    float avg = (baseColor.x + baseColor.y + baseColor.z)/ 4;

    vec3 finalColor = mix(vec3(0), outlineColor/ 1, outlineFactor/1.14);

    vec2 st = texCoord.xy;

    float y = st.x;

    vec3 color = vec3(baseColor.xyz);

    float pct = plot(st);
    color = (1-pct)*color+pct*vec3(finalColor);

    //color.x = color.x > 0.8 ? 1 : 0;

    float finaleAGAIN = color.x + avg / 3;

    fragColor = vec4(vec3(color), 1.0);
}