#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; 
uniform sampler2D u_lightmap; 
uniform sampler2D u_lightColorMap;

uniform vec4 u_ambientColor;

void main(void) {
	vec4 texColor = texture2D(u_texture, v_texCoords);	
	vec4 mask = texture2D(u_lightmap, v_texCoords);
	vec4 lightColor = texture2D(u_lightColorMap, v_texCoords);
	
	gl_FragColor = mix(texColor * lightColor, texColor * u_ambientColor, mask);
}