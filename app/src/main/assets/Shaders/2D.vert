uniform mat4 u_modelViewMatrix;
uniform mat3 u_texMatrix;

attribute highp vec2 a_texcoord;
attribute highp vec4 a_position;

varying highp vec2 v_texcoord;

void main()
{
   v_texcoord = (u_texMatrix * vec3(a_texcoord, 1.0)).xy;

   gl_Position = u_modelViewMatrix * a_position;
}