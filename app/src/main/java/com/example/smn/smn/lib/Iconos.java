package com.example.smn.smn.lib;

import com.example.smn.smn.R;

/**
 * Created by mbraidot on 14/09/2015.
 */
public class Iconos {

    public Integer getDrawableById(String id){

        Integer resourse = null;
        switch (id){

            case "1":
                resourse = R.drawable.dia_neviza_viento;
                break;

            case "2":
                resourse = R.drawable.dia_nubosidad_alta;
                break;

            case "3":
                resourse = R.drawable.dia_nubosidad_alta_con_nubarrones;
                break;

            case "4":
                resourse = R.drawable.dia_nubosidad_alta_con_nubarrones_y_nieve;
                break;

            case "5":
                resourse = R.drawable.dia_nubosidad_alta_con_tormentas;
                break;

            case "6":
                resourse = R.drawable.dia_nubosidad_baja;
                break;

            case "7":
                resourse = R.drawable.dia_nubosidad_baja_con_nubarrones;
                break;

            case "8":
                resourse = R.drawable.dia_nubosidad_media;
                break;

            case "9":
                resourse = R.drawable.dia_nubosidad_media_con_nubarrones;
                break;

            case "10":
                resourse = R.drawable.dia_nubosidad_media_con_tormentas;
                break;

            case "11":
                resourse = R.drawable.dia_nubosidad_muy_baja;
                break;

            case "12":
                resourse = R.drawable.dia_nubosidad_nubarrones_media;
                break;

            case "13":
                resourse = R.drawable.dia_nubosidad_total;
                break;

            case "14":
                resourse = R.drawable.dia_nubosidad_total_lluvia_fina;
                break;

            case "15":
                resourse = R.drawable.dia_nubosidad_total_lluvia_fuerte;
                break;

            case "16":
                resourse = R.drawable.dia_nubosidad_total_lluvia_piedra;
                break;

            case "17":
                resourse = R.drawable.dia_nubosidad_total_nieve;
                break;

            case "18":
                resourse = R.drawable.dia_nubosidad_total_nubarrones_lluvia_fina;
                break;

            case "19":
                resourse = R.drawable.dia_nubosidad_total_piedra;
                break;

            default:
                resourse = R.drawable.dia_soleado;
                break;

        }
        return resourse;

    }

    public Integer getNuevoScrollerIdPorPosicion(int i){

        Integer resourse = null;
        switch (i) {

            case 0:
                resourse = R.id.scroll_dia_0;
                break;
            case 1:
                resourse = R.id.scroll_dia_1;
                break;
            case 2:
                resourse = R.id.scroll_dia_2;
                break;
            case 3:
                resourse = R.id.scroll_dia_3;
                break;
            case 4:
                resourse = R.id.scroll_dia_4;
                break;
            default:
                resourse = R.id.scroll_dia_5;
                break;
        }
        return resourse;
    }

}
