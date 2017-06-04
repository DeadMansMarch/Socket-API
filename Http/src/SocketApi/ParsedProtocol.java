/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import LOCAL.Log;
import SocketApi.ProtocolParser.Protocol;
import static SocketApi.ProtocolParser.Protocol.UNKNOWN;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class ParsedProtocol {
    //Protocol parameters this webserver understands.
    public enum DataType{
        RTYPE,PATH,HOST,USERAGENT,ACCEPT,DNT,CONNECTION,REFERER,ACCEPTENCODING,ACCEPTLANGUAGE,
        UPGRADEINSECUREREQUESTS,CACHECONTROL,PRAGMA;
    }
    
   
    
    public HashMap<String,String> Data = new HashMap<>();
    public Protocol PTYPE = UNKNOWN;
    
    //Adds a parameter protocol
    public void Fill(String toFill,String with){
        try{
            Data.put(toFill, with);
        }catch(Exception E){
            Log.Write(ParsedProtocol.class,E.getMessage());
        }
    }
    
    public boolean Contains(String Cont){
        return Data.containsKey(Cont);
    }
    
    //Returns a specific data parameter from protocol.
    public String Get(String toGet){
        return Data.get(toGet);
    }
    
    
}
