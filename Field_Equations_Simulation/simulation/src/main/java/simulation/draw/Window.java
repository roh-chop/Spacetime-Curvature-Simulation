package simulation.draw;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import org.joml.*;

public class Window{


    private long window;
    private int width;
    private int height;

    public Vector3f cameraInc=new Vector3f(0,0,0);

    public double massChange=1;

    


    public void init(){
        if(!glfwInit()){
            throw new IllegalStateException("Initialization Failed");
        }
        
        window = glfwCreateWindow(640,480,"Gravity Simulation",0,0);
        if(window<=0){
            throw new RuntimeException("window creation failed");
        }
        width=640;
        height=480;

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0,0,width,height);

        glfwSetWindowSizeCallback(window,(window,width,height)->{
            this.width=width;
            this.height=height;
            glViewport(0,0,width,height);
        });
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glPolygonMode( GL_FRONT_AND_BACK, GL_FILL);

        glfwSetKeyCallback(window,(long window,int key, int scancode, int action, int mods)->{
            if(action == GLFW_PRESS){
                if(key == GLFW_KEY_W)
                    cameraInc.z=-15f;
                if(key == GLFW_KEY_S)
                    cameraInc.z = +15f;
                if(key == GLFW_KEY_A)
                    cameraInc.x=-.25f;
                if(key == GLFW_KEY_D)
                    cameraInc.x = +.25f;
                if(key == GLFW_KEY_SPACE)
                    cameraInc.y = +15f;
                if(key == GLFW_KEY_LEFT_SHIFT)
                    cameraInc.y = -15f;

                if(key==GLFW_KEY_U)
                    massChange *= 2.7;
                if(key == GLFW_KEY_J)
                    massChange *= (1/2.7);
            }

            if(action == GLFW_RELEASE)
             cameraInc = new Vector3f(0,0,0);
        });

       
        
    }

    public Vector3f getCameraInc(){
        return cameraInc;
    }

    public long getWindow(){
        return window;
    }  

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public double getMassChange(){
        return massChange;
    }

    public void setMassChange(double value){
        massChange = value;
    }

    



    


}
