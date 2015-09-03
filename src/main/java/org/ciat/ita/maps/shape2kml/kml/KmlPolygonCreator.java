package org.ciat.ita.maps.shape2kml.kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ciat.ita.maps.utils.PropertiesManager;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.MultiPolygon;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class KmlPolygonCreator {

	/*
	 * ruta de los kmls
	 */
	private String path;

	/*
	 * se recibe index, nombre de las columnas a mostrar
	 */
	private HashMap<Integer, String> atributos = new HashMap<Integer, String>();//

	public KmlPolygonCreator(String path, HashMap<Integer, String> atributos) {
		this.path = path;
		File dir = new File(path);
		dir.mkdirs();
		this.atributos = atributos;
	}

	public void createKML(SimpleFeature sf, int id) throws FileNotFoundException {

		// Set<Integer> keySet = atributos.keySet();
		/*
		 * String descripcion =
		 * "<table border=\"1\" padding=\"3\" width=\"300\"><tr bgcolor= \"#D2D2D2\">"
		 * ;
		 * 
		 * for (Integer i : keySet) {//se crean los titulos de la tabla de la
		 * informacion del poligono descripcion+=
		 * "<td>"+atributos.get(i)+"</td>"; } descripcion+="</tr><tr>"; for
		 * (Integer i : keySet) {//se crean los contenidos de la tabla de la
		 * informacion del poligono descripcion+=
		 * "<td>"+sf.getAttribute(i)+"</td>"; } descripcion+="</tr></table>";
		 */
		
		final Kml kml = new Kml();

		String filename = path + File.separator + sf.getAttribute(17) + ".kml";

		MultiPolygon polygons = (MultiPolygon) sf.getAttribute(0);
		final Folder folder = kml.createAndSetFolder().withName(
				atributos.get("name"));
		for (int i = 0; i < polygons.getNumGeometries(); i++) {

			com.vividsolutions.jts.geom.Polygon geo = (com.vividsolutions.jts.geom.Polygon) polygons.getGeometryN(i);

			final Placemark p = folder
					.createAndAddPlacemark()
					.withName(id+"_18")
					.withStyleUrl(PropertiesManager.getInstance().getPropertiesAsString("style.url"));
			final Boundary bound = new Boundary();
			final LinearRing lin = bound.createAndSetLinearRing();
			Polygon pol = p.createAndSetPolygon();

			List<Coordinate> kmlCoords = lin.createAndSetCoordinates();
			com.vividsolutions.jts.geom.Coordinate[] shapeCoords = geo
					.getExteriorRing().getCoordinates();

			for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords)
				kmlCoords.add(new Coordinate(coord.x, coord.y));

			pol.setOuterBoundaryIs(bound);
			Boundary innerBoundary;
			LinearRing innerLin;
			List<Boundary> inner = new ArrayList<Boundary>();
			List<Coordinate> innerKmlCoords;
			for(int j = 0; j<geo.getNumInteriorRing(); j++){
				
				innerBoundary  = new Boundary();
				innerLin = innerBoundary.createAndSetLinearRing();
				innerKmlCoords = innerLin.createAndSetCoordinates();
				shapeCoords = geo.getInteriorRingN(j).getCoordinates();

				for (com.vividsolutions.jts.geom.Coordinate coord : shapeCoords)
					innerKmlCoords.add(new Coordinate(coord.x, coord.y));
						
				inner.add(innerBoundary);

			}
			
			pol.setInnerBoundaryIs(inner);

		}

		kml.marshal(new File(filename));

	}

	public ArrayList<com.vividsolutions.jts.geom.Coordinate> sortCoords(
			com.vividsolutions.jts.geom.Coordinate[] shapeCoords) {
		boolean[] used = new boolean[shapeCoords.length];
		ArrayList<com.vividsolutions.jts.geom.Coordinate> results = new ArrayList<com.vividsolutions.jts.geom.Coordinate>();

		int index = 0;
		int tmp = 0;
		for (int i = 0; i < shapeCoords.length; i++) {
			index = tmp;
			used[index] = true;
			results.add(shapeCoords[index]);
			double dist = Double.MAX_VALUE;
			for (int j = 0; j < shapeCoords.length; j++) {
				if (!used[j]) {
					if (dist < Math.hypot(shapeCoords[j].x
							- shapeCoords[index].x, shapeCoords[j].y
							- shapeCoords[index].y)) {
						dist = Math.hypot(shapeCoords[j].x
								- shapeCoords[index].x, shapeCoords[j].y
								- shapeCoords[index].y);
						tmp = j;
					}
				}
			}
		}

		return results;
	}

}
