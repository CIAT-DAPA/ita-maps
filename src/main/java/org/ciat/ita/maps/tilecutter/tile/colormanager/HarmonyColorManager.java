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
            float max, float NoData, Raster raster) {
        super(rgbMin, rgbMax, min, max, NoData, raster);
    }
    
    
    
    @Override
    public int getRGB(float value) {
        
        if (value == getNoData())
            return 0x00000000; // Los bit de la transparencia deben ser 0
        // (transparente)
        
        float min = 0;
        float max = umbrales.length - 1;
        
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
        float particiones;
        
        float[] hsvMin=Color.RGBtoHSB( (int)getRgbMin()[0], (int)getRgbMin()[1], (int)getRgbMin()[2], null);
        float[] hsvMax=Color.RGBtoHSB( (int)getRgbMax()[0], (int)getRgbMax()[1], (int)getRgbMax()[2], null);
        
        float moduloMin = (float)Math.sqrt((hsvMin[0]*hsvMin[0])+(hsvMin[1]*hsvMin[1])+(hsvMin[2]*hsvMin[2]));
        float moduloMax = (float)Math.sqrt((hsvMax[0]*hsvMax[0])+(hsvMax[1]*hsvMax[1])+(hsvMax[2]*hsvMax[2]));
        float alfa=(float)Math.acos(((hsvMin[0]*hsvMax[0])+(hsvMin[1]*hsvMax[1])+(hsvMin[2]*hsvMax[2]))/(moduloMin*moduloMax));
        
        particiones = ((float)umbrales.length/(float)2);
        float delta_particion=(alfa/particiones);
        //float delta_ang=delta_particion*(umbrales[classe]-((umbrales.length/2) + (!escala1 ? 1 : -2)));
        //float delta_ang=delta_particion*((classe<=umbrales.length-2)?umbrales[classe]:umbrales[classe]+1);
        float delta_ang=delta_particion*umbrales[classe];
        
        float[] newHsv=new float[3];
        //Rotacion x
        newHsv[0]=(1*hsvMin[0]);
        newHsv[1]=((float)Math.cos(delta_ang)*hsvMin[1])+((float)Math.sin(delta_ang)*hsvMin[2]);
        newHsv[2]=((float)-Math.sin(delta_ang)*hsvMin[1])+((float)Math.cos(delta_ang)*hsvMin[2]);
        
        //Rotacion y
        newHsv[0]=((float)Math.cos(delta_ang)*hsvMin[0])+((float)-Math.sin(delta_ang)*hsvMin[2]);
        newHsv[1]=(1*hsvMin[1]);
        newHsv[2]=((float)Math.sin(delta_ang)*hsvMin[0])+((float)Math.cos(delta_ang)*hsvMin[2]);
        
        //Rotacion z
        newHsv[0]=((float)Math.cos(delta_ang)*hsvMin[0])+((float)Math.sin(delta_ang)*hsvMin[1]);
        newHsv[1]=((float)-Math.sin(delta_ang)*hsvMin[0])+((float)Math.cos(delta_ang)*hsvMin[1]);
        newHsv[2]=(1*hsvMin[2]);
        
        rgb=Color.HSBtoRGB(newHsv[0], newHsv[1], newHsv[2]);
        return rgb ;
    }
    
    @Override
    public BufferedImage getScaleImage(String descripcion) {
        int line = 30;
        
        int recWidth = 30;
        int recHeight = line - 10;
        
        int width = 100;
        int height = umbrales.length * line +line;
        
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        
        int cont = line;
        
        //graphics.setColor(Color.BLACK);
        //graphics.drawString(descripcion, 5, (line - recHeight) / 2 + recHeight);
        
        int valorMinimo = (int) this.getMin();
        int valorMax= (int) this.getMax();
        
        if(valorMax==1){
            height = valorMax * line ;
            for (int i = 0; i < umbrales.length; i+=2) {
                if(valorMinimo!=0){
                    Color color = new Color(getRGB(umbrales[i] - (float) Math.pow(10, -8)));
                    graphics.setColor(color);
                    graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight );
                    ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + valorMinimo + " " );
                    cont += line;
                }
                valorMinimo = (int) umbrales[i];
                
            }
            
            return image;
            
        }
        else if(valorMax==2){
            height = valorMax * line ;
            for (int i = 0; i < umbrales.length; i++) {
                if(valorMinimo!=0){
                    Color color = new Color(getRGB(umbrales[i] - (float) Math.pow(10, -8)));
                    graphics.setColor(color);
                    graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight );
                    ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + (int)umbrales[i] + " " );
                    cont += line;
                }
                valorMinimo = (int) umbrales[i];
            }
            return image;
            
        }
        else{
            for (int i = 0; i < umbrales.length; i++) {
                //System.out.println("umbrales"+umbrales.length+" valor i"+i);
                if(valorMinimo!=0){
                    
                    if(i<=umbrales.length-2){
                        if(i!=umbrales.length-2){
                            Color color = new Color(getRGB(umbrales[i] - (float) Math.pow(10, -8)));
                            graphics.setColor(color);
                            graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
                            graphics.setColor(Color.BLACK);
                            graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight );
                            ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + valorMinimo + " - " +((int) umbrales[i]-1));
                            cont += line;
                            
                        }                                
                        else {
                            Color color = new Color(getRGB(umbrales[i] - (float) Math.pow(10, -8)));
                            graphics.setColor(color);
                            graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
                            graphics.setColor(Color.BLACK);
                            graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight );
                            ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics, + valorMinimo + " - " +((int) umbrales[i]));
                            cont += line;
                        }
                    }
                }
                valorMinimo = (int) umbrales[i];
                
            }
            return image;
        }
        
        
    }
    
    /**
     * Funcion que crea la lista de umbrales
     *
     * @return lista de umbrales
     */
    public abstract float[] createThresholds();
}
