#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; 
uniform sampler2D u_lightmap; 

uniform vec4 u_ambientColor;
uniform vec4 u_lightSourceColor;

void main(void) {
	vec4 texColor = texture2D(u_texture, v_texCoords);
	
	vec4 mask = texture2D(u_lightmap, v_texCoords);

	gl_FragColor = mix(texColor * u_lightSourceColor, texColor * u_ambientColor, mask);
}