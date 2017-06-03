/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INET;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 *
 * @author DeadMansMarch
 */
public class IAddress extends SocketAddress{
    private String ADDRESS;
    private int PORT;
    
    //Create an address.
    public IAddress(String ADDRESS,int PORT){
        this.ADDRESS = ADDRESS;
        this.PORT = PORT;
    }
    
    //Get an INET.
    public InetAddress PrepINET() throws UnknownHostException{
        return InetAddress.getByName(ADDRESS);
    }
    
    //Get an InetSocketAddress.
    public InetSocketAddress PrepISOCK() throws UnknownHostException{
        return new InetSocketAddress(this.PrepINET(),PORT);
    }
    
    public String toString(){
        return ADDRESS + "@" + PORT;
    }
}
