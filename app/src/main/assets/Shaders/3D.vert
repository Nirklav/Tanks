uniform mat4 u_modelViewProjectionMatrix;
uniform mat4 u_modelMatrix;
uniform mat3 u_modelNormalMatrix;
uniform vec3 u_light;
uniform vec4 u_colorCoefficients;

attribute highp vec2 a_texcoord;
attribute highp vec3 a_normal;
attribute highp vec4 a_position;

varying highp vec2 v_texcoord;
varying mediump float v_light;
varying mediump vec4 v_colorCoefficients;

void main()
{
  v_texcoord = a_texcoord;

  vec3 position = vec3(u_modelMatrix * a_position);
  vec3 light = normalize(u_light - position);
  vec3 normal = normalize(u_modelNormalMatrix * a_normal);

  v_light = max(dot(normal, light), 0.4);
  v_colorCoefficients = u_colorCoefficients;

  gl_Position = u_modelViewProjectionMatrix * a_position;
}