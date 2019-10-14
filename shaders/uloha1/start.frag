#version 150
out vec4 outColor; // output from the fragment shader
in vec3 color;
out vec3 normal_IO;

void main() {
	vec3 light = vec(10);
	float cosAlpha = dot(normal_IO, normalize(light));
	//outColor = vec4(1.0, 1.0, 1.0, 1.0);
	//outColor = vec4(color, 1.0);
	outColor = vec4(vec3(cosAlpha),1.0);
}

