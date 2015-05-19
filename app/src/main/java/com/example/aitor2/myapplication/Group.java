package com.example.aitor2.myapplication;
/**
 * Created by Aitor on 07/03/2015.
 */
import java.util.ArrayList;
import java.util.Date;

public class Group {

   private String tv_evento;
   private Date tv_fecha;
   private ArrayList<Child> Items;
   public Date getTv_fecha()
   {

      return tv_fecha;
   }
   public void setTv_fecha(Date tv_fecha)
   {

      this.tv_fecha = tv_fecha;
   }
   public String getTv_evento() {
      return tv_evento;
   }

   public void setTv_evento(String name) {
      this.tv_evento = name;
   }

   public ArrayList<Child> getItems() {
      return Items;
   }

   public void setItems(ArrayList<Child> Items) {
      this.Items = Items;
   }

}
