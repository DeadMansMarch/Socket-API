/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionNet;

import ConnectionNet.ConnectionNet.ChainPosition;
import SocketApi.CoveredSocket;

/**
 *
 * @author DeadMansMarch
 */
public interface Connectable {
    public ChainPosition Sequence(ChainPosition Type);
}
