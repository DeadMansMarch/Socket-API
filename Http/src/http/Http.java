/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import ConnectionNet.Connectable;
import ConnectionNet.ConnectionNet;
import INET.IAddress;
import INET.Site;
import SocketApi.CoveredSocket;
import SocketApi.Listeners.Listener;
import SocketApi.Listeners.WebserverListener;
import SocketApi.Senders.WebSender;
import static java.lang.System.exit;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
/**
 *
 * @author DeadMansMarch
 */
public class Http{
    ConnectionNet REF;
    
    public Http(ConnectionNet REF){
        this.REF = REF;
    }
    
    public Http(){
        
    }
    
    private enum Chain{
        
    }
    
    public class Connector implements Connectable{
        int CERL = 0;
        public Connector(){
            super();
        }
        @Override
        public void Sequence(CoveredSocket SEQ) {
            try{
                if (SEQ.readReady()){
                    System.out.println(SEQ.baseRead());
                }else{
                    CERL++;
                }
                
                if (CERL > 12){
                    System.out.println("FATAL CONNECTION ERROR");
                    SEQ.close();
                    SEQ.CLIENT.close();
                }
            }catch(Exception E){
                System.out.println(E.getStackTrace());
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
        System.out.println(Arrays.toString(Connector.class.getConstructors()));
        ConnectionNet k = new ConnectionNet();
        Http m = new Http(k);
        
        k.rLoadToListener(Connector.class.getName(),Http.class,m);
        
        System.out.println("Ok, ready.");
    }
}
