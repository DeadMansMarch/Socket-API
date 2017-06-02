/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import static SocketApi.CoveredClient.FormatType.NewLine;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class ProtocolCompiler {
    public static HashMap<Integer,String> StatusFlags = new HashMap<>(); static {{
        StatusFlags.put(200,"OK");
        StatusFlags.put(400,"Bad Request");
        StatusFlags.put(401,"Unauthorized");
        StatusFlags.put(403,"Forbidden");
        StatusFlags.put(404,"Not Found");
        StatusFlags.put(408,"Request Timeout");
    }};
    
    public static HashMap<String,String> EndingTypes = new HashMap<>(); static {{
        EndingTypes.put(".html","text/html");
        EndingTypes.put(".php","text/html");
        EndingTypes.put(".css","text/css");
        EndingTypes.put(".png","image/webp");
        EndingTypes.put(".jpg","image/webp");
        EndingTypes.put(".jpeg","image/webp");
        EndingTypes.put(".ico","image/webp");
        EndingTypes.put(".gif","image/webp");
        EndingTypes.put(".webm","image/webp");
    }};
    public ProtocolCompiler(){
        
    }
    
    public static void Compile(CoveredClient Client, int Status,String FileName) throws IOException{
        Compile(Client,Status);
        String FileEnding = FileName.substring(FileName.lastIndexOf("."));
        Client.formatSend("Content-Type: " + EndingTypes.get(FileEnding), NewLine);
        Client.baseSend("\n");
    }
    
    public static void Compile(CoveredClient Client, int Status) throws IOException{
        Client.formatSend("HTTP/1.1 " + Status + " " + StatusFlags.get(Status), NewLine);
        Client.formatSend("Server:LMX", NewLine);
    }
}
