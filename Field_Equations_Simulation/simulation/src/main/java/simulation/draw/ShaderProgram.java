package simulation.draw;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.*;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram{
    private int programId;
    private int vertId;
    private int fragId;

    private Map<String, Integer> uniforms;

    public ShaderProgram(){
        programId = glCreateProgram();
        if(programId<=0){
            throw new RuntimeException("Shader Program Creation Failed");
        }
        uniforms = new HashMap<>();
    }

    public void link(){
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS)==0){
            throw new RuntimeException("Linking Failed");
        }
        glValidateProgram(programId);
        glDetachShader(programId,vertId);
        glDetachShader(programId, fragId);
    }

    public void bind(){
        glUseProgram(programId);
    }

    public int createShader(String shaderCode, int type){
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if(glGetShaderi(shaderId,GL_COMPILE_STATUS)==0){
            System.out.println(shaderCode);
            throw new RuntimeException("Faulty Shader Code");
        }
        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void createVertexShader(String shaderCode){
        vertId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    
    public void createFragmentShader(String shaderCode){
        fragId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public void createUniform(String uniformName){
        int uniformId = glGetUniformLocation(programId, uniformName);
        if(uniformId<0){
            throw new RuntimeException("Can't find uniform");
        }
        uniforms.put(uniformName,uniformId);
    }

    public void setUniform(String uniformName,Matrix4f values){
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer fb = stack.mallocFloat(16);
            values.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName),false,fb);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}