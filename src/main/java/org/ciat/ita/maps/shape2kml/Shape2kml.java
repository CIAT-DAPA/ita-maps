package org.ciat.ita.maps.shape2kml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import org.ciat.ita.maps.shape2kml.kml.KmlGroupCreator;
import org.ciat.ita.maps.shape2kml.kml.KmlPolygonCreator;
import org.ciat.ita.maps.shape2kml.shape.Shapefile;
import org.ciat.ita.maps.utils.PropertiesGenerator;
import org.ciat.ita.maps.utils.PropertiesManager;

public class Shape2kml {

	private Shapefile shp;
	public static Shape2kml s2k;

	public static void main(String[] args) {
		s2k = new Shape2kml();
		s2k.executeFromProperties(args[0]);
	}
	
	public void executeFromProperties(String propertiesFile) {
		PropertiesManager.register(propertiesFile);
		
		
		
		String[] shapesID = PropertiesManager.getInstance().getPropertiesAsStringArray("shapes");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		
		
		for (String shapeID : shapesID) {	
			String group = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group+".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".shapefile");
			String urlServer= PropertiesManager.getInstance().getPropertiesAsString(shapeID+".server.url");
			String mainKml = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".kml.main");
			int []columnIndexes = PropertiesManager.getInstance().getPropertiesAsIntArray(shapeID+".shape.column.indexes");
			String[] columnName = PropertiesManager.getInstance().getPropertiesAsStringArray(shapeID+".shape.column.names");
			
			String sourceFile =sourcePath+pathGroup+fileName;
			String targetFile= targetPath+pathGroup+shapeID;
			HashMap<Integer, String> atributos = new HashMap<Integer, String>();
			for (int i = 0; i < columnIndexes.length; i++) {
				atributos.put(columnIndexes[i], columnName[i]);
			}
			
			s2k.execute(sourceFile, targetFile,urlServer,mainKml,atributos);
		}
		
		
	}
	

	public void execute(String sourceFile,String targetFile,
			String urlServer,String mainKml,HashMap<Integer,String> atributos) {
		
		
		
		File file = new File(sourceFile);
		
		shp = new Shapefile(file);
		SimpleFeature sf = null;
		KmlGroupCreator grupo = new KmlGroupCreator(urlServer);

		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		//int count = 5;

		KmlPolygonCreator kml = new KmlPolygonCreator(targetFile,atributos);
		
		//*****************************************************************************
		PropertiesGenerator hola=new PropertiesGenerator("d:/prueba.properties");
		try {
			hola.write();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//*****************************************************************************
		
		//while (fi.hasNext()) {
		try {
		FileReader fr = new FileReader("D:/peru_tabla_final/ID_bosque_concesionreforestacion.csv");
		BufferedReader bf = new BufferedReader(fr);
		String line;	
		int id=26;	
		try {
			line=bf.readLine();
			
			while ((line = bf.readLine()) != null) {
				sf = fi.next();
				String[] value = line.split(",");
				System.out.println("id del shapefile:"+sf.getAttribute(17));
								
				
				System.out.println("id del csv:"+value[2].replaceAll("\"", ""));
				
				System.out.println("analyzed area"+Double.parseDouble(value[5].replaceAll("\"", "")));
				System.out.println("");
				
				if(Double.parseDouble(value[5].replaceAll("\"", ""))!=0.0){
					System.out.println("entra");
				try {					
					
					kml.createKML(sf, id);
					grupo.addElement(id+".kml");					
					
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}

				
				//System.out.print(sf.getAttribute(6) + " ");
				//System.out.println(sf.getAttribute(7));

			//}			
				id++;				
				} else {					
					System.out.println("no data");					
				}
				
				}			
				bf.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} 
		try {
			grupo.writeKml(targetFile,mainKml);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
