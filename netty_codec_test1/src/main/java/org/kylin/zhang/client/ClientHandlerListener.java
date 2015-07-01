package org.kylin.zhang.client;

import org.kylin.zhang.common.Message;

/**
 * Created by root on 7/1/15.
 */
public interface ClientHandlerListener {
    void messageReceived( Message msg ) ;
}
