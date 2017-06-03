/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Senders;

import INET.IAddress;
import SocketApi.CoveredSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author DeadMansMarch
 */
public class Sender {
    
    public Sender(){
    }
    
    public CoveredSocket connect(IAddress To) throws UnknownHostException, IOException{
        Socket Connection = new Socket(To.PrepINET(),To.getPORT());
        return new CoveredSocket(Connection);
    }
    
    public CoveredSocket connectSSL(IAddress To) throws UnknownHostException, IOException{
        Socket Connection = new Socket(To.PrepINET(),To.getPORT());
        return new CoveredSocket(Connection);
    }
}
