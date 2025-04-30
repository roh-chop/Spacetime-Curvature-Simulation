package simulation.draw;

import org.joml.Vector3f;


public class Grid extends Object{

    private Mesh mesh;
    private float[]vertices;
    private float[]colors;
    private int[]indices;
    
    private double scale;

    public Grid(double scale){
        super(0);
        this.scale = scale;
    }


    public void createGrid(int row, int col){
        float[]vertices = new float[3*row*col];
        int[]indices = new int[4*row*col-2*row-2*col];
        float[]colors =new float[3*row*col];

        float x=-900f;
        float y=0f;
        float z=1900f;

        int index=0;
        int index1=0;

        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                vertices[index]=x;
                colors[index]=0.3f;
                index++;
                vertices[index]=y;
                colors[index]=0.3f;
                index++;
                vertices[index]=z;
                colors[index]=0.3f;
                index++;
                x+=scale;
                if(j<col-1){
                    indices[index1]=col*i+j;
                    index1++;
                    indices[index1]=col*i+j+1;
                    index1++;
                }
               if(i<row-1){
                indices[index1]=col*i+j;
                index1++;
                indices[index1]=col*(i+1)+j;
                index1++;
               }
                
            }
            z+=-scale;
            x=-900f;
        }

        this.mesh = MeshGen.createMesh(vertices, colors, indices);
        this.vertices = vertices;
        this.colors = colors;
        this.indices = indices;
    }

    public Mesh getMesh(){
        return mesh;
    }
    
     public Vector3f getRotation(){
        return new Vector3f(0,0,0);
    }


    public Vector3f getPosition(){
        return new Vector3f(0,0,0);
    }

    public float[]getVertices(){
        return vertices;
    }

    public void setVertices(float[]vertices){
        this.vertices = vertices;
        int vao = this.mesh.getVao();
        MeshGen.updateVBO(vao,vertices);
    }
}
