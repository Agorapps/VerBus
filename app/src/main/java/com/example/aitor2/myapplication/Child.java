package com.example.aitor2.myapplication;
/**
 * Created by Aitor on 07/03/2015.
 */
public class Child {

   private String tv_destino;
   private String tv_personas;
   private String tv_salida;
   private String tv_llegada;

    public Integer getId_viaje() {
        return id_viaje;
    }

    public void setId_viaje(Integer id_viaje) {
        this.id_viaje = id_viaje;
    }

    private Integer id_viaje;

   public String getTv_salida()
   {

      return tv_salida;
   }
   public void setTv_salida(String tv_salida)
   {

      this.tv_salida = tv_salida;
   }
   public String getTv_llegada()
   {

      return tv_llegada;
   }
   public void setTv_llegada(String tv_llegada)
   {

      this.tv_llegada = tv_llegada;
   }

   public String getTv_destino()
   {

      return tv_destino;
   }
   public void setTv_destino(String tv_destino)
   {

      this.tv_destino = tv_destino;
   }
   public String getTv_personas()
   {

      return tv_personas;
   }
   public void setTv_personas(String tv_personas)
   {

      this.tv_personas = tv_personas;
   }
}
