/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Listeners;

import INET.IAddress;
import SocketApi.CoveredClient;
import java.io.IOException;
import java.net.ServerSocket;
import static java.lang.System.exit;
import java.util.ArrayList;


public class Listener {
    protected ServerSocket Listen;
    protected ArrayList<CoveredClient> Accepted = new ArrayList<>();
    
     protected Thread Acceptor = new Thread(new Runnable(){
        public void run(){
            //System.out.println("Listening.");
            while (true){
                try{
                    CoveredClient Client = new CoveredClient(Listen.accept());

                    Accepted.add(Client);
                }catch(Exception E){
                    System.out.println(E.getMessage());
                    exit(2);
                }
            }
        }
     });
    
    public Listener(IAddress To) throws IOException{
        System.out.print("Binding...");
        Listen = new ServerSocket();
        Listen.bind(To.PrepISOCK());
        System.out.print(" To:");
        System.out.println(Listen.getInetAddress() + ":" + Listen.getLocalPort());
    }
    
    public void Activate(boolean Active){
        if (Active) Acceptor.start();
        else Acceptor.interrupt();
    }
    
}
