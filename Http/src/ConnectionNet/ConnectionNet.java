/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionNet;

import INET.IAddress;
import LOCAL.Log;
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
    
    public ArrayList<CoveredSocket> getSTREAMING(){
        return STREAMING;
    }
    
    public enum ChainPosition{
        Connecting,Idle,Transmission;
    }
    
    public class Accel extends accelerator{
        
        ChainPosition BLOCKPOS = ChainPosition.Connecting;
        Sequencer SQCR;
        int POSBREAKER = 0;
        
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
            STREAMING.add(CLIENT);
        }
        
        public void loadSequence(Sequencer Sequence){
            this.SQCR = Sequence;
            this.SQCR.preLoad(this.CLIENT);
        }
        
        public void run() {
            
            try {
                
            } catch (Exception E) {
                Log.Write(this,"Sequencer unavailable.");
                Log.Write(this,E);
            }
            while (true && !Thread.currentThread().isInterrupted()){ //Thread wont be stopped on its own.
                try {
                    this.BLOCKPOS = this.SQCR.Sequence(BLOCKPOS);
                } catch (Exception E) {
                    Log.Write(this,E);
                    Log.Write(this,Arrays.toString(E.getStackTrace()));
                }
            }
        }         
    }
    
    public void loadToListener(String QName,Class<?> Parent,Object Ref) throws ClassNotFoundException{
        Log.Write(this,"Casting : " + QName);
        Class<?> Seq = Class.forName(QName);
        Catch.DRIVEAXLE((accelerator k)->{
            try {
                ((Accel) k).loadSequence((Sequencer) Seq.newInstance());
            }catch(Exception E){
                Log.Write(this,E);
            }
        });
        Catch.Activate(true);
    }
    
    public void rLoadToListener(String QName,Class<?> Parent,Object Ref) throws ClassNotFoundException{
        Log.Write(this,"Casting : " + QName);
        Class<?> Seq = Class.forName(QName);
        Catch.DRIVEAXLE((accelerator k)->{
            try {
                ((Accel) k).loadSequence((Sequencer) Seq.getConstructor(Parent).newInstance(new Object[] {Ref}));
            }catch(Exception E){
                Log.Write(this,E);
            }
        });
        Catch.Activate(true);
    }
   
    public ConnectionNet(IAddress To) throws Exception{
        this.Catch = new Listener(To);
        Catch.preLoad(Accel.class.getName());
        Catch.preLoad(this.getClass().getName(),this);
        Catch.Activate(true);
    }
}
