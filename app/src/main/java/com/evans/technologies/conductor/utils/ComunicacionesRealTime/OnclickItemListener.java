package com.evans.technologies.conductor.utils.ComunicacionesRealTime;


import com.evans.technologies.conductor.model.chats;
import com.evans.technologies.conductor.model.notification.notification;

public interface    OnclickItemListener {
     void itemClickNotify(notification notifica, int adapterPosition);
    void itemClickChat(chats chat, int adapterPosition);
}
