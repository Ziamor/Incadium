#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; 

void main(void) {
	vec4 texColor = texture2D(u_texture, v_texCoords);
	vec4 twilightColor = vec4(1.0,0.24,0.24,1.0);
	
	gl_FragColor = texColor * twilightColor;
}