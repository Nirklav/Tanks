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
  float timeProgress = time / life;

  // from 0 to 0.9
  // (it's part of hyperbola "1 / -x" with some coeff for move it to 0..1x and 0..0.9y)
  float positionProgress = (2. + 1. / -(4. * timeProgress + 0.5)) * (u_explosionSize / 2.);
  float maxPosition = 0.9 * u_explosionSize;

  vec3 position = a_directionVector * positionProgress;
  float progress = max(length(position) / maxPosition, timeProgress);

  v_color = mix(u_startColor, u_endColor, progress);
  gl_PointSize = mix(u_pointSize.x, u_pointSize.y, progress);
  gl_Position = u_matrix * vec4(position, 1.0);
}