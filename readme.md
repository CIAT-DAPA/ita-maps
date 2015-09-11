# ita-maps

## Dev by: Louis Reymondin


generatetiles
=============
 this folder has the java files to get tiles
 
 these are the order to run and get tiles
 
## Models ##
'''
java -cp ita-maps-1.0-SNAPSHOT.jar org.ciat.ita.maps.tilecutter.TileCutter iabin.properties
'''
## when the properties files has a different name ##
'''
java -cp ita-maps-1.0-170114-SNAPSHOT.jar org.ciat.ita.maps.tilecutter.TileCutter iabin2.properties
'''
## richness few values ##
'''
java -cp ita-maps-1.0-RichnessNew-SNAPSHOT.jar org.ciat.ita.maps.tilecutter.TileCutter iabin2.properties
'''
## richness two values ##
'''
java -cp ita-maps-1.0-Richness2Values-SNAPSHOT.jar org.ciat.ita.maps.tilecutter.TileCutter iabin2.properties
'''
## richness >8 values (ranges) ##
'''
java -cp ita-maps-1.0-RichnessRanges-SNAPSHOT.jar org.ciat.ita.maps.tilecutter.TileCutter iabin2.properties
'''
##richness with the old file ##
'''
java -cp ita-maps-1.0-RichnessNewArchivo v
'''