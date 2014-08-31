uniform sampler2D u_texture;

varying mediump vec2 v_texcoord;
varying mediump float v_light;

void main (void)
{
 	mediump vec4 color = texture2D(u_texture, v_texcoord);

	color = vec4(color.x * v_light, color.y * v_light, color.z * v_light, color.w);

	gl_FragColor = color;
}