package dk.brics.tajs.test;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.DependencyObject;
import dk.brics.tajs.flowgraph.SourceLocation;

public class DependencyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// DEMO 1
		
		/*
		 * var a = 4711;
		 * var b = document.getValue('test')
		 * var c = a + b; 
		 */
//		
//		DependencyObject init_int = DependencyObject.getDependencyObject(new SourceLocation(21, "DependencyTest.java"));
//		Dependency a = init_int.getDependency();
//		
//		DependencyObject get_object = DependencyObject.getDependencyObject(new SourceLocation(24, "DependencyTest.java"));
//		Dependency b = get_object.getDependency();
		
		// FUNCTION +
		//Dependency c = a.join(b);
		
		//System.out.println(c);
		
		/////////////////
		
/*		
		DependencyObject t0 = DependencyObject.getDependencyObject(new SourceLocation(3, "DependencyTest.java"));
		DependencyObject t1 = DependencyObject.getDependencyObject(new SourceLocation(5, "DependencyTest.java"));
		DependencyObject t2 = DependencyObject.getDependencyObject(new SourceLocation(7, "DependencyTest.java"));
		DependencyObject t3 = DependencyObject.getDependencyObject(new SourceLocation(11, "DependencyTest.java"));
		
		
		// a = trace(1); 
		Dependency d1 = new Dependency("", t0);
		Dependency da = new Dependency("=", d1);
		
		// c = trace(2);
		Dependency d2 = new Dependency("", t1);
		Dependency db = new Dependency("=", d2);

		
		// c = a + b
		Dependency dexp = new Dependency("+", da, db);
		Dependency dc = new Dependency("=", dexp);
		dc.join(t1);
	
		Dependency dif = new Dependency("if");
		dif.join(t1);
		dif.join(t2);
		dif.join(t3);
		dif.join(dc);
		
		
		
		System.out.println("# " + dif);
		System.out.println("� " + dif.printDependency());
*/
		
		DependencyObject t0 = DependencyObject.getDependencyObject(new SourceLocation(3, "DependencyTest.java"));
		DependencyObject t1 = DependencyObject.getDependencyObject(new SourceLocation(5, "DependencyTest.java"));
		DependencyObject t2 = DependencyObject.getDependencyObject(new SourceLocation(7, "DependencyTest.java"));
		DependencyObject t3 = DependencyObject.getDependencyObject(new SourceLocation(11, "DependencyTest.java"));
		
		
		Dependency d0 = new Dependency(t0);
//		Dependency d1 = new Dependency(t1);
//		Dependency d2 = new Dependency(t2);
//		Dependency d3 = new Dependency(t3);
//
//		Dependency da = new Dependency(t0, t1);
//		Dependency db = new Dependency(t2, t3);
//		
//		Dependency dc = new Dependency(t1);
//		Dependency dd = new Dependency(da, db);
//		
//		
//		System.out.println(d0.toString());
//		System.out.println(d1.toString());
//		System.out.println(d2.toString());
//		System.out.println(d3.toString());
//		
//		System.out.println(da.toString());
//		System.out.println(db.toString());
//		
//		System.out.println(dc.toString());
//		System.out.println(dd.toString());
		
		
		
		
		
		
	}

}
