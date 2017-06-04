/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Senders;

import INET.IAddress;
import LOCAL.FileSys;
import LOCAL.Log;
import SocketApi.CoveredSocket;
import SocketApi.ProtocolCompiler;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author DeadMansMarch
 */
public class WebSender extends Sender{
    public WebSender(){
        
    }
    
    public String[] getPage(IAddress Page,String FromRoot,int Wait) throws UnknownHostException, IOException, InterruptedException{
        Log.Write(this,"Getting.");
        CoveredSocket Connection = this.connect(Page);
        ProtocolCompiler.CompileGET(Connection, Page, FromRoot);
        if (Wait < 0){
            while (!Connection.readReady()){
                Thread.sleep(60);
            }
        }else{
            Thread.sleep(Wait);
        }
        if (Connection.readReady()){
            ArrayList<String> Con = Connection.blockRead();
            String File = Con.stream().reduce((String L,String M)->L+M + "\r\n").get();
            String[] Ini = File.split("\r\n\r\n",2);
            return Ini;
        }else{
            return new String[0];
        }
    }
    
    public String[] getPage(IAddress Page,String FromRoot) throws UnknownHostException, IOException, InterruptedException{
        return this.getPage(Page, FromRoot,-1);
    }
    
    public void getImage(IAddress Page,String FromRoot) throws UnknownHostException, IOException, InterruptedException{
        String File = getPage(Page,FromRoot)[1];
        FileSys.save("file.jpg", File);
    }
}
