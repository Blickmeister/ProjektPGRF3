#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 color;
uniform mat4 MVP;
void main() {
	vec2 position = inPosition;
	position.xy -= 0.5;
	float z = -(position.x*position.x*5+position.y*position.y*5);
	color = vec3(position, z);
	gl_Position = MVP*vec4(position.x, z, position.y, 1.0);
} 
