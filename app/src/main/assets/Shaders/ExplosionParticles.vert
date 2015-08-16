attribute vec3 a_position;
attribute vec3 a_directionVector;
attribute vec4 a_color;

uniform vec2 u_lifeTime;
uniform mat4 u_matrix;

varying vec4 v_color;

void main()
{
  float time = u_lifeTime.y;
  float life = u_lifeTime.x;

  float length = min(time, (time - life / 4.0) / 4.0 + life / 4.0);
  vec3 currentPosition = a_position + (a_directionVector * length);

  float delta = time / life;

  v_color = mix(a_color, vec4(0.0, 0.0, 0.0, 0.0), delta);

  gl_PointSize = mix(50.0, 20.0, delta);
  gl_Position = u_matrix * vec4(currentPosition, 1.0);
}