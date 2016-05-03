#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

uniform mat4 transMx;
uniform mat4 projMx;
uniform mat4 vwMx;

uniform vec3 lightPos;

out data {
	vec2 textureCoords;
	
	vec3 surfaceNormal;
	vec3 toLight;
} vsOut;

void main(void) {
	
	//position after rotation and scale are calculated
	vec4 worldPos = transMx * vec4(position, 1.0);
	
	gl_Position = projMx * vwMx * worldPos;
	
	vsOut.textureCoords = textureCoords * 800;
	
	vsOut.surfaceNormal = (transMx * vec4(normal, 0.0)).xyz;
	vsOut.toLight = lightPos - worldPos.xyz;
}