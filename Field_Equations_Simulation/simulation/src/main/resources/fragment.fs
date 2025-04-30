#version 450

in vec3 exColors;

out vec4 fragColor;

void main(){
    fragColor = vec4(exColors,0.1);
}