package dk.brics.tajs.solver;

import java.util.ArrayList;
import java.util.HashMap;

import dk.brics.tajs.dependency.Dependency;
import dk.brics.tajs.dependency.graph.DependencyGraphReference;
import dk.brics.tajs.flowgraph.ObjectLabel;
import dk.brics.tajs.lattice.Obj;

/**
 * Interface for abstract states for block entries.
 */
/**
 * @author Matthias
 *
 * @param <BlockStateType>
 */
public interface IBlockState<BlockStateType extends IBlockState<BlockStateType>> extends Cloneable {

	/**
	 * Checks that the state is consistent.
	 * @throws RuntimeException if inconsistent
	 */
	public void check();
	
	/**
	 * Sharpens the state.
	 */
	public void sharpen();
	
	/**
	 * Constructs a new state as a copy of this state.
	 */
	public BlockStateType clone();
	
	/**
	 * Joins the given state into this state.
	 * @return true if changed
	 */
	public boolean join(BlockStateType s);
	
	/**
	 * Checks whether this state is empty.
	 */
	public boolean isEmpty();
	
	/**
	 * Returns a brief description of the state.
	 */
	public String toStringBrief();
	
	/**
	 * Returns a description of the changes from the old state to this state.
	 * It is assumed that the old state is less than this state.
	 */
	public String diff(BlockStateType old);
	
	/**
	 * Returns a measure of the vertical position of this lattice element within the lattice.
	 */
	public int getPosition();
	
	/**
	 * Reduces this state according to the given existing state.
	 * If the given state is null, the entire store is reduced.
	 */
	public void reduce(BlockStateType s);
	
	/**
	 * Clears effects at function entry.
	 */
	public void clearEffects();
	
	/**
	 * Looks up an object in the store.
	 * Returns null if absent or 'unknown'.
	 * @param writable if set, the object becomes modifiable
	 */
	public Obj getObjectRaw(ObjectLabel objlabel, boolean writable);
	
	/**
	 * Analyze the value dependencies
	 * @return 
	 */
	public ArrayList<HashMap<String, Dependency>> analyzeDependencies();
	
	/**
	 * Analyze the dependency references
	 * @return 
	 */
	public ArrayList<HashMap<String, DependencyGraphReference>> analyzeDependencyReferences();
}
