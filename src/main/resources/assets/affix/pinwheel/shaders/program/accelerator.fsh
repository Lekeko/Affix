#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D Noise;
uniform float GameTime;

uniform float tranzitioner;
uniform float shaderStrength;

in vec2 texCoord;
out vec4 fragColor;

uniform float outlineThreshold = 0.1;
uniform float outlineSmoothness = 0.05;

uniform vec3 shaderAccent;


float depthSampleToWorldDepth(float depthSample) {
    float f = depthSample * 2.0 - 1.0;
    return 2.0 * VeilCamera.NearPlane * VeilCamera.FarPlane / (VeilCamera.FarPlane + VeilCamera.NearPlane - f * (VeilCamera.FarPlane - VeilCamera.NearPlane));
}


float plot(vec2 st) {
    return smoothstep(tranzitioner * 2.4, tranzitioner * 2.4 -0.5, abs(st.y));
}

void main() {
    float speed = GameTime * 1400;
    vec4 baseColor = texture(DiffuseSampler0, texCoord);
    vec3 outlineColor = (shaderAccent + vec3(1))/255;
    vec2 texelSize = 0.5 / vec2(textureSize(DiffuseDepthSampler, 0)) ;
    float pula = 1;

    float depthL = texture(DiffuseDepthSampler, texCoord - vec2(texelSize.x, 0.0)).r / pula;
    float depthR = texture(DiffuseDepthSampler, texCoord + vec2(texelSize.x, 0.0)).r / pula;
    float depthUp = texture(DiffuseDepthSampler, texCoord + vec2(0.0, texelSize.y)).r / pula;
    float depthDown = texture(DiffuseDepthSampler, texCoord - vec2(0.0, texelSize.y)).r / pula;


    float worldDepthL = depthL * shaderStrength;
    float worldDepthR = depthR * shaderStrength;
    float worldDepthUp = depthUp * shaderStrength;
    float worldDepthDown = depthDown * shaderStrength;

    float deltaX = abs(worldDepthR - worldDepthL);
    float deltaY = abs(worldDepthUp - worldDepthDown);

    float edgeStrength = length(vec2(deltaX, deltaY));

    float outlineFactor = smoothstep(outlineThreshold - outlineSmoothness,
    outlineThreshold + outlineSmoothness,
    edgeStrength) ;

    vec3 finalColor = mix(vec3(0), outlineColor, outlineFactor);

    vec2 st = texCoord.xy;

    float y = st.x;

    vec3 color = vec3(baseColor.xyz);

    float pct = plot(st);
    color = (1-pct)*color+pct*vec3(finalColor);

    //color.x = color.x > 0.8 ? 1 : 0;


    fragColor = vec4(vec3(color), 1.0);
}