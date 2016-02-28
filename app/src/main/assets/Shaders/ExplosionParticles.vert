attribute vec3 a_directionVector;

uniform vec2 u_lifeTime;
uniform mat4 u_matrix;
uniform vec2 u_pointSize;
uniform float u_explosionSize;
uniform vec4 u_startColor;
uniform vec4 u_endColor;

varying vec4 v_color;

void main()
{
  float life = u_lifeTime.x;
  float time = u_lifeTime.y;

  // from 0 to 1
  float delta = time / life;

  // from 0 to 0.9
  // (its it's part of hyperbola "1 / -x" with some coeff for move it to 0..1x and 0..0.9y)
  float length = (2. + 1. / -(4. * delta + 0.5)) * (u_explosionSize / 2.);

  vec3 currentPosition = a_directionVector * length;

  v_color = mix(u_startColor, u_endColor, delta);
  gl_PointSize = mix(u_pointSize.x, u_pointSize.y, delta);
  gl_Position = u_matrix * vec4(currentPosition, 1.0);
}