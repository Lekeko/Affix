#include veil:space_helper

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D NoiseTexture;
uniform sampler2D DistortTexture;
uniform float GameTime;


in vec2 texCoord;
out vec4 fragColor;

float depthSampleToWorldDepth(float depthSample) {
    float f = depthSample * 2.0 - 1.0;
    return 2.0 * VeilCamera.NearPlane * VeilCamera.FarPlane / (VeilCamera.FarPlane + VeilCamera.NearPlane - f * (VeilCamera.FarPlane - VeilCamera.NearPlane));
}

void main() {

    vec2 st = texCoord.xy;
    st.x /= VeilCamera.ProjMat[0][0] / VeilCamera.ProjMat[1][1];

    vec3 flamePrimaryColor =vec3(94,96,117);
    vec3 flameSecondaryColor =vec3(394,96,117);

    float some = VeilCamera.ProjMat[0][0] + VeilCamera.ProjMat[1][1];



    float speed = GameTime * 500;

    float distort = texture(NoiseTexture, st).a ;
    vec4 noiseVector = texture(DistortTexture, vec2(st.x - distort, st.y - distort - speed));
    vec4 noiseVector2 = texture(DistortTexture, vec2(st.x - distort, st.y - distort - speed * 1.3));
    vec4 baseColor = texture(DiffuseSampler0, texCoord);

    //float mata = pow(1 - abs(3 - texCoord.x * 22), 3); LASER BEAM

    //some cool ass shit
    float mata = pow(12 - abs(st.x * 44), 2);

    vec3 gradient = mix(flamePrimaryColor, vec3(0,0,0), min(pow(st.x * mata, 0.0009), 1));
    vec3 gradient2 = mix(flameSecondaryColor, vec3(0,0,0), min(pow(st.y * mata, 0.005), 1));

    vec3 finalColor = (
    vec3(
        gradient.x * noiseVector.x,
        gradient.y * noiseVector.y,
        gradient.z * noiseVector.z
    ) +
    vec3(
    gradient2.x * noiseVector2.x,
    gradient2.y * noiseVector2.y,
    gradient2.z * noiseVector2.z
    ));


    finalColor = vec3(finalColor + baseColor.xyz);



    fragColor = vec4(finalColor.x, finalColor.y, finalColor.z, 1.0);

}