package dk.brics.tajs.analysis.dom;

import dk.brics.tajs.analysis.InitialStateBuilder;
import dk.brics.tajs.analysis.State;
import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Value;
import dk.brics.tajs.util.Collections;

import java.util.Set;

import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMFunction;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMSpecialProperty;
import static dk.brics.tajs.analysis.dom.DOMFunctions.createDOMInternalPrototype;

public class ObjectBuilder {

	private static final class InstanceProperty {
		private String property;
		private Value value;
		private DOMSpec spec;

		private InstanceProperty(String property, Value value, DOMSpec spec) {
			this.property = property;
			this.value = value;
			this.spec = spec;
		}

		public String getProperty() {
			return property;
		}

		public Value getValue() {
			return value;
		}

		public DOMSpec getSpec() {
			return spec;
		}
	}

	private static final class PrototypeFunction {
		private DOMObjects nativeObject;
		private String name;
		private int args;
		private DOMSpec spec;

		private PrototypeFunction(DOMObjects nativeObject, String name, int args, DOMSpec spec) {
			this.nativeObject = nativeObject;
			this.name = name;
			this.args = args;
			this.spec = spec;
		}

		public DOMObjects getNativeObject() {
			return nativeObject;
		}

		public String getName() {
			return name;
		}

		public int getArgs() {
			return args;
		}

		public DOMSpec getSpec() {
			return spec;
		}
	}

	private ObjectLabel constructor;
	private ObjectLabel prototype;
	private ObjectLabel prototype_prototype;
	private ObjectLabel instances;

	private Set<InstanceProperty> instanceProperties = Collections.newSet();
	private Set<PrototypeFunction> prototypeFunctions = Collections.newSet();

	private boolean built = false;

	public static ObjectBuilder newInstance() {
		return new ObjectBuilder();
	}

	public ObjectBuilder constructor(ObjectLabel constructor) {
		this.constructor = constructor;
		return this;
	}

	public ObjectBuilder prototype(ObjectLabel prototype) {
		this.prototype = prototype;
		return this;
	}

	public ObjectBuilder prototypePrototype(ObjectLabel prototype_prototype) {
		this.prototype_prototype = prototype_prototype;
		return this;
	}

	public ObjectBuilder instances(ObjectLabel instances) {
		this.instances = instances;
		return this;
	}

	public void addInstanceProperty(String property, Value value, DOMSpec spec) {
		if (built) {
			throw new IllegalStateException();
		}
		instanceProperties.add(new InstanceProperty(property, value, spec));
	}

	public void addPrototypeFunction(DOMObjects nativeObject, String name, int args, DOMSpec spec) {
		if (built) {
			throw new IllegalStateException();
		}
		prototypeFunctions.add(new PrototypeFunction(nativeObject, name, args, spec));
	}

	public void build(State s) {
		if (constructor == null) {
			throw new IllegalStateException("'object' must be non-null");
		}
		if (prototype == null) {
			throw new IllegalStateException("'prototype' must be non-null");
		}
		if (prototype_prototype == null) {
			throw new IllegalStateException("'prototypePrototype' must be non-null");
		}
		if (instances == null) {
			throw new IllegalStateException("'instance' must be non-null");
		}

		// FIXME: transfer function throws TypeError "Illegal Constructor"'
		// Constructor Object
		s.newObject(constructor);
		createDOMSpecialProperty(s, constructor, "length", Value.makeNum(0, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMSpecialProperty(s, constructor, "prototype",
				Value.makeObject(prototype, new Dependency(), new DependencyGraphReference()).setAttributes(true, true, true));
		createDOMInternalPrototype(s, constructor, Value.makeObject(InitialStateBuilder.OBJECT_PROTOTYPE, new Dependency(), new DependencyGraphReference()));

		// Prototype Object
		s.newObject(prototype);
		for (PrototypeFunction f : prototypeFunctions) {
			createDOMFunction(s, prototype, f.getNativeObject(), f.getName(), f.getArgs(), f.getSpec());
		}
		createDOMInternalPrototype(s, prototype, Value.makeObject(prototype_prototype, new Dependency(), new DependencyGraphReference()));

		// Instances Object
		s.newObject(instances);
		createDOMInternalPrototype(s, instances, Value.makeObject(prototype, new Dependency(), new DependencyGraphReference()));
		for (InstanceProperty p : instanceProperties) {
			createDOMProperty(s, instances, p.getProperty(), p.getValue(), p.getSpec());
		}
		s.multiplyObject(instances);
		instances = instances.makeSingleton().makeSummary();

		built = true;
	}

	public ObjectLabel getInstances() {
		if (!built) {
			throw new IllegalStateException();
		}
		return instances;
	}

}
