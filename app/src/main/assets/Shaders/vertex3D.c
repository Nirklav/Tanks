uniform mat4 u_ModelViewProjectionM;
uniform mat4 u_ModelViewM;
uniform vec3 u_light;

attribute highp vec2 a_texcoord;
attribute highp vec4 a_normal;
attribute highp vec4 a_position;

varying highp vec2 v_texcoord;
varying mediump float v_light;

void main()
{
    v_texcoord = a_texcoord;

    mat4 ModelViewNormals = u_ModelViewM;
    ModelViewNormals[0][3] = ModelViewNormals[3][0] = 0.0;
    ModelViewNormals[1][3] = ModelViewNormals[3][1] = 0.0;
    ModelViewNormals[2][3] = ModelViewNormals[3][2] = 0.0;
    ModelViewNormals[3][3] = 1.0;

    vec3 position = vec3(u_ModelViewM * a_position);
    vec3 light = normalize(u_light - position);
    vec3 normal = normalize(vec3(ModelViewNormals * a_normal));

    v_light = max(dot(normal, light), 0.2);
    gl_Position = u_ModelViewProjectionM * a_position;
}