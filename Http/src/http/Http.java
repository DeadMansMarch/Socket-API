/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import ConnectionNet.ConnectionNet;
import ConnectionNet.ConnectionNet.ChainPosition;
import ConnectionNet.Sequencer;
import FLSys.FLConnector;
import INET.IAddress;
import LOCAL.FileSys;
import LOCAL.Log;
import SocketApi.CoveredSocket;
import static SocketApi.CoveredSocket.FormatType.Dump;
import SocketApi.Listeners.Listener;
import SocketApi.Listeners.WebserverListener;
import SocketApi.Listeners.accelerator;
import SocketApi.ParsedProtocol;
import SocketApi.ProtocolCompiler;
import SocketApi.ProtocolParser;
import SocketApi.ProtocolParser.ProtocolSyntaxException;
import SocketApi.Senders.WebSender;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author DeadMansMarch
 */
public class Http{
    private ConnectionNet REF;
    private HashMap<String,Object> DataStore = new HashMap<>();
    private HashSet<String> Blacklist = new HashSet<>();
    public Http(ConnectionNet REF){
        this.REF = REF;
    }
    
    public Http(){
        
    }
    
    private enum Chain{
        Wait,WRelay,Done;
    }
    
    public class Connector extends Sequencer{
        public CoveredSocket CLIENT = null;
        Chain BLOCK = Chain.Wait;
        
        int CERL = 0;
        public Connector(){
            super();
        }
        
        private boolean Require(ParsedProtocol In, String... Requirements){
            for (String Require : Requirements){
                if (!In.Contains(Require)){
                    Log.Write(this,"Requirement failed :" + Require);
                    return false;
                }
            }
            return true;
        }
        
        private Chain cDie() throws IOException{
            Log.Write(this,"Initialization Error. Putting IP on blacklist.");
            Blacklist.add(CLIENT.whois());
            CLIENT.close();
            return BLOCK;
        }
        
        private Chain Connector() throws Exception{
            if (CLIENT.readReady()){
                ParsedProtocol FL;
                try{
                    FL = ProtocolParser.parse(CLIENT);
                }catch(ProtocolSyntaxException E){
                    Log.Write(this,"Initialization Error. Putting IP on blacklist.");
                    Log.Write(this,E);
                    return cDie();
                }
                
                switch(BLOCK){
                    case Wait:
                        Thread.sleep(300);

                        if (Require(FL,"CMD","QIP","RNAME") && FL.Get("CMD").equals("CNCUP")){
                            DataStore.put(CLIENT.describe() + "#LIP",FL.Get("QIP"));
                            DataStore.put(CLIENT.describe() + "#LNM",FL.Get("RNAME"));
                            Log.Write(this,String.format("SLAVE CONNECTION : %1$s %2$s",FL.Get("QIP"),FL.Get("RNAME")));
                            CLIENT.baseSend("INITOK\n");
                            return Chain.WRelay;
                        }else{
                            return cDie();
                        }
                        
                    case WRelay:
                        if (Require(FL,"CMD","ENCAPABLE","HOSTING")){
                            if (FL.Get("CMD").equals("RELAY")){
                                Log.Write(this,FL.Get("ENCAPABLE"));
                                return Chain.Done;
                            }else{
                                return cDie();
                            }
                        }else{
                            return cDie();
                        }
                }
            }   
            
            return BLOCK;
        }
        
        @Override
        public void preLoad(CoveredSocket SEQ){
            this.CLIENT = SEQ;
        }
        
        @Override
        public ChainPosition Sequence(ChainPosition Type) {
            
            //Log.Write(this,"Going...");
            try{
                if (Blacklist.contains(CLIENT.whois())){
                    CLIENT.close();
                    Log.Write(this,"PROBLEM : BLACKLISTED MEMBER REJOIN.");
                    exit(100);
                }
                switch(Type){
                    case Connecting:
                        Chain Rectify = Connector();
                        CERL += (Rectify == BLOCK) ? -CERL : 1;
                        BLOCK = Rectify;
                        if (CERL > 12){
                            Log.Write(this,"Client waited too long. Disconnecting.");
                            CLIENT.close();
                        }
                        if (BLOCK == Chain.Done){
                            return ChainPosition.Transmission;
                        }
                        break;
                    case Transmission:
                        
                        break;
                    default:
                        Log.Write(this, "Bueno");
                }
                
            }catch(Exception E){
                Log.Write(this,E);
                E.printStackTrace();
                try{
                    CLIENT.close();
                }catch(IOException Ex){
                    
                }
                    
            }
            
            return Type;
        }
    }
    
    public class Accel extends accelerator{
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
        }
        
        public void run(){
            try{
                ParsedProtocol Request;
                try {
                    Request = ProtocolParser.parse(CLIENT);
                } catch (Exception E) {
                    Log.Write(this,E.toString());
                    ProtocolCompiler.Compile(CLIENT, 400);
                    Log.Write(this,"Recieved malformed request.");
                    return;
                }

                String Path = Request.Get("PATH");
                CoveredSocket CLI = REF.getSTREAMING().get(0);
                CLI.baseSend("GIMME /Interactor.php?rType=sCap");
                
                do{
                    Thread.sleep(100);
                }while(!CLI.readReady());
                CLI.blockRead().forEach((String k)->{
                    try {
                        CLIENT.baseSend(k + "\n");
                    } catch (IOException ex) {
                        
                    }
                });
                
                
            }catch(Exception E){
                
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
        Log.Disable(ConnectionNet.class);
        Log.Disable(Listener.class);
        Log.Disable(Listener.Acceptor.class);
        ConnectionNet k = new ConnectionNet(new IAddress("localhost",8070));
        Http m = new Http(k);
        
        k.rLoadToListener(Connector.class.getName(),Http.class,m);
        
        Log.Write(Http.class,"Now connecting.");
        for (int i = 0;i<1;i++){
            try{
                FLConnector up = new FLConnector(new IAddress("localhost",8070),80);
            }catch(Exception E){
                Log.Write(FLConnector.class, E);
                E.printStackTrace();
            }
        }
        
        Thread.sleep(1000);
        
        WebserverListener Serv = new WebserverListener(new IAddress("localhost",8081));
        Serv.preLoad(Accel.class.getName());
        Serv.preLoad(Http.class.getName(), m);
        Serv.Activate(true);
       
        
    }
}
