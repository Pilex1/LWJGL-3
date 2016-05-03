#version 400 core

in data {
	vec2 textureCoords;
	
	vec3 surfaceNormal;
	vec3 toLight;
} fsIn;

uniform sampler2D textureSampler;

uniform vec3 lightColour;

out vec4 outColour;

void main(void) {

	vec3 surfaceNormal = normalize(fsIn.surfaceNormal);
	vec3 toLight = normalize(fsIn.toLight);
	
	//brightness of current pixel
	float brightness = max(dot(surfaceNormal, toLight), 0.2);
	
	vec3 diffuse = brightness * lightColour;
	
	//outColour = vec4(fsIn.colour, 1.0);
	outColour = vec4(diffuse, 1.0) * texture(textureSampler, fsIn.textureCoords);
}