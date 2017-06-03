/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Listeners;

import INET.IAddress;
import INET.Site;
import LOCAL.FileSys;
import SocketApi.CoveredSocket;
import static SocketApi.CoveredSocket.FormatType.Dump;
import SocketApi.Listeners.WebserverListener.Accel;
import SocketApi.ParsedProtocol;
import SocketApi.ProtocolCompiler;
import SocketApi.ProtocolParser;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;

/**
 *
 * @author DeadMansMarch
 */
public class WebserverListener extends Listener {
    
    Site LOADEDSITE = new Site();
    ArrayList<Thread> THREADER = new ArrayList<>();
    
    //Can host webservers. Pretty low quality server atm.
    public WebserverListener(IAddress To) throws IOException, ClassNotFoundException{
        super(To);
        super.preLoad(this.getClass().getName(), this);
        
        try{
            this.preLoad(Accel.class.getName());
        }catch(ClassNotFoundException E){
            System.out.println(E);
            exit(3);
        }
    }
    
    //Will load a Site object from package INET for use in the server.
    public void loadSite(Site LOAD){
        this.LOADEDSITE = LOAD;
    }
    
    //Webserver client intercept.
    public class Accel extends Listener.accelerator{
        public Accel(CoveredSocket CLIENT){
            super(CLIENT);
        }
        
        public void run(){
            try{
                ParsedProtocol Request;
                try {
                    Request = ProtocolParser.parse(CLIENT);
                } catch (Exception E) {
                    System.out.println(E.toString());
                    ProtocolCompiler.Compile(CLIENT, 400);
                    System.out.println("Recieved malformed request.");
                    return;
                }

                String Path = Request.Get(ParsedProtocol.DataType.PATH);
                Path = (Path.equals("/")) ? Path + LOADEDSITE.DEFAULT : Path;
                String Site = LOADEDSITE.getFile(Path);
                String File = Path.substring(Path.lastIndexOf("/") + 1);
                String FileRel = FileSys.load(Site);
                if (Site == null) {
                    ProtocolCompiler.Compile(CLIENT, 404);
                    CLIENT.close();
                }
                Site = ProtocolCompiler.Compile(CLIENT, 200, File, FileRel);
                if (Site != null){
                    System.out.println(Site);
                    CLIENT.formatSend(FileRel, Dump);
                }else{
                    System.out.println(LOADEDSITE.getFile(Path));
                    FileSys.streamImage(LOADEDSITE.getFile(Path), CLIENT.getOutstream());
                }
                
                CLIENT.close();
            }catch(Exception E){
                System.out.println(E);
            }
        }
    }
}
