uniform sampler2D u_texture;

varying highp vec2 v_texcoord;
varying mediump vec4 v_colorCoefficients;

void main (void)
{
	gl_FragColor = texture2D(u_texture, v_texcoord) * v_colorCoefficients;
}