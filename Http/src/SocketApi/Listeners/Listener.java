/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Listeners;

import INET.IAddress;
import SocketApi.CoveredSocket;
import java.io.IOException;
import java.net.ServerSocket;
import static java.lang.System.exit;
import java.util.HashMap;

public class Listener {
    protected ServerSocket Listen;
    protected String AXLESUB;
    protected Object AXLEREF;
    protected String AXLEClass;
    
    //Base listener interceptor.
    public class Accel extends accelerator{
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
        }
        
        public void run(){
            try{    
                System.out.println(CLIENT.baseRead());
            }catch(Exception E){
                System.out.println(E);
            }
        }
    }
    
    protected Thread Acceptor;
    
    protected class Acceptor implements Runnable{
        Object XCLASS;
        String XREF;
        public Acceptor(String XREF,Object XCLASS){
            this.XREF = XREF;
            this.XCLASS=XCLASS;
        }
        
        public void run(){
            System.out.println("Listening.");
            while (true){
                try{
                    CoveredSocket Client = new CoveredSocket(Listen.accept());
                    System.out.println("Caught : " + Client.describe());
                    if (AXLEClass != null){
                        
                        System.out.println(XREF);
                        accelerator AXLE = (accelerator) (Class.forName(AXLEClass).getConstructor(Class.forName(XREF),CoveredSocket.class).newInstance(new Object[] {XCLASS,Client}));
                        System.out.println("AXLE CREATED");
                        Thread RUN = new Thread(AXLE);
                        Client.REFER = RUN;
                        Client.REFER.start();
                    }else{
                        System.out.println("NO ACCEL THREAD : PLEASE SPECIFY BEFORE ACTIVATING");
                    }
                }catch(Exception E){
                    System.out.println(E.getMessage());
                    System.out.println(E);
                    exit(2);
                }
            }
        }
     }
     
    //Preloads only a class for intercepting. May cause errors without SUB and REF (unchecked).
    public void preLoad(String AXLE) throws ClassNotFoundException{
        Class Assert = Class.forName(AXLE);
        AXLEClass = AXLE;
    }
    
    //Preloads only intercept subclass and reference.
    public void preLoad(String AXLESUB,Object AXLEREF) throws ClassNotFoundException{
        this.AXLESUB=AXLESUB;
        this.AXLEREF=AXLEREF;
    }
    
    //Will preload a class for intercepting clients with a subclass and reference.
    public void preLoad(String AXLE,String AXLESUB,Object AXLEREF) throws ClassNotFoundException{
        preLoad(AXLE);
        this.AXLESUB=AXLESUB;
        this.AXLEREF=AXLEREF;
    }
    
    //Main constructor, links a socket to IAddress To, then waits for an activation.
    public Listener(IAddress To) throws IOException, ClassNotFoundException{
        System.out.print("Binding...");
        Listen = new ServerSocket();
        Listen.bind(To.PrepISOCK());
        System.out.print(" To:");
        System.out.println(Listen.getInetAddress() + ":" + Listen.getLocalPort());
        preLoad(Listener.Accel.class.getName());
    }
    
    
    //Allows for new client receivers to be created outside of family scheme.
    public Listener(IAddress To,String XCLASS,Object XREF) throws IOException, ClassNotFoundException{
        preLoad(XCLASS,XREF);
    }
    
    //Starts the listener.
    public void Activate(boolean Active){
        if (Acceptor != null) Acceptor.interrupt();
        Acceptor = new Thread(new Acceptor(this.AXLESUB,this.AXLEREF));
        
        if (Active) Acceptor.start();
        else Acceptor.interrupt();
    }
    
}
