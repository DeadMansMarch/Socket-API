/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketApi.Listeners;

import SocketApi.CoveredSocket;

/**
 *
 * @author DeadMansMarch
 */
public abstract class accelerator implements Runnable {
    protected CoveredSocket CLIENT;
    public accelerator(CoveredSocket CLIENT){
        this.CLIENT = CLIENT;
    }
}
