/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.ciat.ita.maps.tilecutter.raster.Raster;

/**
 *
 * @author HSOTELO
 */
public abstract class HarmonyColorManager  extends ColorManager {
    
    private float[] umbrales;
    protected float umbral;
    private float grades;
    
    public void setThresholds(float[] thresholds) {
        this.umbrales = thresholds;
    }
    
    /**
     * Constructor de la clase DiscreteColorManager
     *
     * @param rgbMin
     *            color minimo
     * @param rgbMax
     *            color maximo
     * @param min
     *            valor minimo
     * @param max
     *            valor maximo
     * @param NoData
     *            valor de noData
     */
    public HarmonyColorManager(float[] rgbMin, float[] rgbMax, float min,
            float max, float NoData, Raster raster, float umbral, float grades) {
        super(rgbMin, rgbMax, min, max, NoData, raster);
        this.umbral=umbral;
        this.grades=grades;
    }
    
    
    
    @Override
    public int getRGB(float value) {
        
        if (value == getNoData())
            return 0x00000000; // Los bit de la transparencia deben ser 0
        // (transparente)
        
        int classe = 0;
        
        int rgb = 0;
        
        for (classe = 0; classe < umbrales.length && value > umbrales[classe]; classe++) {
        }
        /*
        * Los colores estan en la memoria en un int, cada byte del int
        * corresponde a una capa A (transparencia) R (rojo) G (verde) B (azul)
        * 0xAARRGGBB
        *
        * Si quiero un rojo de 3, tengo que poner el byte 00000011 (o 0x03 en
        * exadecimal) en el byte del rojo. El rojo es en el tercer byte, voy
        * entonces a mover todos los bit de 2*8
        *
        * 0x03 << 16 = 0x030000
        */
        
        float hue_grades=getRgbMin()[0];
        
        float delta_grade= grades/(float)umbrales.length;
        float hue_new=hue_grades+(delta_grade*classe);
        
        // The values for Saturation and brightness are between 0 and 1
        // By this reason is necessary divide them by 100
        rgb=Color.HSBtoRGB(hue_new/360, getRgbMin()[1] > 1 ? getRgbMin()[1]/100 : getRgbMin()[1], getRgbMin()[2] > 1 ? getRgbMin()[2]/100 : getRgbMin()[2]);
        return rgb | 0xFF000000 ;
    }
    
    @Override
    public BufferedImage getScaleImage(String descripcion) {
        int line = 30;
        
        int recWidth = 30;
        int recHeight = line - 10;
        
        int width = 100;
        int height = umbrales.length * line +line;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        
        int cont = line;
        
        //graphics.setColor(Color.BLACK);
        //graphics.drawString(descripcion, 5, (line - recHeight) / 2 + recHeight);
        
        int valorMinimo = (int) this.getMin();
        
        for (int i = 0; i < umbrales.length; i++) {
            System.out.println("umbral " + umbrales[i]);
            if(valorMinimo!=0){
                Color color = new Color(getRGB(umbrales[i] - (float) Math.pow(10, -8)));
                graphics.setColor(color);
                graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
                graphics.setColor(Color.BLACK);
                graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight );
                if(umbral!=1)
                    ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + valorMinimo + " - " +((int)((i+1)==umbrales.length ? umbrales[i] : (umbrales[i]-1) ) ));
                else
                    ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + valorMinimo + "  " );
                cont += line;
            }
            if(umbral==1 && (i+1)<umbrales.length)
                valorMinimo = (int) umbrales[i+1];
            else
                valorMinimo = (int) umbrales[i];
        }
        return image;
        
    }
    
    /**
     * Funcion que crea la lista de umbrales
     *
     * @return lista de umbrales
     */
    public abstract float[] createThresholds();
}
