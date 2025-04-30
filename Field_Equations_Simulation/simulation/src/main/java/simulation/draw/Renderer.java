package simulation.draw;

import java.io.*;
import java.lang.Math;
import org.joml.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class Renderer {


    public final float PI = (float)(Math.PI);
  
    public final float scale = 347408000f;
    public final float gConstant = 0.0000000000667f  ;
    public final float lightSpeed = (float)2.998f*(float)Math.pow(10,8);

    public static final double moonMass = 7.3f*(float)Math.pow(10f,22f);
    public static final double earthMass = 5.972f*(float)Math.pow(10f,24f);
    public static final double sunMass = 1.98f*(float)Math.pow(10f,30f);
    public static final double mercuryMass = 3.285*(float)Math.pow(10f,23f);
    public static final double jupiterMass = 1.898*(float)Math.pow(10f,27f);


    private Window window;
    private MouseInput mInput;
    private float sunRs;
    private float jupiterRs;
    private float earthRs;
    private float mercuryRs;
    private float moonRs;

    public Renderer(){
        window = new Window();
        window.init();
        glfwShowWindow(window.getWindow());
        mInput = new MouseInput();
        mInput.init(window);
    }
    
    public void renderSolarSystem() throws Exception{
        
        
        


        glfwSwapInterval(1);
        glClearColor(0,0,0,0);
       
        Camera camera = new Camera();
        camera.setPosition(0f, 475f,100f);

        //moon
        Object sphere1 = new Object(moonMass);
        sphere1.createSphere(50,50, 0.25f,1,1,1);
        sphere1.setPosition(new Vector3f(-15f,0f,-28.8936f));
        sphere1.setVelocity(new Vector3f(0.00008874f,0f,0f));

        //earth
        Object sphere2 = new Object(earthMass); 
        sphere2.createSphere(50,50,1f,0f,0f,1f);
        sphere2.setPosition(new Vector3f(-15f,0f,-30f));
        sphere2.setVelocity(new Vector3f(0.0000858f,0f,0f));

        //sun
        Object sphere3 = new Object(sunMass);
        sphere3.createSphere(50,50,50f,1,1,0);
        sphere3.setPosition(new Vector3f(-15f,600f,-460.169f));
        sphere3.setVelocity(new Vector3f(0.0000001f,0f,0f));
        
        //mercury
        Object sphere4 = new Object(mercuryMass);
        sphere4.createSphere(50,50,1f,1,0.5f,0);
        sphere4.setPosition(new Vector3f(-15f,475f,-608.217f));
        sphere4.setVelocity(new Vector3f(-0.0001353f,0f,0f));

        //jupiter
        Object sphere5 = new Object(jupiterMass);
        sphere5.createSphere(50,50,50f,0.58f,0.29f,0f);
        sphere5.setPosition(new Vector3f(-15f,475f,1737.9669f));
        sphere5.setVelocity(new Vector3f(0.000037621f,0f,0f));
        
        
        

       

        this.sunRs = (2*gConstant*(float)sphere3.mass()/(float)Math.pow(lightSpeed,2));
        this.jupiterRs= (2*gConstant*(float)sphere5.mass()/(float)Math.pow(lightSpeed,2));
        this.mercuryRs = (2*gConstant*(float)sphere4.mass()/(float)Math.pow(lightSpeed,2));
        this.earthRs= (2*gConstant*(float)sphere2.mass()/(float)Math.pow(lightSpeed,2));
        this.moonRs= (2*gConstant*(float)sphere1.mass()/(float)Math.pow(lightSpeed,2));

        

        Object[]spheres = {sphere1,sphere2,sphere3,sphere4,sphere5};

        Grid grid = new Grid(30);
        grid.createGrid(100,100);
      

       
        ShaderProgram shader = new ShaderProgram();
        shader.createVertexShader(loadResources("simulation\src\main\resources\shader.vs"));
        shader.createFragmentShader(loadResources("simulation\src\main\resources\fragment.fs"));
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
        
        double lastTime = glfwGetTime();
        int frameCount=0;
        
        while(!glfwWindowShouldClose(window.getWindow())){
            frameCount++;
            double currentTime = glfwGetTime();
            if(currentTime-lastTime>1){
                double fps = frameCount/(currentTime-lastTime);
                System.out.println(fps);
                lastTime=currentTime;
                frameCount=0;
            }

            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            mInput.input(window);
            if(mInput.isRightPressed()){
                Vector2f rotVector = mInput.getDisplayVec();
                camera.moveRotation(rotVector.x*0.3f,rotVector.y*0.3f,0);
            }

            Vector3f offset = window.getCameraInc();
            camera.movePosition(offset.x, offset.y, offset.z);
           
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
               
                
               if(sphere!=sphere3){
                    Vector3f sphere1Position = sphere.getPosition();
                    Vector3f position = sphere3.getPosition();

                    float distanceZ = sphere1Position.z-position.z;
                    float distanceX = sphere1Position.x-position.x;
               
                    float totalDistanceSun = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                    Math.pow(scale*distanceZ,2));

                    position = sphere5.getPosition();

                    distanceZ = sphere1Position.z-position.z;
                    distanceX = sphere1Position.x-position.x;
               
                    float totalDistanceJupiter = (float)Math.sqrt(Math.pow(scale*distanceX,2)+
                    Math.pow(scale*distanceZ,2));




                    float offsetY = (2f*(float)Math.sqrt(sunRs*(Math.abs(totalDistanceSun-sunRs)))+2f*(float)Math.sqrt(jupiterRs*(Math.abs(totalDistanceJupiter-jupiterRs))))/30000f;
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
                y+=2f*(float)Math.sqrt(rS*(Math.abs(totalDistance-rS)))/30000f;
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
