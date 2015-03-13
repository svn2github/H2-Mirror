package org.h2.jts;

import java.sql.SQLException;

import org.h2.api.IEnvelope;
import org.h2.message.DbException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Implementation of the {@link IEnvelope} interface which wraps the
 * {@link Envelope}.
 */
public class H2Envelope implements IEnvelope {
	private final Envelope envelope;

	/**
	 * Constructs a new {@link H2Envelope} instance by using the given
	 * parameters.
	 */
	public H2Envelope(Envelope envelope) {
		this.envelope = envelope;
	}

	/**
	 * @see org.h2.value.IEnvelope#getMinX()
	 */
	@Override
	public double getMinX() {
		return envelope.getMinX();
	}

	/**
	 * @see org.h2.value.IEnvelope#getMinY()
	 */
	@Override
	public double getMinY() {
		return envelope.getMinY();
	}

	/**
	 * @see org.h2.value.IEnvelope#getMaxX()
	 */
	@Override
	public double getMaxX() {
		return envelope.getMaxX();
	}

	/**
	 * @see org.h2.value.IEnvelope#getMaxY()
	 */
	@Override
	public double getMaxY() {
		return envelope.getMaxY();
	}

	/**
	 * @see org.h2.value.IEnvelope#intersects(org.h2.value.IEnvelope)
	 */
	@Override
	public boolean intersects(IEnvelope other) {

		//intersects is only supported if the given envelope is wrapper for com.vividsolutions.jts.geom.Envelope
		if (!other.isWrapperFor(Envelope.class))
			return false;

		return this.envelope.intersects(other.unwrap(Envelope.class));
	}

	/**
	 * @see org.h2.value.IEnvelope#getUnion(org.h2.value.IEnvelope)
	 */
	@Override
	public IEnvelope getUnion(IEnvelope other) {
		Envelope mergedEnvelope = new Envelope(envelope);

		// union is only supported if the given envelope is wrapper for com.vividsolutions.jts.geom.Envelope
		if (other.isWrapperFor(Envelope.class))
		mergedEnvelope.expandToInclude(other.unwrap(Envelope.class));
		
		return new H2Envelope(mergedEnvelope);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) {
		if (isWrapperFor(iface)) {
			return (T) envelope;
		}
		throw DbException.getInvalidValueException("iface", iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) {
		return iface != null && iface.isAssignableFrom(envelope.getClass());
	}
	
	@Override
	public String toString() {
		return envelope.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((envelope == null) ? 0 : envelope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		H2Envelope other = (H2Envelope) obj;
		if (envelope == null) {
			if (other.envelope != null)
				return false;
		} else if (!envelope.equals(other.envelope))
			return false;
		return true;
	}
	
	
}
