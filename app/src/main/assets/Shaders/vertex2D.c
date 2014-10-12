uniform mat4 u_matrix;

attribute mediump vec2 a_texcoord;
attribute highp vec4 a_position;

varying mediump vec2 v_texcoord;

void main()
{
   v_texcoord = a_texcoord;

   gl_Position = u_matrix * a_position;
}