package simulation.draw;

import java.lang.Math;
import org.joml.*;

public class Camera {
    
    private final Vector3f position;
    private final Vector3f rotation;

    public Camera(){
        position = new Vector3f(0f,0f,0f);
        rotation = new Vector3f(0f,0f,0f);
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ){
        if(offsetZ!=0){
            position.x+=(float)Math.sin(Math.toRadians(rotation.y))*-1f*offsetZ;
            position.z+=(float)Math.cos(Math.toRadians(rotation.y))*offsetZ;
        }
        if(offsetX!=0){
            position.x+=(float)Math.sin(Math.toRadians(rotation.y-90))*-1f*offsetX;
            position.z+=(float)Math.cos(Math.toRadians(rotation.y-90))*offsetX;
        }
        position.y+=offsetY;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x+=offsetX;
        rotation.y+=offsetY;
        rotation.z+=offsetZ;
    }



    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
    public void setRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public Vector3f getPosition(){
        return position;
    }

    public Vector3f getRotation(){
        return rotation;
    }
}
