package util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.hibernate.spatial.jts.mgeom.MCoordinateSequence;
import org.hibernate.spatial.jts.mgeom.MGeometryFactory;

/**
 * User: TangGaowei
 * Date: 2016-11-15
 * PostGIS utils
 */
public class GeoUtils {
    public final static int SRID = 4326;

    /**
     * 创建地理点
     * @param x
     * @param y
     * @return
     */
    public static Point createPoint(double x, double y){
        MGeometryFactory mgf = new MGeometryFactory(new PrecisionModel(), SRID);
        Coordinate coord = new Coordinate( x, y );
        MCoordinateSequence coordinates = new MCoordinateSequence(new Coordinate[]{coord});
        return  mgf.createPoint(coordinates);
    }
}
