uniform mat4 u_modelViewMatrix;
uniform vec4 u_colorCoefficients;

attribute highp vec2 a_texcoord;
attribute highp vec4 a_position;

varying highp vec2 v_texcoord;
varying mediump vec4 v_colorCoefficients;

void main()
{
  v_texcoord = a_texcoord;
  v_colorCoefficients = u_colorCoefficients;

  gl_Position = u_modelViewMatrix * a_position;
}