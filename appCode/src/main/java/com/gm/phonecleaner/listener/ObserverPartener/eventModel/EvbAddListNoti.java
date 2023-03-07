package com.gm.phonecleaner.listener.ObserverPartener.eventModel;

import com.gm.phonecleaner.model.NotifiModel;

import java.util.List;

public class EvbAddListNoti extends ObserverAction {
    public List<NotifiModel> lst;

    public EvbAddListNoti(List<NotifiModel> lst) {
        this.lst = lst;
    }

}
