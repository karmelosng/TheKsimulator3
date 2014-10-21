/*                                      
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.karmelos.ksimulator.view.swing;

import com.karmelos.ksimulator.controller.SimController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author jumoke
 */
public class TestUndo {
   
    
    public byte[] createModification(String usernString,String componentString,String opString,long timeL,String point){
    String header = "<"+componentString+">"+",";
    header+= usernString+","+timeL+","+point+","+opString+"\n";
    return  header.getBytes();
    }

     public static void main(String [] args){
        try {
            String content = FileUtils.readFileToString(new File("C:\\work.txt"));
            Map<String,String> componentUser = new LinkedHashMap<String, String>();
            Map<Long,String> componentTime= new LinkedHashMap<Long, String>();
            Map<String,String> componentLocation = new LinkedHashMap<String, String>();
            Map<String,String> componentOperation = new LinkedHashMap<String, String>();
            
            String[] split = content.split("\n");
             
            for(String rec: split){
                String[] splitRec = rec.split(",");
                String id = splitRec[0].replaceAll("<", "").replaceAll(">", "");
                componentUser.put(splitRec[1],id);
                componentTime.put(Long.valueOf(splitRec[2]),id);
                componentLocation.put(splitRec[3],id);
                componentOperation.put(splitRec[4],id);
                
            }
             Stack <Long>lifo = new Stack<Long>();
             Queue<Long> q = new ArrayDeque<Long>();
            Set<Long> keySet = componentTime.keySet();
            Iterator<Long> iterator = keySet.iterator();
             
            while(iterator.hasNext()){
                lifo.add(iterator.next());
          
            } 
            Long popped = lifo.pop();
            q.add( popped);
             System.out.println("popped:"+popped);
              System.out.println("Queued"+q.peek());
        } catch (IOException ex) {
            Logger.getLogger(SimController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
