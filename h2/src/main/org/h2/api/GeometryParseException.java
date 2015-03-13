/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.api;


/**
 * Signals that an error has been reached unexpectedly while parsing a geometry.
 * 
 * @see java.lang.Exception
 * @see IGeometryFactory
 */
public class GeometryParseException extends Exception {
	
	private static final long serialVersionUID = -3743282683053748926L;
	
	/**
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *            retrieval by the {@link #getMessage()} method.
     */
    public GeometryParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). 
     * 
     * This constructor is useful for exceptions that are little more than 
     * wrappers for other throwables (for example,
     * {@link java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A <tt>null</tt> value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @since 1.4
     */
    public GeometryParseException(Exception cause) {
        super(cause);
    }
}
