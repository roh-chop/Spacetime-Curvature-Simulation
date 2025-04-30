package simulation.draw;
import org.joml.*;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {

    private boolean isRightPressed;
    private boolean isRightReleased;
    private boolean isEntered;
    private Vector2d previousPos;
    private Vector2d currentPos;
    private Vector2f displayVec = new Vector2f();

    public MouseInput(){
        previousPos = new Vector2d(-1,-1);
        currentPos = new Vector2d(0,0);
    }

    public void init(Window window){

        glfwSetCursorPosCallback(window.getWindow(),(windowHandle, xPos, yPos)->{
            currentPos.x = xPos;
            currentPos.y = yPos;
        });

        glfwSetCursorEnterCallback(window.getWindow(),(windowHandle,entered)->{
            isEntered = entered;
        });

        glfwSetMouseButtonCallback(window.getWindow(),(windowHandle, button, action, mode)->{
            isRightPressed = button==GLFW_MOUSE_BUTTON_LEFT && action==GLFW_PRESS;
            isRightReleased = button==GLFW_MOUSE_BUTTON_LEFT && action==GLFW_RELEASE;
        });
    }

    public void input(Window window){
        displayVec.x = 0;
        displayVec.y = 0;
        if(previousPos.x>0 && previousPos.y>0 && isEntered){
            double deltaX = currentPos.x-previousPos.x;
            double deltaY = currentPos.y-previousPos.y;
            boolean rotateX = deltaX!=0;
            boolean rotateY = deltaY!=0;

            if(rotateX){
                displayVec.y = (float)deltaX;
            }
            if(rotateY){
                displayVec.x = (float)deltaY;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public boolean isRightPressed(){
        return isRightPressed;
    }

    public boolean isRightReleased(){
        return isRightReleased;
    }

    public Vector2f getDisplayVec(){
        return displayVec;
    }
    
}
