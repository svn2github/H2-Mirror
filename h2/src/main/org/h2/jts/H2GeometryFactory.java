package org.h2.jts;

import org.h2.api.GeometryParseException;
import org.h2.api.IEnvelope;
import org.h2.api.IGeometry;
import org.h2.api.IGeometryFactory;
import org.h2.message.DbException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Implementation of the {@link IGeometryFactory} interface to support the JTS
 * Topology Suite.
 */
public class H2GeometryFactory implements IGeometryFactory {
	/**
	 * @see org.h2.value.IGeometryFactory#toGeometry(java.lang.String)
	 */
	@Override
	public IGeometry toGeometry(String s) throws GeometryParseException {
		try {
			return new H2Geometry(new WKTReader().read(s));
		} catch (ParseException e) {
			throw new GeometryParseException(e);
		}
	}

	/**
	 * @see org.h2.value.IGeometryFactory#toGeometry(java.lang.String, int)
	 */
	@Override
	public IGeometry toGeometry(String s, int srid)
			throws GeometryParseException {
		try {
			GeometryFactory geometryFactory = new GeometryFactory(
					new PrecisionModel(), srid);
			Geometry g = new WKTReader(geometryFactory).read(s);
			return new H2Geometry(g);
		} catch (ParseException e) {
			throw new GeometryParseException(e);
		}

	}

	/**
	 * @see org.h2.value.IGeometryFactory#toGeometry(byte[])
	 */
	@Override
	public IGeometry toGeometry(byte[] bytes) throws GeometryParseException {
		try {
			return new H2Geometry(new WKBReader().read(bytes));
		} catch (ParseException ex) {
			throw DbException.convert(ex);
		}
	}

	/**
	 * @see org.h2.value.IGeometryFactory#toGeometry(org.h2.value.IEnvelope)
	 */
	@Override
	public IGeometry toGeometry(IEnvelope envelope) {
		if (!H2Envelope.class.isInstance(envelope))
			throw new AssertionError(
					"The given envelope must be an instance of H2Envelope!");

		GeometryFactory geometryFactory = new GeometryFactory();
		return new H2Geometry(
				geometryFactory.toGeometry(envelope.unwrap(Envelope.class)));
	}
}
