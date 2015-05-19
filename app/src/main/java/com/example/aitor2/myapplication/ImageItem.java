package com.example.aitor2.myapplication;

import android.util.Log;

import java.util.Date;

/**
 * Created by aitor2 on 08/04/2015.
 */
public class ImageItem {
    private String gNombreEvento;
    private String gHoraSalida;

    public String getgPersonas() {

        return gPersonas;
    }

    public void setgPersonas(String gPersonas) {
        this.gPersonas = gPersonas;
    }

    private String gPersonas;

    public int getGidViaje() {
        return gidViaje;
    }

    public void setGidViaje(int gidViaje) {
        this.gidViaje = gidViaje;
    }

    private int gidViaje;

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    private String idReserva;

    public String getgHoraLlegada() {
        return gHoraLlegada;
    }

    public void setgHoraLlegada(String gHoraLlegada) {
        this.gHoraLlegada = gHoraLlegada;
    }

    private String gHoraLlegada;
    private Date gFecha;

    public Date getgFecha() {
        return gFecha;
    }

    public void setgFecha(Date gFecha) {
        this.gFecha = gFecha;
    }

    public String getgNombreEvento() {
        return gNombreEvento;
    }

    public void setgNombreEvento(String gNombreEvento) {
        this.gNombreEvento = gNombreEvento;
    }



    public String getgHoraSalida() {
        return gHoraSalida;
    }

    public void setgHoraSalida(String gHoraSalida) {
        this.gHoraSalida = gHoraSalida;
    }



    public ImageItem(String gNombreEvento, String gHoraSalida, Date gFecha, String gHoraLlegada, String idReserva, int gidViaje, String gPersonas ) {
        super();
        this.gNombreEvento = gNombreEvento;
        this.gHoraSalida = gHoraSalida;
        this.gFecha = gFecha;
        this.gHoraLlegada =gHoraLlegada;
        this.idReserva = idReserva;
        this.gidViaje=gidViaje;
        this.gPersonas=gPersonas;
    }


}
