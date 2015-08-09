precision mediump float;

varying vec3 v_color;
varying float v_time;

void main()
{
  gl_FragColor = vec4(v_color / pow(v_time, 2.0), 1.0);
}