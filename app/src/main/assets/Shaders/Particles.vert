attribute vec3 a_position;
attribute vec3 a_directionVector;
attribute vec3 a_color;

uniform vec2 u_lifeTime;
uniform mat4 u_matrix;

varying vec3 v_color;
varying float v_time;

void main()
{
  v_color = a_color;
  v_time = u_lifeTime.y;
  float life = u_lifeTime.x;

  if (life < v_time)
    gl_Position = vec4(1000.0, 1000.0, 1.0, 1.0);
  else
  {
    vec3 currentPosition = a_position + (a_directionVector * v_time);
    gl_PointSize = 10.0;
    gl_Position = u_matrix * vec4(currentPosition, 1.0);
  }
}