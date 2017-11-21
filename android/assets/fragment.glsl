#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;
uniform float time;
uniform float blink_rate;
void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

	gl_FragColor = vec4(0.0);
	if(color.a > 0.0) {
	    gl_FragColor = mix(color,vec4(1.0), mod(time, blink_rate) * 1.0 / blink_rate);
	}
}