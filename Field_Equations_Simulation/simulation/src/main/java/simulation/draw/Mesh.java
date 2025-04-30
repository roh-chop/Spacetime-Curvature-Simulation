package simulation.draw;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh{

    private int vao;
    private int vertices;

    public Mesh(int vao, int vertices){
        this.vao = vao;
        this.vertices = vertices;
    }

    public void render(){
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES,vertices,GL_UNSIGNED_INT,0);
    }




    public void renderGrid(){
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_LINES,vertices,GL_UNSIGNED_INT,0);
    }

    public int getVao(){
        return vao;
    }

    public int getVertices(){
        return vertices;
    }
}