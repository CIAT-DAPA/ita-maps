package org.ciat.ita.maps.csv2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class Csv2Point {

	private LinkedList<String[]> lista;
	

	public Csv2Point(LinkedList<String[]> lista) {
		this.lista = lista;
	}

	public void createKML(String path, String archivo) throws FileNotFoundException {

		//String descripcion = "descripcion";

		final Kml kml = new Kml();

		

		String ruta = path +archivo+ "-point.kml";
		//filename = "d:/csv2punto.kml";

		System.out.println("empieza a mostrar");

		Placemark placemark = KmlFactory.createPlacemark();
		Folder folder = kml.createAndSetFolder();
/**
 * se recorre la lista generando las coordenadas y agregando al folder
 */
		for (String[] s : lista) {

			placemark = folder.createAndAddPlacemark().withStyleUrl("http://wikipedia.agilityhoster.com/estilo.kml#estilo");//se crea el placemark y se naade al folder
			placemark.createAndSetPoint().addToCoordinates(
					Double.parseDouble(s[0]), Double.parseDouble(s[1]));//se crean las coordenadas y se registran al placemark

		}
		kml.setFeature(folder);//se registra el folder al kml
		
		File dir = new File(path);
		dir.mkdirs();	
		
		kml.marshal();//se imprime kml en consola
		kml.marshal(new File(ruta));//se guarda kml en archivo

	}

}
