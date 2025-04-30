package simulation.draw;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;


import java.nio.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class MeshGen{

    private static ArrayList<Integer>vbos = new ArrayList<>();
    private static ArrayList<Integer>vaos = new ArrayList<>();

    

    public static Mesh createMesh(float[]vertices,float[]colors,int[]indices){
       
        int vao = genVAO();
        storeData(0,3,vertices);
        storeData(1,3,colors);
        bindIndices(indices);
        glBindVertexArray(0);
        return new Mesh(vao,indices.length);
    }

    private static FloatBuffer createFloatBuffer(float[]data){
        FloatBuffer fb = MemoryUtil.memAllocFloat(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    private static IntBuffer createIntBuffer(int[]data){
        IntBuffer ib = MemoryUtil.memAllocInt(data.length);
        ib.put(data);
        ib.flip();
        return ib;
    }

    private static int genVAO(){
        int vao = glGenVertexArrays();
        vaos.add(vao);
        glBindVertexArray(vao);
        return vao;
    }

    private static void storeData(int attribute,int size,float[]data ){
        FloatBuffer buffer = createFloatBuffer(data);
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
        glVertexAttribPointer(attribute, size,GL_FLOAT,false,0,0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        MemoryUtil.memFree(buffer);
    }

    private static void bindIndices(int[]data){
        IntBuffer buffer = createIntBuffer(data);
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
    }

    public static void updateVBO(int vao,float[]vertices){
        FloatBuffer buffer = createFloatBuffer(vertices);
        int index = vaos.indexOf(vao);
        int vbo = vbos.get(index*3);
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBufferData(GL_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        MemoryUtil.memFree(buffer);
    }
    

    
       
}