attribute vec3 a_directionVector;
attribute float a_startTime;

uniform vec2 u_lifeTime;
uniform mat4 u_matrix;
uniform vec2 u_pointSize;
uniform vec4 u_startColor;
uniform vec4 u_endColor;

varying vec4 v_color;

void main()
{
  float life = u_lifeTime.x;
  float time = u_lifeTime.y;

  // from 0 to 1
  float timeProgress = (time - a_startTime) / life;

  vec3 position = a_directionVector * timeProgress;

  v_color = mix(u_startColor, u_endColor, timeProgress);
  gl_PointSize = mix(u_pointSize.x, u_pointSize.y, timeProgress);
  gl_Position = u_matrix * vec4(position, 1.0);
}