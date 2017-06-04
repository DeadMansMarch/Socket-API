/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionNet;

import INET.IAddress;
import SocketApi.CoveredSocket;
import SocketApi.Listeners.Listener;
import SocketApi.Listeners.accelerator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author DeadMansMarch
 */

//This class is essentially just a wrapper for a listener. 
//Makes everything a bit easier (things like broadcasts, serverwide systems especially).
public class ConnectionNet {
    protected Listener Catch;
    protected ArrayList<CoveredSocket> STREAMING = new ArrayList<>();
    protected String SQCR;
    
    public void setSequencer(String SQCR){
        this.SQCR = SQCR;
    }
    
    enum ChainPosition{
        Connecting,Idle,Transmission;
    }
    
    public class Accel extends accelerator{
        
        ChainPosition BLOCKPOS = ChainPosition.Connecting;
        Connectable SQCR;
        int POSBREAKER = 0;
        
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
            STREAMING.add(CLIENT);
        }
        
        public void loadSequence(Connectable Sequence){
            this.SQCR = Sequence;
        }
        
        public void run() {
            
            try {
                
            } catch (Exception E) {
                System.out.println("Sequencer unavailable.");
                System.out.println(E);
            }
            while (true && !Thread.currentThread().isInterrupted()){ //Thread wont be stopped on its own.
                try {
                    switch(BLOCKPOS){
                        case Connecting:
                            this.SQCR.Sequence(this.CLIENT);
                            break;
                        case Idle:
                            Thread.sleep(3000);
                            break;
                        case Transmission:
                            break;
                    }
                } catch (Exception E) {
                    System.out.println(E);
                    System.out.println(Arrays.toString(E.getStackTrace()));
                }
            }
        }         
    }
    
    public void loadToListener(String QName,Class<?> Parent,Object Ref) throws ClassNotFoundException{
        System.out.println("Casting : " + QName);
        Class<?> Seq = Class.forName(QName);
        Catch.DRIVEAXLE((accelerator k)->{
            try {
                ((Accel) k).loadSequence((Connectable) Seq.newInstance());
            }catch(Exception E){
                System.out.println(E);
            }
        });
        Catch.Activate(true);
    }
    
    public void rLoadToListener(String QName,Class<?> Parent,Object Ref) throws ClassNotFoundException{
        System.out.println("Casting : " + QName);
        Class<?> Seq = Class.forName(QName);
        Catch.DRIVEAXLE((accelerator k)->{
            try {
                ((Accel) k).loadSequence((Connectable) Seq.getConstructor(Parent).newInstance(new Object[] {Ref}));
            }catch(Exception E){
                System.out.println(E);
            }
        });
        Catch.Activate(true);
    }
   
    public ConnectionNet() throws Exception{
        
        IAddress Local = new IAddress("localhost",8081);
        
        this.Catch = new Listener(Local);
        Catch.preLoad(Accel.class.getName());
        Catch.preLoad(this.getClass().getName(),this);
        Catch.Activate(true);
    }
}
