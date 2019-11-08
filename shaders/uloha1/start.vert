#version 150
in vec2 inPosition; // input from the vertex buffer
out vec3 color;
out vec3 normal_IO;
out vec3 lightDir;
uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;
uniform int objectType;
out vec2 posIO;
//uniform mat4 matMV;

float getFValue(vec2 xy){
	return -(xy.x*xy.x*5+xy.y*xy.y*5);
}

vec3 getNormal(vec2 xy){
	float delta = 0.01;
	vec3 u = vec3(xy.x + delta, xy.y, getFValue(xy + vec2(delta,0)))
	- vec3(xy - vec2(delta,0), getFValue(xy + vec2(delta,0)));
	vec3 v = vec3(xy + vec2(0,delta), getFValue(xy + vec2(delta,0)))
	- vec3(xy - vec2(0,delta), getFValue(xy - vec2(0,delta)));
	return cross(u,v);
}

vec3 getSphere(vec2 vec) {
	float az = vec.x * 3.14;
	float ze = vec.y * 3.14 / 2;
	float r = 1;

	float x = r*cos(az)*cos(ze);
	float y = 2* r*sin(az)*cos(ze); //2pryc
	float z = 0.5 * r*sin(ze); //0.5pryc

	return vec3(x,y,z);
}

vec3 getSphereNormal(vec2 vec){
	vec3 u = getSphere(vec+vec2(0.001, 0))
	- getSphere(vec-vec2(0.001,0));

	vec3 v = getSphere(vec+vec2(0, 0.001))
	- getSphere(vec-vec2(0, 0.001));

	return cross(u,v); //vektorovy soucin
}

void main() {
	vec3 light = vec3(10);
	vec2 position = inPosition;

	posIO = inPosition;

	if(objectType == 0) {

		position.xy -= 0.5;
		float z = getFValue(position.xy);
		vec4 objectPos = vec4(position.x, z, position.y, 1.0);

		lightDir = light - (model*objectPos).xyz;
		vec3 normal = normalize(getNormal(position.xy));
		normal =  inverse(transpose(mat3(view*model)))*normal;
		normal_IO = normal;
		color = vec3(normal);
		gl_Position = proj*view*model*objectPos;

	} else {

		position.xy -= 100;
		vec4 objectPos = vec4(getSphere(position),1.0);
		lightDir = light - (model*objectPos).xyz;
		vec3 normal = normalize(getSphereNormal(position.xy));
		normal =  inverse(transpose(mat3(view*model)))*normal;
		normal_IO = normal;
		color = vec3(normal);
		gl_Position = proj*view*model*objectPos;
	}
} 
