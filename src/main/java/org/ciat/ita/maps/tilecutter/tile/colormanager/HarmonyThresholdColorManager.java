/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ciat.ita.maps.tilecutter.tile.colormanager;

import org.ciat.ita.maps.tilecutter.raster.Raster;

/**
 *
 * @author HSOTELO
 */
public class HarmonyThresholdColorManager extends HarmonyColorManager {
    
    
    /**
     * Constructor de la clase FixedThresholdColorManager
     * @param rgbMin valor de color minimo
     * @param rgbMax valor de color maximo
     * @param min valor minimo
     * @param max valor maximo
     * @param NoData valor de noData
     * @param umbral para el color discreto fijo
     */
    public HarmonyThresholdColorManager(float[] rgbMin, float[] rgbMax, float min, float max,
            float NoData, float umbral,Raster raster, float grades) {
        super(rgbMin, rgbMax, min, max, NoData, raster,umbral, grades);
    }
    
    @Override
    public float[] createThresholds() {
        System.out.println(getMax());
        System.out.println(getMin());
        double fSize;
        if( getMax()-getMin()==0)
            return new float[]{getMax()};        
        else
            fSize = (getMax()+(1)-getMin())/umbral;
        
        double rSize = Math.floor(fSize);
        
        int size = (int) rSize;
        
        if(fSize != rSize)
            size++;
        
        float[] umbrales = new float[size];
        
        for(int i = 0; i<umbrales.length;i++){
            if((i+1)==umbrales.length)
                umbrales[i] = getMax();
            else if(umbral==1)
                umbrales[i] = getMin()+umbral*i;
            else
                umbrales[i] = getMin()+umbral*(i+1);
        }
        return umbrales;
    }
    
}
