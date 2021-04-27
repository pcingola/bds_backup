package org.bds.lang.value;

import java.util.HashSet;
import java.util.Set;

import org.bds.lang.type.Type;

public abstract class ValueComposite extends Value {

	private static final long serialVersionUID = -6252319879040413844L;

	protected Type type;

	public ValueComposite(Type type) {
		super();
		this.type = type;
	}

	/**
	 * Convert to 'bool'
	 */
	@Override
	public boolean asBool() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to bool");
	}

	/**
	 * Convert to 'int'
	 */
	@Override
	public long asInt() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to int");
	}

	/**
	 * Convert to 'real'
	 */
	@Override
	public double asReal() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to real");
	}

	/**
	 * Convert to 'string'
	 */
	@Override
	public String asString() {
		return toString();
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<Value> done = new HashSet<>();
		toString(sb, done);
		return sb.toString();
	}

}
