uniform sampler2D u_texture;

varying mediump vec2 v_texcoord;
varying mediump float v_light;
varying mediump vec4 v_colorCoefficients;

void main (void)
{
 	mediump vec4 color = texture2D(u_texture, v_texcoord);
	color = vec4(color.x * v_light, color.y * v_light, color.z * v_light, color.w);
	gl_FragColor = color * v_colorCoefficients;
}