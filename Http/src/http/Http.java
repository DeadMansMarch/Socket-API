/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import INET.IAddress;
import INET.Site;
import SocketApi.Listeners.WebserverListener;
import static java.lang.System.exit;
import java.io.File;
import java.net.URISyntaxException;
/**
 *
 * @author DeadMansMarch
 */
public class Http{
    
    public static void main(String[] args) throws URISyntaxException {
        IAddress Local = new IAddress("localhost",8090);
        Site Map = new Site();
        String Location = new File(Http.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
        String L = Location.substring(0,Location.lastIndexOf("/"));
        Map.addFile("/default.html",L + "/ServerFiles/default.html");
        Map.addFile("/text.png",L + "/ServerFiles/Yes.png");
        Map.addFile("/favicon.ico",L + "/ServerFiles/favicon.ico");
        Map.addFile("/LightFlower.jpg",L + "/ServerFiles/Flower.jpg");
        
        try{
            WebserverListener Main = new WebserverListener(Local);
            Main.loadSite(Map);
            Main.Activate(true);
            System.out.println("Activated.");
        }catch(Exception E){
            System.out.println("Error Initializing : " + E.getMessage());
            exit(1);
        }
        
        
    }
}
