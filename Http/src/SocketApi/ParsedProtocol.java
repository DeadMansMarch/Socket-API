/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import SocketApi.ProtocolParser.Protocol;
import static SocketApi.ProtocolParser.Protocol.UNKNOWN;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class ParsedProtocol {
    public enum DataType{
        RTYPE,PATH,HOST,USERAGENT,ACCEPT,DNT,CONNECTION,REFERER,ACCEPTENCODING,ACCEPTLANGUAGE,
        UPGRADEINSECUREREQUESTS,CACHECONTROL,PRAGMA;
    }
    
    public HashMap<DataType,String> Data = new HashMap<>();
    public Protocol PTYPE = UNKNOWN;
    
    public ParsedProtocol(){
        
    }
    
    public void Fill(DataType toFill,String with){
        Data.put(toFill,with);
    }
    
    public void Fill(String toFill,String with){
        try{
            Fill(DataType.valueOf(toFill),with);
        }catch(Exception E){
            System.out.println(E.getMessage());
        }
    }
    
    public String Get(DataType toGet){
        return Data.get(toGet);
    }
    
    public String Get(String toGet){
        return Get(DataType.valueOf(toGet));
    }
    
    
}
