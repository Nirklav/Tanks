precision mediump float;

uniform sampler2D u_texture;

varying vec4 v_color;

void main()
{
  gl_FragColor = v_color * texture2D(u_texture, gl_PointCoord);
}