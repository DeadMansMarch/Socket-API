/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author DeadMansMarch
 */
public class ProtocolParser {
    public enum Protocol{
        UNKNOWN, HTTP1_1;
    }
    
    public static class ProtocolSyntaxException extends Exception{
        public ProtocolSyntaxException(){
            super();
        }
        
        public ProtocolSyntaxException(String Cause){
            super(Cause);
        }
    }
    
    public static ParsedProtocol parse(CoveredClient Client, Protocol Proto) throws IOException, ProtocolSyntaxException{
        ParsedProtocol Parse = new ParsedProtocol();
        switch (Proto){
            case HTTP1_1:
                ArrayList<String> Protocol = Client.blockRead();
                System.out.println(String.format("HTTP1.1 : %1$d lines.",Protocol.size()));
                Parse.PTYPE = Proto;
                
                String[] RD = Protocol.get(0).split(" ");
                if (RD.length == 3){
                    Parse.Fill("RTYPE",RD[0]);
                    Parse.Fill("PATH",RD[1]);
                }else{
                    System.out.println(Protocol.get(0));
                    Client.close();
                    throw new ProtocolSyntaxException(Proto.toString());
                }
                
                Protocol.remove(0);
                
                for (String ProtoLine : Protocol){
                    String[] Split = ProtoLine.split(":", 2);
                    if (Split.length > 1){ 
                        Parse.Fill(Split[0].toUpperCase().replaceAll("-", ""),Split[1]);
                    }
                }
                
                break;
        }
        return Parse;
    }
    
    public static ParsedProtocol parse(CoveredClient Client) throws Exception{
        Protocol Proto;
        
        Client.CINSTREAM.mark(1); //Take a look at the data without trashing it.
        while (!Client.CINSTREAM.ready()){
            Thread.sleep(50);
        }
        String PL = Client.baseRead();
        Client.CINSTREAM.reset();
        
        String PR = PL.substring(PL.length() - 8, PL.length());
        String k = PR.replaceAll("\\.", "_").replaceAll("/","");
        Proto = Protocol.valueOf(k);
        
        return parse(Client,Proto);
    }
}
