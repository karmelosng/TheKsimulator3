package com.karmelos.ksimulator.view.swing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Array;
import com.karmelos.ksimulator.model.SimComponent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
public class Component3dApplication implements ApplicationListener {
   ModelBatch modelBatch;
   Environment environment;
   PerspectiveCamera camera;
   AssetManager assets;
   CameraInputController cameraController; 
   int screenWidth;
   int screenHeight;
    List<SimComponent> listing;
   Array<ModelInstance> instances = new Array<ModelInstance>();
   Map<SimComponent,ModelInstance> modelStash=new HashMap<SimComponent, ModelInstance>();
    private boolean toShow =true;
   
   public Component3dApplication(  List<SimComponent> listed){
      
       assets = new AssetManager();
    listing = listed;
//      toShow = show;
   }

    

   @Override
   public void create() {   
      // Create ModelBatch that will render all models using a camera
      modelBatch = new ModelBatch(); 
      // Create a camera and point it to our model
      camera = new PerspectiveCamera(85, 0, 0);
      camera.position.set(1,1, 1150);
      camera.lookAt(0, 0, 0);
       camera.near = 100f;
      camera.far =2000f;
      camera.update();
 
      // Create the generic camera input controller to make the app interactive
      cameraController = new CameraInputController(camera);
      Gdx.input.setInputProcessor(cameraController);
     
      /// Create an asset manager that lets us dispose all assets at once
      
        for(int i=0;i<listing.size();i++){
                  assets.load("data/obj_"+listing.get(i).getId()+".g3db",Model.class);

                 }  assets.finishLoading();
              // Create an instance of our crate model and put it in an array
               for(int i=0;i<listing.size();i++){
                  Model model = assets.get("data/obj_"+listing.get(i).getId()+".g3db", Model.class);
              ModelInstance inst = new ModelInstance(model);
              instances.add(inst);
                 }
     
     
 
      // Set up environment with simple lighting
      environment = new Environment();
      environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
      environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, 0.8f,-0.2f));
  
   }
 
   @Override
   public void dispose() {
      // Release all resources
       environment.clear();
       assets.dispose();
      modelBatch.dispose();
      instances.clear();
 
   }
 
   @Override
   public void render() {
       // Respond to user events and update the camera
      cameraController.update(); 
      // Clear the viewport
     // Gdx.gl.glViewport(0, 0, screenWidth, screenHeight);
     Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//     Gdx.gl.glClearColor(0.55f, 0, 0, 1.0f);
     Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
      // Draw all model instances using the camera
      modelBatch.begin(camera);
      modelBatch.render(instances, environment);
      modelBatch.end();     
   }
   public Array<ModelInstance> convertMapToList(Map<SimComponent,ModelInstance> mI,List<SimComponent> list){
     Array<ModelInstance> instances = new Array<ModelInstance>();
       Iterator<SimComponent> iterator = list.iterator();
       while(iterator.hasNext()){
        SimComponent sC = iterator.next();
         ModelInstance get = mI.get(sC);
         instances.add(get);
       }
   return instances;
   }
   @Override
   public void resize(int width, int height) {
      // Update screen dimensions
      screenWidth = width;
      screenHeight = height;
 
      // Update viewport size and refresh camera matrices
      camera.viewportWidth = width;
      camera.viewportHeight = height;
      camera.update(true);
   }

    public boolean isToShow() {
        return toShow;
    }

    public void setToShow(boolean toShow) {
        this.toShow = toShow;
    }
 
   @Override
   public void pause() {
   }
 
   @Override
   public void resume() {
   }

}
  

