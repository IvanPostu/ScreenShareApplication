package com.ivan.app.screenshare;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkSender {

    private boolean conected = false;
    
    public void setHostConfig(String host, int port){
        
        if(port < 0){
            throw new IllegalArgumentException("Port is less then 0");
        }
        
         try { 
            

            InetAddress iaddress = InetAddress.getByName(host);
  
           
        } 
        catch (UnknownHostException e) { 
            throw new IllegalArgumentException("host is invalid");
        } 
        
    }
    
    public void sendData(byte[] data){
        
    }
    
    public void clearConnection(){
        this.conected = false;
    }

    public boolean isConected() {
        return conected;
    }
    
    
    
}
