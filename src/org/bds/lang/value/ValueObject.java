package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;

import java.util.*;

/**
 * Define a value of an object (i.e. a class)
 *
 * @author pcingola
 */
public class ValueObject extends ValueComposite {

    private static final long serialVersionUID = -1443386366370835828L;

    Map<String, Value> fields;

    public ValueObject(Type type) {
        super(type);
    }

    @Override
    public Value clone() {
        ValueObject vclone = new ValueObject(type);
        vclone.fields.putAll(fields);
        return vclone;
    }

	public Type getFieldType(String name) {
		TypeClass tc = (TypeClass) type;
		return tc.getFieldType(name);
	}

	/**
     * Get field's value, can be null
     */
    public Value getFieldValue(String name) {
        return fields.get(name);
    }

    /**
     * Does the class have a field 'name'?
     */
    public boolean hasField(String name) {
        TypeClass tc = (TypeClass) type;
        return tc.hasField(name);
    }

    @Override
    public int hashCode() {
        // Note: We use identity hash to avoid infinite recursion
        //       E.g. if an object has a field pointing to itself. This
        //       could also happen indirectly:  A -> B -> A
        return System.identityHashCode(this);
        //		return fields != null ? fields.hashCode() : 0;
    }

    /**
     * Initialize fields (by default the fields are null)
     */
    public void initializeFields() {
        fields = new HashMap<>();
        TypeClass tc = (TypeClass) type;

        // Fields for this class and all parent classes
        for (Map.Entry<String, Type> e : tc.getFieldTypes().entrySet()) {
            var fname = e.getKey();
            var ftype = e.getValue();
            fields.put(fname, ftype.newDefaultValue());
        }

        // TODO: Remove old code
//		for (ClassDeclaration cd = tc.getClassDeclaration(); cd != null; cd = cd.getClassParent()) {
//			FieldDeclaration[] fieldDecls = cd.getFieldDecl();
//			for (FieldDeclaration fieldDecl : fieldDecls) { // Add all fields
//				Type vt = fieldDecl.getType();
//				for (VariableInit vi : fieldDecl.getVarInit()) {
//					String fname = vi.getVarName();
//					if (!fields.containsKey(fname)) { // Don't overwrite values 'shadowed' by a child class
//						fields.put(fname, vt.newDefaultValue());
//					}
//				}
//			}
//		}
    }

    public boolean isNull() {
        return fields == null;
    }

    @Override
    public void parse(String str) {
        runtimeError("String parsing unimplemented for type '" + this + "'");
    }

    public void setValue(String name, Value v) {
        if (isNull()) runtimeError("Null pointer: Cannot set field '" + getType() + "." + name + "'");
        fields.put(name, v);
    }

    @Override
    public void setValue(Value v) {
        fields = ((ValueObject) v).fields;
    }

    @Override
    protected void toString(StringBuilder sb, Set<Value> done) {
        if (isNull()) {
            sb.append("null");
            return;
        }
        if (done.contains(this)) {
            sb.append(toStringIdentity());
            return;
        }
        done.add(this);

        sb.append("{");
        if (fields != null) {
            List<String> fnames = new ArrayList<>(fields.size());
            fnames.addAll(fields.keySet());
            Collections.sort(fnames);
            for (int i = 0; i < fnames.size(); i++) {
                String fn = fnames.get(i);
                Value val = fields.get(fn);
                sb.append((i > 0 ? ", " : " ") + fn + ": ");
                val.toString(sb, done);
            }
        }
        sb.append(" }");
    }

}
