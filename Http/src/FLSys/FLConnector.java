/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FLSys;

import INET.IAddress;
import LOCAL.Log;
import SocketApi.CoveredSocket;
import SocketApi.Senders.Sender;
import SocketApi.Senders.WebSender;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author DeadMansMarch
 */
public class FLConnector {
    private Sender CNUP;
    private CoveredSocket CSUP;
    private WebSender LC = new WebSender();
    private IAddress LOCAL = new IAddress("localhost",8080);
    
    Runnable MainThread = new Runnable(){
        @Override
        public void run(){
            try{
                CSUP.baseSend("CNCUP " + InetAddress.getLocalHost().toString());
                CSUP.baseSend(" [" + System.getProperty("user.name").replaceAll(" ","_") + "]");
                CSUP.baseSend(" ForwardLess_1\n");
                CSUP.baseSend("LINKING: LIVE\n");
                CSUP.baseSend("\r\n\r\n");
                Log.Write(this,"Initiator Sent");

                while (true){
                    if (CSUP.readReady()){
                        String RQST = CSUP.baseRead();
                        String[] Data = RQST.split(" ");
                        Log.Write(this,"RECIEVE : " + RQST);
                        switch(Data[0]){
                            case "INITOK":
                                Log.Write(this,"Init Succeeded. Encryption check.");
                                String[] LocalUp = LC.getPage(LOCAL,"/",60);
                                Thread.sleep(100);

                                CSUP.baseSend("RELAY ForwardLess_1\n");
                                CSUP.baseSend("EnCapable: None\n");
                                CSUP.baseSend("Hosting: ");
                                if (!(LocalUp.length > 0)){
                                    CSUP.baseSend("NO\n");
                                }else{
                                    CSUP.baseSend("YES\n");
                                }

                                break;
                            case "GIMME":
                                Data = LC.getPage(LOCAL,Data[1]);
                                Arrays.asList(Data).forEach((String k)->{
                                    Arrays.asList(k.split("\r\n")).forEach((String m)->{
                                        try{
                                            CSUP.baseSend(m + "\r\n");
                                        }catch(Exception E){
                                            
                                        }
                                    });
                                });
                                break;
                        }
                    }
                }
            }catch(Exception E){
                Log.Write(this,E);
                E.printStackTrace();
            }
        }
    };
    
    class UnconfiguredServerException extends Exception{
        public UnconfiguredServerException(String Reason){
            super(Reason);
        }
    }
    
    //Will connect to IAddress To as public connection, and localhost:LocalPort.
    //Forwardless servers pushed to forwarded servers.
    public FLConnector(IAddress To,int LocalPort) throws Exception{
        this.CNUP = new Sender();
        CSUP = CNUP.connect(To);
        Thread.sleep(400);
        Log.Write(this, "Live");
        if (CSUP.readReady()){
            CSUP.close();
            throw new UnconfiguredServerException("Server responded.");
        }else{
            Thread Main = new Thread(MainThread);
            Main.start();
        }
        
    }
}
