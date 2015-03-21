package Default;

import WorldClasses.CityObject;
import WorldClasses.Road;


//TODO find better name for Helper class
public class Helper {
	public static boolean assertRoad(CityObject o){
		return(o instanceof Road);
	}
}
