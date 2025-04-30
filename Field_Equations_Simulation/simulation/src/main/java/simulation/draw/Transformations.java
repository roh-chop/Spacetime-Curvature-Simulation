package simulation.draw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformations {

    private final Matrix4f projectionMatrix;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f viewMatrix;

    public Transformations(){
        projectionMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public Matrix4f createProjectionMatrix(float fov, float aspectRatio, float znear, float zfar){
      projectionMatrix.identity()
      .perspective((float)Math.toRadians(fov), aspectRatio, znear, zfar);

      return projectionMatrix;
    }

    public Matrix4f createViewMatrix(Camera camera){
        Vector3f rotation = camera.getRotation();
        Vector3f cameraPos = camera.getPosition();
        viewMatrix.identity()
        .rotate((float)Math.toRadians(rotation.x),new Vector3f(1,0,0))
        .rotate((float)Math.toRadians(rotation.y),new Vector3f(0,1,0))
        .rotate((float)Math.toRadians(rotation.z),new Vector3f(0,0,1))
        .translate(-cameraPos.x,-cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public Matrix4f getModelViewMatrix(Object object, Matrix4f viewMatrix){
        Vector3f rotation = object.getRotation();
        
        modelViewMatrix.identity()
        .translate(object.getPosition())
        .rotateX((float)Math.toRadians(-rotation.x))
        .rotateY((float)Math.toRadians(-rotation.y))
        .rotateZ((float)Math.toRadians(-rotation.z))
         ;

        Matrix4f viewCurr = new Matrix4f(viewMatrix);

        return viewCurr.mul(modelViewMatrix);
    }
    
}
