package com.karmelos.ksimulator.loader;


/*
3ds to java converter
Copyright (C) 2003  Pieter Bos

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


*/



import java.io.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
import java.awt.*;

public class Loader3d {

String textureDir;

  public Loader3d(String filename, TransformGroup tg, String textureDir) {

    BufferedReader file = null;
    boolean moreData = true;
    //set the parameters
    this.textureDir = textureDir;

    //open a file
    try{
      file = new BufferedReader(
          new InputStreamReader(
          (new FileInputStream(filename))), 50000);
    } catch (Exception e) {System.err.println("kan model niet laden"); moreData = false;}

    //read shapes until no more data
    while(moreData)
    {
      Shape3D shape = readShape(file);
      if(shape != null)
      {
        tg.addChild(shape);

      }
      else
      {
        moreData = false;
      }
    }

    try{
      file.close();
      file=null;
    } catch (IOException e)
    {
      System.err.println("file " + filename + " could not be closed!");
    }




  }

  public float[] readArray(BufferedReader file)
  {
    float[] values = null;
    try{
       String line = file.readLine();
       if(line ==null)
         return null;

       int numberOfValues = Integer.parseInt(line);
       values = new float[numberOfValues];
       int i = 0;
       for(i = 0; i < numberOfValues;i++)
       {
         line = file.readLine();
         if (line == null)
           return null;
         values[i] = Float.parseFloat(line);
       }

       return values;
    } catch(Exception e)
    {
      e.printStackTrace();

      return null;
    }

  }

  public int readTextureCoords(BufferedReader file, GeometryInfo geometry){
    //read the texture coordinates
    int numberOfTextures = 0;
     try{
       String line = file.readLine();
       numberOfTextures = Integer.parseInt(line);
     } catch(Exception e)
     {
       e.printStackTrace();
       return 0;
     }

     //read all the textures
     if(numberOfTextures > 0)
     {
       geometry.setTextureCoordinateParams(numberOfTextures,2);
       for(int i = 0; i<numberOfTextures;i++)
       {

         geometry.setTextureCoordinates(i, readArray(file));
       }



     }
     return numberOfTextures;

  }

  public Appearance readAppearance(BufferedReader file, int numberOfTextures)
  {
    Appearance appear = new Appearance();
    Material mat;
    Color3f ambient, diffuse, specular;
    float shininess, transparency;
    int selfIllum;
    ambient = new Color3f();
    diffuse = new Color3f();
    specular = new Color3f();
    shininess = 0;
    selfIllum = 0;
    transparency = 0;

    try
    {
      float[] temp = new float[3];
      String line = file.readLine();
      temp[0] = Float.parseFloat(line);
      line = file.readLine();
      temp[1] = Float.parseFloat(line);
      line = file.readLine();
      temp[2] = Float.parseFloat(line);
      ambient.set(temp);

      line = file.readLine();
      selfIllum = Integer.parseInt(line);

      line = file.readLine();
      temp[0] = Float.parseFloat(line);
      line = file.readLine();
      temp[1] = Float.parseFloat(line);
      line = file.readLine();
      temp[2] = Float.parseFloat(line);
      diffuse.set(temp);

      line = file.readLine();
      temp[0] = Float.parseFloat(line);
      line = file.readLine();
      temp[1] = Float.parseFloat(line);
      line = file.readLine();
      temp[2] = Float.parseFloat(line);
      specular.set(temp);

      line = file.readLine();
      shininess = Float.parseFloat(line);

      line = file.readLine();
      transparency = Float.parseFloat(line);

      //if (numberOfTextures > 0)
      //{
        line =  file.readLine();
        if(!line.equals(""))
        {
          line = textureDir + line;
          //FIXME: hack: the java textureloader requires an observer
          //according to the tutorial, an awt.component should be used.
          //so, i use a label.
          Label observer = new Label("image observer hack");
          TextureLoader loader = new TextureLoader(line, TextureLoader.GENERATE_MIPMAP, observer);

          ImageComponent2D image = loader.getImage();


          //check if the image format is a power of two
          int imageWidth = image.getWidth();
          int imageHeight = image.getHeight();
          if((imageWidth & -imageWidth) != imageWidth || (imageHeight & -imageHeight) != imageHeight){
            imageHeight = (int) Math.ceil(Math.log(imageHeight)/Math.log(2));
            imageHeight = (int) Math.pow(2, imageHeight) ;
            imageWidth= (int) Math.ceil(Math.log(imageWidth)/Math.log(2));
            imageWidth = (int) Math.pow(2, imageWidth) ;
            image = null;
            image = loader.getScaledImage(imageWidth, imageHeight);

          }

          Texture2D texture = new Texture2D(Texture2D.MULTI_LEVEL_MIPMAP, Texture2D.RGB,
                                            imageWidth, imageHeight);

          int imageLevel = 0;
          texture.setImage(imageLevel, image);

          while (imageWidth > 1 || imageHeight > 1){ // loop until size: 1x1
            imageLevel++; // compute this level

            if (imageWidth > 1) imageWidth /= 2; // adjust width as necessary
            if (imageHeight > 1) imageHeight /= 2; // adjust height as necessary

            image = null;

            image = loader.getScaledImage(imageWidth, imageHeight);
            texture.setImage(imageLevel, image);

          }

          texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
          texture.setMinFilter(Texture.MULTI_LEVEL_LINEAR);


          //make sure java frees up the image resources, seems to be a problem
          //sometimes
          image = null;

          TextureAttributes textureAttr = new TextureAttributes();
          textureAttr.setTextureMode(TextureAttributes.MODULATE);


          appear.setTextureAttributes(textureAttr);


          appear.setTexture(texture);

        }



    } catch (Exception e)
    {
      System.out.println("error reading materials");
      e.printStackTrace();
    }


    mat = new Material(ambient, new Color3f(selfIllum/100,selfIllum/100,selfIllum/100) , diffuse, specular, shininess);
    appear.setMaterial(mat);

    if(transparency > 0)
    {
      TransparencyAttributes transpar = new TransparencyAttributes(
          TransparencyAttributes.BLENDED,
          transparency);
      appear.setTransparencyAttributes(transpar);
    }
    return appear;
  }


  public Shape3D readShape(BufferedReader file)
  {
    GeometryInfo geometry = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
    Shape3D shape = null;
    int readLines = 0, numberOfTextures = 0;
    float[] coords, normals, texCoords = null;
    Appearance appear = null;

    //read the coordinates
    coords = readArray(file);
    if(coords == null)
      return null;

    normals = readArray(file);
    if(normals == null)
      return null;

    numberOfTextures = readTextureCoords(file, geometry);

    geometry.setNormals(normals);
    geometry.setCoordinates(coords);

    appear = readAppearance(file, numberOfTextures);

    shape = new Shape3D(geometry.getGeometryArray(), appear);

    return shape;
  }


}