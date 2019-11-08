#version 150

out vec4 outColor; // output from the fragment shader
// in vec3 vertColor; // bud ev projektu
in vec3 normal;
in vec3 light;
in vec3 viewDirection;


void main() {

    vec4 ambient = vec4(0.2,0,0,1);


    float NdotL = max(0,dot(normalize(normal), normalize(light)));
    vec4 diffuse = vec4(NdotL*vec3(0,0.8,0),1);


    vec3 halfVector = normalize(normalize(light)+normalize(viewDirection));
    float NdotH = dot(normalize(normal), halfVector);
    vec4 specular = vec4(pow(NdotH, 16)*vec3(0,0,0.8),1);


    outColor = ambient +diffuse +specular;
    //= vec4(vertColor, 1.0);
}