/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import org.omg.CORBA.Any;

/**
 *
 * @author DeadMansMarch
 */

//Contains items relevant to client interaction.
public class CoveredSocket {
    public Socket CLIENT;
    protected OutputStream OUTSTREAM;
    protected InputStreamReader INSTREAM;
    protected DataOutputStream COUTSTREAM;
    protected BufferedReader CINSTREAM;
    public Thread REFER;
    public CoveredSocket(Socket CLIENT) throws IOException{
        this.CLIENT = CLIENT;
        
        OUTSTREAM = CLIENT.getOutputStream(); 
        COUTSTREAM = new DataOutputStream(OUTSTREAM);
        
        INSTREAM = new InputStreamReader(CLIENT.getInputStream());
        CINSTREAM = new BufferedReader(INSTREAM);
    }
    
    public OutputStream getOutstream(){
        return OUTSTREAM;
    }
    
    public void close() throws IOException{
        REFER.interrupt();
        CLIENT.close();
    }
    
    public void baseSend(String Data) throws IOException{
        
        byte[] byteData = Data.getBytes();
        if (byteData.length > 0) {
            COUTSTREAM.write(byteData, 0, byteData.length);
        }
    }
    
    public String baseRead() throws IOException{
        if (CINSTREAM.ready()) return CINSTREAM.readLine();
        throw new IOException("Reading empty stream.");
    }
    
    public enum FormatType{
        Dump,NewLine,LenMat;
    }
    
    public void formatSend(String Data, FormatType Format) throws IOException{
        switch (Format){
            case Dump:
                this.baseSend(Data);
                break;
            case NewLine:
                this.baseSend(Data + "\n");
                break;
            case LenMat:
                this.baseSend(Data.getBytes().length + Data);
                break;
        }
    }
    
    public ArrayList<String> blockRead() throws IOException{
        ArrayList<String> Block = new ArrayList<>();
        while (true){
            if (CINSTREAM.ready()) Block.add(CINSTREAM.readLine());
            else break;
        }
        return Block;
    }
    
    public String describe(){
        return CLIENT.getInetAddress() + ":" + CLIENT.getPort();
    }
}
