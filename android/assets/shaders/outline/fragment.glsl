#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture; 
uniform vec2 u_pixelSize;
uniform vec4 u_OutlineColor;

void main(void) {
	vec4 texColor = texture2D(u_texture, v_texCoords);
	if(texColor.a == 0.0){
		gl_FragColor = texColor;
	} else {
		vec4 outLine = u_OutlineColor;
		
		float alpha = 4.0*texture2D(u_texture, v_texCoords).a;
		alpha -= texture2D( u_texture, v_texCoords + vec2( u_pixelSize.x, 0.0 ) ).a;
		alpha -= texture2D( u_texture, v_texCoords + vec2( -u_pixelSize.x, 0.0 ) ).a;
		alpha -= texture2D( u_texture, v_texCoords + vec2( 0.0, u_pixelSize.y ) ).a;
		alpha -= texture2D( u_texture, v_texCoords + vec2( 0.0, -u_pixelSize.y ) ).a;
	
		outLine.a = alpha;	
		if(outLine.a > 0.0){
			gl_FragColor = outLine;
		} else {
			gl_FragColor = texColor;
		}
	}
}