/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Listeners;

import INET.IAddress;
import INET.Site;
import SocketApi.CoveredClient;
import static SocketApi.CoveredClient.FormatType.Dump;
import static SocketApi.CoveredClient.FormatType.LenMat;
import static SocketApi.CoveredClient.FormatType.NewLine;
import SocketApi.ParsedProtocol;
import SocketApi.ProtocolCompiler;
import SocketApi.ProtocolParser;
import java.io.IOException;
import static java.lang.System.exit;
import java.util.ArrayList;
import org.omg.CORBA.Any;

/**
 *
 * @author DeadMansMarch
 */
public class WebserverListener extends Listener {
    
    Site LOADEDSITE = new Site();
    ArrayList<Thread> THREADER = new ArrayList<>();
    
    public WebserverListener(IAddress To) throws IOException{
        super(To);
        super.Acceptor = OAcceptor;
    }
    
    public void loadSite(Site LOAD){
        this.LOADEDSITE = LOAD;
    }
    
    Thread OAcceptor = new Thread(new Runnable(){
        public void run(){
            System.out.println("Listening.");
            while (true){
                try{
                    CoveredClient Client = new CoveredClient(Listen.accept()); //Newing isn't great.
                    Client.REFER = new Thread(new Accel(Client));
                    Client.REFER.start();
                }catch(IOException E){
                    System.out.println(E.getMessage());
                    exit(2);
                }
            }
        }
    });
    
    class Accel implements Runnable{
        CoveredClient CLIENT;
        public Accel(CoveredClient CLIENT){
            this.CLIENT = CLIENT;
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
                System.out.println(Path);
                String Site = LOADEDSITE.getFile(Path);
                if (Site == null) {
                    ProtocolCompiler.Compile(CLIENT, 404);
                    CLIENT.close();
                }

                ProtocolCompiler.Compile(CLIENT, 200, Path.substring(Path.lastIndexOf("/") + 1));
                
                CLIENT.formatSend(Site, Dump);

                CLIENT.close();
            }catch(Exception E){
                System.out.println(E);
            }
        }
    }
}
