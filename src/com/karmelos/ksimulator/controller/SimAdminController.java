package com.karmelos.ksimulator.controller;

import com.karmelos.ksimulator.model.SimModule;
import com.karmelos.ksimulator.model.SimModuleType;
import com.karmelos.ksimulator.model.SimUser;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author MorpheuS
 */
public class SimAdminController extends SimController{
	
     @PersistenceContext(unitName = "SimulatorPU")
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerfactory;
   private static String baseUrl = "jdbc:mysql://host:3306/ksimulator?zeroDateTimeBehavior=convertToNull";

    public SimAdminController() {
        Map LocalProperty = new HashMap();
        LocalProperty.put("hibernate.show_sql", true);
        LocalProperty.put("hibernate.connection.url", baseUrl.replaceAll("host", "127.0.0.1"));
        entityManagerfactory = Persistence.createEntityManagerFactory("Access", LocalProperty);
        entityManager = entityManagerfactory.createEntityManager();
    }
     @Override
    public SimModuleType[] fetchModuleTypes() {
        try {
            Query q = entityManager.createQuery("select o from SimModuleType o");

            //convert list to SimModule[] and return
            List<SimModule> list = q.getResultList();
            SimModuleType[] listArray = new SimModuleType[list.size()];
            listArray = list.toArray(listArray);
            return listArray;
        } finally {
        }
    }
   
    //fetch SimUser
     public SimUser[] fetchSimUser() {
        try {
            Query q = entityManager.createQuery("select o from SimUser o");

            //convert list to SimModule[] and return
            List<SimModule> list = q.getResultList();
            SimUser[] listArray = new SimUser[list.size()];
            listArray = list.toArray(listArray);
            return listArray;
        } finally {
        }
    }

    //fetchSimModules
     @Override
    public SimModule[] fetchModules(Long moduleTypeId) {
  

        try {
            Query q = entityManager.createQuery("select o from SimModule o where o.moduleType.id = :mtid ");
            q.setParameter("mtid", moduleTypeId);
            //convert list to SimModule[] and return
            List<SimModule> list = q.getResultList();
            SimModule[] listArray = new SimModule[list.size()];
            listArray = list.toArray(listArray);
            return listArray;
        } finally {
        }
    }
    
    //fetchSessions or SimState
     @Override
    public void saveObject(Object simObject)
    { 
     try {
        entityManager.getTransaction().begin();
                entityManager.persist(simObject);
           
            entityManager.flush();
            entityManager.getTransaction().commit();
        } finally {
           
        }

       }
    
    // save exixting objects
    public void mergeObject(Object simObject)
    { 
     try {
           entityManager.getTransaction().begin();
            
                entityManager.merge(simObject);
           
            entityManager.flush();
            entityManager.getTransaction().commit();
        } finally {
           
        }

     }
     public void deleteObject(Object SimObject){
    
        try {
            
            
            entityManager.getTransaction().begin();
          
          
            
            
                entityManager.remove(SimObject);
             entityManager.getTransaction().commit();
          
        } finally {
           
        }

    
    } // end DeleteObject
     
     public static String passwordHasher(String passWord){
         // this uses an md5
            byte[] bytes = passWord.getBytes(Charset.forName("UTF-8"));
        final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
       MessageDigest md = null;
       try {
           md = MessageDigest.getInstance("SHA-1");
       }
       catch(NoSuchAlgorithmException e) {
       }
       byte[] buf = md.digest(bytes);
       char[] chars = new char[2 * buf.length];
       for (int i = 0; i < buf.length; ++i) {
           chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
           chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
       }
       return new String(chars);

   }

}