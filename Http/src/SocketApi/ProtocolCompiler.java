/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import INET.IAddress;
import LOCAL.FileSys;
import static SocketApi.CoveredSocket.FormatType.NewLine;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author DeadMansMarch
 */
public class ProtocolCompiler {
    
    //Status codes.
    public static HashMap<Integer,String> StatusFlags = new HashMap<>(); static {{
        StatusFlags.put(200,"OK");
        StatusFlags.put(400,"Bad Request");
        StatusFlags.put(401,"Unauthorized");
        StatusFlags.put(403,"Forbidden");
        StatusFlags.put(404,"Not Found");
        StatusFlags.put(408,"Request Timeout");
    }};
    
    //MIME Endings.
    public static HashMap<String,String> EndingTypes = new HashMap<>(); static {{
        EndingTypes.put(".html","text/html");
        EndingTypes.put(".php","text/html");
        EndingTypes.put(".css","text/css");
        EndingTypes.put(".png","image/png");
        EndingTypes.put(".jpg","image/jpeg");
        EndingTypes.put(".jpeg","image/jpeg");
        EndingTypes.put(".ico","image/webp");
        EndingTypes.put(".gif","image/gif");
        EndingTypes.put(".webm","image/webm");
    }};
    
    //Generalized file types.
    public static HashMap<String,String> Types = new HashMap<>(); static {{
        Types.put(".html","text");
        Types.put(".php","text");
        Types.put(".css","css");
        Types.put(".png","image");
        Types.put(".jpg","image");
        Types.put(".jpeg","image");
        Types.put(".ico","image");
        Types.put(".gif","image");
        Types.put(".webm","image");
    }};
    
    //Will make a set of protocol headers.
    public static String Compile(CoveredSocket Client, int Status,String FileName, String Page) throws IOException{
        Compile(Client,Status);
        String FileEnding = FileName.substring(FileName.lastIndexOf("."));
        Client.formatSend("Content-Type: " + EndingTypes.get(FileEnding), NewLine);
        boolean isImage = Types.get(FileEnding).equals("image");
        if (isImage){
            Client.baseSend("\r\n\r\n");
            return null;
        }
        Client.baseSend("\r\n\r\n");
        return Page;
    }
    
    //Will make a set of protocol headers.
    public static void Compile(CoveredSocket Client, int Status) throws IOException{
        Client.formatSend("HTTP/1.1 " + Status + " " + StatusFlags.get(Status), NewLine);
        Client.formatSend("Server:LMX", NewLine);
    }
    
    public static void CompileGET(CoveredSocket Server, IAddress Location,String Page) throws IOException{
        Server.formatSend("GET " + Page + " HTTP/1.1",NewLine);
        Server.formatSend("Host: " + Location.getADDRESS(),NewLine);
        Server.formatSend("Connection: Keep-Alive",NewLine);
        Server.baseSend("\n");
    }
}
