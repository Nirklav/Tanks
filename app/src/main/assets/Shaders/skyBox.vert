uniform mat4 u_modelViewProjectionMatrix;

attribute highp vec2 a_texcoord;
attribute highp vec4 a_position;

varying highp vec2 v_texcoord;

void main()
{
  v_texcoord = a_texcoord;
  gl_Position = u_modelViewProjectionMatrix * a_position;
}