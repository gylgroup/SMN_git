package com.example.smn.smn.ajustes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mbraidot on 24/07/2015.
 */
public class Ajustes {

    private static SharedPreferences ajustes;
    private static SharedPreferences.Editor editor;
    //private Context context;

    public Ajustes(Context context){
        ajustes = context.getSharedPreferences("AjustesSMN", context.MODE_PRIVATE);
        editor = ajustes.edit();
    }

    /**
     * getter para unidades
     * @return boolean
     */
    public static Boolean getUnidades(){
        return ajustes.getBoolean("unidades", false);
    }
    /**
     * setter para unidades
     * @param newUnidades
     * @param commit realizar el commit en este momento
     */
    public static void setUnidades(Boolean newUnidades, Boolean commit){
        editor.putBoolean("unidades", newUnidades);
        if(commit) {
            editor.commit();
        }
    }
    /**
     * getter para notificaciones
     * @return boolean
     */
    public static Boolean getNotificaciones(){
        return ajustes.getBoolean("notificaciones", true);
    }
    /**
     * setter para notificaciones
     * @param newNotificaciones
     * @param commit realizar el commit en este momento
     */
    public static void setNotificaciones(Boolean newNotificaciones, Boolean commit){
        editor.putBoolean("notificaciones", newNotificaciones);
        if(commit) {
            editor.commit();
        }

    }
    /**
     * getter para ciudades
     * @return boolean
     */
    public static Set<String> getCiudades(){
        Set<String> resultado = ajustes.getStringSet("ciudades", new HashSet<String>());
        return resultado;
    }
    /**
     * setter para ciudades
     * @param newCiudades
     * @param commit realizar el commit en este momento
     */
    public static void setCiudades(Set<String> newCiudades, Boolean commit){

        editor.putStringSet("ciudades", newCiudades);
        //editor.putBoolean("ciudades", newCiudades);
        if(commit) {
            editor.commit();
        }
    }

    /**
     * Agrega el id de una nueva ciudad al conjunto
     *
     * @param nuevo_id Id de la ciudad en BD
     * @return True en caso de exito, y false en caso de entrada repetida
     */
    public boolean agregarCiudad(String nuevo_id){

        Set<String> ciudades = getCiudades();
        Iterator iCiudades = (Iterator) ciudades.iterator();
        boolean no_encontrado = false;
        while(iCiudades.hasNext()){
            String id = (String) iCiudades.next();
            // si la ciudad no existe ya en la lista, la agrego
            if(id.equals(""+nuevo_id+"") ){
                no_encontrado = true;

            }
        }
        if(!no_encontrado){
            // Agregar el id de la nueva ciudad a los ajustes
            String nueva_ciudad = ""+nuevo_id+"";
            ciudades.add(nueva_ciudad);
            setCiudades(ciudades, true);

            return true;
        }else{
            return false;
        }

    }


    public void quitarCiudad(String id){

        Set<String> ciudades = getCiudades();
        String quitar_ciudad = ""+id+"";
        ciudades.remove(quitar_ciudad);
        setCiudades(ciudades, true);

    }


    public void commit(){
        editor.commit();
    }


}
