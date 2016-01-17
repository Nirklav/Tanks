attribute vec3 a_position;

uniform mat4 u_modelViewProjectionMatrix;
uniform vec4 u_color;

varying vec4 v_color;

void main()
{
  v_color = u_color;
	gl_Position = u_modelViewProjectionMatrix * vec4(a_position, 1.0);
}
