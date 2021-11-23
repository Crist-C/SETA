package com.examplecodes.Seta.Inventario;

public class OptionItem {

    public String Option;
    public int Imagen;


    public OptionItem(String optionText, int imagenItem) {
        this.Option = optionText;
        this.Imagen = imagenItem;
    }

    public OptionItem() {
    }

    public String getOpcion() {
        return Option;
    }

    public void setOpcion(String opcion) {
        Option = opcion;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }
}
