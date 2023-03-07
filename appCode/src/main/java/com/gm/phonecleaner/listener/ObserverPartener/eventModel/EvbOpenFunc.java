package com.gm.phonecleaner.listener.ObserverPartener.eventModel;

import com.gm.phonecleaner.utils.Config;

public class EvbOpenFunc extends ObserverAction {
    public Config.FUNCTION mFunction;

    public EvbOpenFunc(Config.FUNCTION mFunction) {
        this.mFunction = mFunction;
    }
}
