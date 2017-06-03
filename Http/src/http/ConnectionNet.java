/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import INET.IAddress;
import SocketApi.CoveredSocket;
import SocketApi.Listeners.Listener;
import SocketApi.Listeners.accelerator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DeadMansMarch
 */
public class ConnectionNet {
    
    private ArrayList<CoveredSocket> CONNECTED = new ArrayList<>();
    
    public int POSBREAK = 0;
    
    enum ChainPosition{
        Connecting,Idle,Transmission;
    }
    
    public class Accel extends accelerator{
        
        ChainPosition BLOCKPOS = ChainPosition.Connecting;
        int POSBREAKER = 0;
        
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
            CONNECTED.add(CLIENT);
        }
        
        public void run() {
            while (true){
                try {
                    switch(BLOCKPOS){
                        case Connecting:
                            if (!this.CLIENT.readReady()){
                                POSBREAKER++;
                                if (POSBREAKER > POSBREAK){
                                    System.out.println("Client never ready. Disconnecting.");
                                    this.CLIENT.baseSend("DISCONNECT\n");
                                    this.CLIENT.close();
                                    return;
                                }
                                break;
                            }else{
                                String Message = this.CLIENT.baseRead();
                                System.out.println(Message);
                            }
                            
                            break;
                        case Idle:
                            break;
                        case Transmission:
                            break;
                    }
                    Thread.sleep(1000);
                } catch (Exception E) {
                    
                }
            }
        }         
    }
   
    public ConnectionNet() throws Exception{
        
        IAddress Local = new IAddress("localhost",8081);
        
        Listener Catch = new Listener(Local);
        Catch.preLoad(Accel.class.getName());
        Catch.preLoad(this.getClass().getName(),this);
        Catch.Activate(true);
    }
}
