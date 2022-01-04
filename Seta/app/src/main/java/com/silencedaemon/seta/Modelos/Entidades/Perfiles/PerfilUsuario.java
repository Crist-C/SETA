package com.silencedaemon.seta.Modelos.Entidades.Perfiles;

import java.util.ArrayList;

public abstract class PerfilUsuario {

    protected static ArrayList<String> PERMISOS = new ArrayList<String>();
    protected ArrayList<String> NOMBRE_FUNCIONES_PERMITIDAS = new ArrayList<String>();
    protected ArrayList<Integer> ICONOS_FUNCIONES_PERMITIDAS = new ArrayList<Integer>();


    void addPermiso(String permiso) {

    }

    void deletePermiso(String permiso) {

    }

    abstract void AddFuncion(String permiso);

}
