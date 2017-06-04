/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import LOCAL.Log;
import static SocketApi.ProtocolParser.subProtocol.Get;
import static SocketApi.ProtocolParser.subProtocol.Serve;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author DeadMansMarch
 */
public class ProtocolParser {
    //Contains different protocol types.
    public enum Protocol{
        UNKNOWN, HTTP1_1, ForwardLess_1;
    }
    
    public enum subProtocol{
        Serve,Get;
    }
    
    //For errors in protocol syntax from client.
    public static class ProtocolSyntaxException extends Exception{
        public ProtocolSyntaxException(){
            super();
        }
        
        public ProtocolSyntaxException(String Cause){
            super(Cause);
        }
    }
    
    //Break protocol down into parameters given the protocol.
    public static ParsedProtocol parse(CoveredSocket Client, Protocol Proto,subProtocol SProto) throws IOException, ProtocolSyntaxException{
        ParsedProtocol Parse = new ParsedProtocol();
        switch (Proto){
            case HTTP1_1:
                switch(SProto){
                    case Get:
                        ArrayList<String> Protocol1_1Get = Client.blockRead();

                        Log.Write(ProtocolParser.class,String.format("HTTP1.1 : %1$d lines.",Protocol1_1Get.size()));
                        Parse.PTYPE = Proto;

                        String[] RD = Protocol1_1Get.get(0).split(" ");
                        if (RD.length >= 3){
                            Parse.Fill("STATUS", RD[1]);
                            Parse.Fill("STATUSMESSAGE",RD[2]);
                        }else{
                            Log.Write(ProtocolParser.class,Protocol1_1Get.get(0));
                            Client.close();
                            throw new ProtocolSyntaxException(Proto.toString());
                        }
                        
                        int Ind = 0;
                        for (String ProtoLine : Protocol1_1Get){
                            String[] Split = ProtoLine.split(":", 2);
                            if (Split.length > 1){ 
                                Parse.Fill(Split[0].toUpperCase().replaceAll("-", ""),Split[1]);
                                Ind++;
                            }else{
                                break;
                            }
                            
                        }
                        
                        for (int i = 1;i<Ind;i++){
                            Protocol1_1Get.remove(0);
                        }
                        
                        Parse.Fill("Body", Protocol1_1Get.stream().reduce((String A,String B)->A+B+"\n").get());

                        break;
                        
                    case Serve:
                        ArrayList<String> Protocol1_1Serve = Client.blockRead();

                        Log.Write(ProtocolParser.class,String.format("HTTP1.1 : %1$d lines.",Protocol1_1Serve.size()));
                        Parse.PTYPE = Proto;

                        String[] RDS = Protocol1_1Serve.get(0).split(" ");
                        if (RDS.length == 3){
                            Parse.Fill("RTYPE",RDS[0]);
                            Parse.Fill("PATH",RDS[1]);
                        }else{
                            Log.Write(ProtocolParser.class,Protocol1_1Serve.get(0));
                            Client.close();
                            throw new ProtocolSyntaxException(Proto.toString());
                        }

                        Protocol1_1Serve.remove(0);

                        for (String ProtoLine : Protocol1_1Serve){
                            String[] Split = ProtoLine.split(":", 2);
                            if (Split.length > 1){ 
                                Parse.Fill(Split[0].toUpperCase().replaceAll("-", ""),Split[1]);
                            }else{
                                break;
                            }
                            //if (ProtoLine.equals("\r\n") || ProtoLine.equals("\n")){
                            //    break;
                            //}
                        }

                        break;
                }
                break;
                
            case ForwardLess_1:
                ArrayList<String> ProtocolFL = Client.blockRead();
                Log.Write(ProtocolParser.class,String.format("ForwardLess_1 : %1$d lines.",ProtocolFL.size()));
                
                String[] FL0 = ProtocolFL.get(0).split(" ");
                if (FL0.length == 4){
                    Parse.Fill("CMD",FL0[0]);
                    Parse.Fill("QIP",FL0[1]);
                    Parse.Fill("RNAME",FL0[2]);
                }else if (FL0.length == 2){
                    Parse.Fill("CMD",FL0[0]);
                }else{
                    Log.Write(ProtocolParser.class,ProtocolFL.get(0));
                    throw new ProtocolSyntaxException(Proto.toString());
                }
                
                ProtocolFL.remove(0);
                
                for (String ProtoLine : ProtocolFL){
                    String[] Split = ProtoLine.split(":", 2);
                    if (Split.length > 1){ 
                        Parse.Fill(Split[0].toUpperCase().replaceAll("-", ""),Split[1]);
                    }
                    Log.Write(ProtoLine);
                }
                
                break;
        }
        return Parse;
    }
    
    public static ParsedProtocol parse(CoveredSocket Client,Protocol P) throws Exception{
        return parse(Client,P,Serve);
    }
    
    //Break down protocol into parameters given exchange. ((Could)) get protocol wrong.
    public static ParsedProtocol parse(CoveredSocket Client) throws Exception{
        Protocol Proto;
        
        Client.CINSTREAM.mark(100); //Take a look at the data without trashing it.
        while (!Client.CINSTREAM.ready()){
            Thread.sleep(50);
        }
        
        String PL = Client.baseRead();
        Client.CINSTREAM.reset();
        
        Log.Write(ProtocolParser.class, PL);
        
        for (Protocol K : Protocol.values()){
            String TP = PL.substring(PL.length() - (K.toString().length() + 1)).replaceAll("\\.", "_").replaceAll("/","");
            Log.Write(ProtocolParser.class,TP);
            if (K.toString().equals(TP)){
                return parse(Client,K);
            }
        }
        
        for (Protocol K : Protocol.values()){
            Log.Write(ProtocolParser.class,PL.substring(0,K.toString().length()));
            String TP = PL.substring(0,K.toString().length() + 1).replaceAll("\\.", "_").replaceAll("/","");
            if (K.toString().equals(TP)){
                return parse(Client,K,subProtocol.Get);
            }
        }
        
        Log.Write("No protocols match.");
        
        return null;
    }
}
