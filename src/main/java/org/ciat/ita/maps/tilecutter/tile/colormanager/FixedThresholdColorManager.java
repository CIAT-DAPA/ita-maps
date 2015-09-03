package org.ciat.ita.maps.tilecutter.tile.colormanager;

import org.ciat.ita.maps.tilecutter.raster.Raster;

public class FixedThresholdColorManager extends DiscreteColorManager {

	private float umbral;
	/**
	 * Constructor de la clase FixedThresholdColorManager
	 * @param rgbMin valor de color minimo
	 * @param rgbMax valor de color maximo
	 * @param min valor minimo
	 * @param max valor maximo
	 * @param NoData valor de noData
	 * @param umbral para el color discreto fijo
	 */
	public FixedThresholdColorManager(float[] rgbMin, float[] rgbMax, float min, float max,
			float NoData, float umbral,Raster raster) {
		super(rgbMin, rgbMax, min, max, NoData, raster);
		
		this.umbral = umbral;
	}

	@Override
	public float[] createThresholds() {
		System.out.println(getMax());
		System.out.println(getMin());
		double fSize;
		if( getMax()-getMin()==0)
			fSize = (getMax()+(2)-getMin())/umbral;
		else if(getMax()-getMin()==1)
			fSize = (getMax()+(1)-getMin())/umbral;
		else
			fSize = (getMax()+(1)-getMin())/umbral;
		
		double rSize = Math.floor(fSize);
		
		int size = (int) rSize;
		
		if(fSize != rSize)
			size++;
		
		float[] umbrales = new float[size+1];
		
		for(int i = 0; i<umbrales.length;i++)
			if((i+2)<umbrales.length)
				umbrales[i] = getMin()+umbral*(i+1);
			else
				umbrales[i] = getMax();
		
		return umbrales;
	}

}
