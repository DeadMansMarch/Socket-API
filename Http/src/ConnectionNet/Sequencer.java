/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionNet;

import SocketApi.CoveredSocket;

/**
 *
 * @author DeadMansMarch
 */
public abstract class Sequencer implements Connectable {
    private CoveredSocket CLIENT;
    
    public void preLoad(CoveredSocket SEQ){
        this.CLIENT = SEQ;
    }
}
