package dk.brics.tajs.solver;

/**
 * Synchronizer for solver events.
 */
abstract public class SolverSynchronizer {
	
	private boolean single_step;
	
	private boolean active;
	
	/**
	 * Constructs a new synchronizer.
	 */
	public SolverSynchronizer() {}
	
	/**
	 * Checks whether this solver is active.
	 */
	public synchronized boolean isActive() {
		return active;
	}
	
	/**
	 * Sets/resets the active flag.
	 */
	public synchronized void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Checks whether single-stepping is enabled.
	 */
	public synchronized boolean isSingleStep() {
		return single_step;
	}
	
	/**
	 * Enable/disable single-stepping.
	 */
	public synchronized void setSingleStep(boolean enable) {
		this.single_step = enable;
	}
	
	/**
	 * Waits for notification if single-stepping is enabled.
	 */
	synchronized void waitIfSingleStep() {
		if (single_step)
			try {
				waiting();
				wait();
			} catch (InterruptedException e) {
				// ignore
			}
	}
	
	/**
	 * Sends a notification to run/single-step.
	 */
	public synchronized void notifyRunOrSingleStep() {
		notify();
	}
	
	/**
	 * Callback, invoked when initialing wait.
	 */
	abstract public void waiting();
}
