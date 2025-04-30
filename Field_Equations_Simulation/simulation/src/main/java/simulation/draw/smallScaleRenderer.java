package simulation.draw;

import java.io.*;
import java.lang.Math;
import org.joml.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class smallScaleRenderer {


    public final float PI = (float)(Math.PI);
  
    public final float scale = 34740800f;
    public final float gConstant = 0.0000000000667f  ;
    public final float lightSpeed = (float)2.998f*(float)Math.pow(10,8);

    public static final double moonMass = 7.3f*(float)Math.pow(10f,22f);
    public static final double earthMass = 5.972f*(float)Math.pow(10f,24f);
    public static final double sunMass = 1.98f*(float)Math.pow(10f,30f);
    public static final double mercuryMass = 3.285*(float)Math.pow(10f,23f);
    public static final double jupiterMass = 1.898*(float)Math.pow(10f,27f);
    public static final double blackHoleMass = 1.91*Math.pow(10,35);


    private Window window;
    private MouseInput mInput;
    private float sunRs;
    private float jupiterRs;
    private float earthRs;
    private float mercuryRs;
    private float moonRs;

    public smallScaleRenderer(){
        window = new Window();
        window.init();
        glfwShowWindow(window.getWindow());
        mInput = new MouseInput();
        mInput.init(window);
    }
    
    public void render() throws Exception{
        
        
        


        glfwSwapInterval(1);
        glClearColor(0,0,0,0);
       
        Camera camera = new Camera();
        camera.setPosition(-800f,1900f,100f);
        camera.setRotation(0f,-180f,0f);

        Object sphere1 = new Object(blackHoleMass);
        sphere1.createSphere(50,50,0.00000000000005f,1f,1f,0f);
        sphere1.setPosition(new Vector3f(0f,0f,1500f));
        sphere1.setVelocity(new Vector3f(0f,0f,0f));
        

        Object sphere2 = new Object(sunMass);
        sphere2.createSphere(50,50,5f,1f,1f,0f);
        sphere2.setPosition(new Vector3f(300f,0f,1000f));
        sphere2.setVelocity(new Vector3f(0f,0f,0f));

        float sphere1Rs = (2*gConstant*(float)sphere1.mass()/(float)Math.pow(lightSpeed,2));


       

        

        Object[]spheres = {sphere1, sphere2};

        Grid grid = new Grid(15);
        grid.createGrid(100,100);
      

       
        ShaderProgram shader = new ShaderProgram();
        shader.createVertexShader(loadResources("C:\\Users\\rohan\\OneDrive\\Programming Projects\\Field_Equations_Simulation\\simulation\\src\\main\\resources\\shader.vs"));
        shader.createFragmentShader(loadResources("C:\\Users\\rohan\\OneDrive\\Programming Projects\\Field_Equations_Simulation\\simulation\\src\\main\\resources\\fragment.fs"));
        shader.link();

        float aspectRatio = (float)window.getWidth()/window.getHeight();
        Transformations transformer = new Transformations();
        Matrix4f projectionMatrix = transformer.
        createProjectionMatrix(360f,aspectRatio,0.05f, 30000);
        Matrix4f viewMatrix = transformer.createViewMatrix(camera);
        
        Matrix4f worldMatrix = transformer.getModelViewMatrix(sphere1,viewMatrix);

        shader.createUniform("projectionMatrix");
        shader.createUniform("worldMatrix");
        shader.bind();
        shader.setUniform("projectionMatrix", projectionMatrix);
        
      
        
        while(!glfwWindowShouldClose(window.getWindow())){
           

            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            mInput.input(window);
            if(mInput.isRightPressed()){
                Vector2f rotVector = mInput.getDisplayVec();
                camera.moveRotation(rotVector.x*0.3f,rotVector.y*0.3f,0);
            }

            Vector3f offset = window.getCameraInc();
            camera.movePosition(offset.x, offset.y, offset.z);

            double massChange = window.getMassChange();
            if(massChange>1){
            sphere2.changeMass(massChange);
            sphere2.setRadius(1.3f);
            sphere2.createSphere(50, 50, sphere2.radius(), 1,1,0);
            window.setMassChange(1);
            sphere2.setPosition(new Vector3f(sphere2.getPosition().x,3*sphere2.radius(),sphere2.getPosition().z));
            }
            if(massChange<1){
                sphere2.changeMass(massChange);
                sphere2.setRadius(0.75f);
                sphere2.createSphere(50, 50, sphere2.radius(), 1,1,0);
                window.setMassChange(1);
                sphere2.setPosition(new Vector3f(sphere2.getPosition().x,3*sphere2.radius(),sphere2.getPosition().z));
                }

            aspectRatio = (float)window.getWidth()/window.getHeight();
           
            projectionMatrix = transformer.
            createProjectionMatrix(60f,aspectRatio,0.01f, 10000f);
            shader.setUniform("projectionMatrix", projectionMatrix);
           
            viewMatrix = transformer.createViewMatrix(camera);


            for(Object sphere:spheres){
                Vector2f totalForce = new Vector2f(0f,0f);
                for(Object nextSphere:spheres){
                    if(nextSphere!=sphere){
                        Vector3f spherePosition = sphere.getPosition();
                        Vector3f nextSpherePosition = nextSphere.getPosition();

                        float distanceZ = spherePosition.z-nextSpherePosition.z;
                        float distanceX = spherePosition.x-nextSpherePosition.x;
                        float totalDistance = scale*(float)Math.sqrt(Math.pow(distanceX,2)+
                        Math.pow(distanceZ,2));

                        double gForce = gConstant*sphere.mass()*
                        nextSphere.mass()/
                        (
                        Math.pow(totalDistance,2));
                        

                        float theta = (float)Math.atan(distanceZ/distanceX);

                        if(distanceX>0&&distanceZ>0){
                            theta+=PI;
                        }
                        if(distanceZ<0&&distanceX>0){
                            theta-=PI;
                        }
                        if(distanceZ>0&&distanceX==0){
                            theta = -1*PI/2;
                        }
                        if(distanceZ<0&&distanceX==0){
                            theta = PI/2;
                        }
                        if(distanceX>0&&distanceZ==0){
                            theta = PI;
                        }
                        if(distanceX<0&&distanceZ==0){
                            theta = 0;
                        }
           
        

                        Vector2f sphereForce;
                       

                        sphereForce = new Vector2f((float)Math.cos(theta)*(float)gForce,(float)Math.sin(theta)*(float)gForce);
                        totalForce.x+=sphereForce.x;
                        totalForce.y+=sphereForce.y;
                    }  
                }
                sphere.setAcceleration(new Vector3f(totalForce.x/(scale*(float)sphere.mass()),0f,totalForce.y/(scale*(float)sphere.mass())));
                sphere.move();
               
                
               if(sphere!=sphere1){
                    Vector3f sphere1Position = sphere.getPosition();
                    Vector3f position = sphere1.getPosition();

                    float distanceZ = sphere1Position.z-position.z;
                    float distanceX = sphere1Position.x-position.x;
               
                    float totalDistance = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                    Math.pow(scale*distanceZ,2));

                    




                    float offsetY = (2f*(float)Math.sqrt(sphere1Rs*(Math.abs(totalDistance-sphere1Rs))))/3000000f;
                    sphere.setPosition(new Vector3f(sphere1Position.x,offsetY,sphere1Position.z));
                
                }
                worldMatrix.identity();
                worldMatrix = transformer.getModelViewMatrix(sphere,viewMatrix);
           
                shader.setUniform("worldMatrix", worldMatrix);
                sphere.getMesh().render();
                
            }   

          

            float[]gridVertices = grid.getVertices();
            
            for(int i=1;i<gridVertices.length;i+=3){
                float x = gridVertices[i-1];
                float z = gridVertices[i+1];
                float y=0;

                for(Object spher: spheres){
                    float rS = (2*gConstant*(float)spher.mass()/(float)Math.pow(lightSpeed,2));


                Vector3f position = spher.getPosition();

                float distanceZ = z-position.z;
                float distanceX = x-position.x;
               
                float totalDistance = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                Math.pow(scale*distanceZ,2));
                y+=2f*(float)Math.sqrt(rS*(Math.abs(totalDistance-rS)))/3000000f;
                }

                gridVertices[i] = y ;

            }

            grid.setVertices(gridVertices);
            

            worldMatrix.identity();
            worldMatrix = transformer.getModelViewMatrix(grid,viewMatrix);
            shader.setUniform("worldMatrix", worldMatrix);
            grid.getMesh().renderGrid();
            
            
            glfwSwapBuffers(window.getWindow());
            glfwPollEvents();
        }
        glfwTerminate();
    }

    private String loadResources(String filePath){
        try{
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder builder = new StringBuilder();
        String nextLine="";
        while((nextLine = reader.readLine())!=null){
            builder.append(nextLine).append("\n");
        }
        reader.close();
        return builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return"";
    }



    
    
    public void calculateNormals(){
        
    }
}
