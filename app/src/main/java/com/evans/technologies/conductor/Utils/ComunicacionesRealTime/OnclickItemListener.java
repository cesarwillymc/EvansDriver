package com.evans.technologies.conductor.Utils.ComunicacionesRealTime;


import com.evans.technologies.conductor.model.chats;
import com.evans.technologies.conductor.model.notification.notification;

import java.util.Map;

public interface    OnclickItemListener {
     void itemClickNotify(notification notifica, int adapterPosition);
    void itemClickChat(chats chat, int adapterPosition);
}
