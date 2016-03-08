/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.value;

import org.h2.api.ValueGeometryFactory;
import org.h2.message.DbException;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;

/**
 * The {@link ValueGeometryFactory} implementation for the JTS geometry
 * framework.
 * 
 * @author Steve Hruda
 */
public class JTSValueGeometryFactory implements ValueGeometryFactory<JTSValueGeometry, Geometry> {
	
	@Override
	public Geometry getGeometry(byte[] bytes) throws DbException {
		try {
			return  new WKBReader().read(bytes);
		} catch (ParseException ex) {
            throw DbException.convert(ex);
        }
	}
	
	@Override
	public Geometry getGeometry(String s) throws DbException
	{
		try {
            return new WKTReader().read(s);
        } catch (ParseException ex) {
            throw DbException.convert(ex);
        }
	}
	
	@Override
	public Geometry getGeometry(String s, int srid) throws DbException {
		try {
			GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
			return new WKTReader(geometryFactory).read(s);
		}catch(ParseException ex)
		{
			throw DbException.convert(ex);
		}
	}

	@Override
	public JTSValueGeometry get(Geometry g) {
        byte[] bytes = convertToWKB(g);
        return (JTSValueGeometry) Value.cache(new JTSValueGeometry(bytes, g));
    }
	

    @Override
	public JTSValueGeometry get(String s) {
        try {
            Geometry g = new WKTReader().read(s);
            return get(g);
        } catch (ParseException ex) {
            throw DbException.convert(ex);
        }
    }

    @Override
    public JTSValueGeometry get(String s, int srid) {
        try {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
            Geometry g = new WKTReader(geometryFactory).read(s);
            return get(g);
        } catch (ParseException ex) {
            throw DbException.convert(ex);
        }
    }

    @Override
	public JTSValueGeometry get(byte[] bytes) {
        return (JTSValueGeometry) Value.cache(new JTSValueGeometry(bytes, null));
    }
    
	@Override
	public JTSValueGeometry get(Object g) {
    	if(!isGeometryTypeSupported(g))
    		throw new RuntimeException("The given object is not compatible with this ValueGeometryFactory instance!");
		
    	return get((Geometry)g);
	}
	
	@Override
	public JTSValueGeometry get(Value g) {
		if(!(g instanceof JTSValueGeometry))
			throw new RuntimeException("The given value is not compatible with this ValueGeometryFactory instance!");
		
		return (JTSValueGeometry) g;
	}

	@Override
	public boolean isGeometryTypeSupported(Object g) {
		return g instanceof Geometry;
	}
	
	 @Override
		public Class<Geometry> getGeometryType() {
			return Geometry.class;
		}
    
    private static byte[] convertToWKB(Geometry g) {
        boolean includeSRID = g.getSRID() != 0;
        int dimensionCount = getDimensionCount(g);
        WKBWriter writer = new WKBWriter(dimensionCount, includeSRID);
        return writer.write(g);
    }

    private static int getDimensionCount(Geometry geometry) {
        ZVisitor finder = new ZVisitor();
        geometry.apply(finder);
        return finder.isFoundZ() ? 3 : 2;
    }

    /**
     * A visitor that checks if there is a Z coordinate.
     */
    static class ZVisitor implements CoordinateSequenceFilter {
        boolean foundZ;

        public boolean isFoundZ() {
            return foundZ;
        }

        @Override
        public void filter(CoordinateSequence coordinateSequence, int i) {
            if (!Double.isNaN(coordinateSequence.getOrdinate(i, 2))) {
                foundZ = true;
            }
        }

        @Override
        public boolean isDone() {
            return foundZ;
        }

        @Override
        public boolean isGeometryChanged() {
            return false;
        }
    }
}
