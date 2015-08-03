precision mediump float;

varying vec4 v_color;

uniform sampler2D u_texture;

void main()
{
  gl_FragColor = v_color * texture2D(u_texture, gl_PointCoord);
}