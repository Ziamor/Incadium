#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; 
uniform sampler2D u_texture1; 

void main(void) {
	vec4 texColor0 = texture2D(u_texture, v_texCoords);
	vec4 texColor1 = texture2D(u_texture1, v_texCoords);
	
	vec4 outlineColor = vec4(0.0,0.0,0.75, 1.0);
	if(texColor0.a == 0){
	gl_FragColor = vec4(0.0);
	} else {
		if(texColor1.r <= 0.2){
			gl_FragColor = outlineColor;
		} else{
			gl_FragColor = texColor0;
		}
	}
}