attribute vec3 a_positionStart;
attribute vec3 a_vector;
attribute vec3 a_color;
attribute vec2 a_speedSize;

uniform float u_time;

varying vec4 v_color;

void main()
{
  v_color = vec4(a_color, 1.0);

  float speed = a_speedSize.x;
  float size = a_speedSize.y;

  gl_Position.xyz = mod((a_vector * (speed * u_time) + a_positionStart), 2.0) - 1.0;
  gl_Position.w = 1.0;

  gl_PointSize = size;
}