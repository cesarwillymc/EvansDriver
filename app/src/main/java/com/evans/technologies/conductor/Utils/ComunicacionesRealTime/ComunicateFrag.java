package com.evans.technologies.conductor.Utils.ComunicacionesRealTime;

import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

public interface ComunicateFrag {
    interface mapa_inicio{
        void updatefrag(Boolean checked);
        void mensajeGet(@NotNull DataSnapshot p0);
    }
    interface  chat{
        void sendDtaMessage(DataSnapshot snapshot);
    }

}
