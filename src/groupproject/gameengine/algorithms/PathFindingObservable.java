package groupproject.gameengine.algorithms;


import groupproject.gameengine.algorithms.models.Node;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Vector;

public class PathFindingObservable<N extends Node<N>> {

    private boolean changed = false;
    private Vector<PathFindingObserver<N>> obs;

    public PathFindingObservable() {
        obs = new Vector<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set
     */
    public synchronized void addObserver(PathFindingObserver<N> o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object
     */
    public synchronized void deleteObserver(PathFindingObserver<N> o) {
        obs.removeElement(o);
    }

    /**
     * If this object has changed,notify all of its observer
     * @param arg any object.
     * @see PathFindingObservable#clearChanged()
     * @see PathFindingObservable#hasChanged()
     * @see PathFindingObserver#update(PathFindingObservable, LinkedHashSet)
     */
    public void notifyObservers(LinkedHashSet<N> arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        PathFindingObserver[] arrLocal;

        synchronized (this) {
            /*  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed)
                return;

            arrLocal = obs.toArray(new PathFindingObserver[0]);
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            arrLocal[i].update(this, arg);
    }

    /**
     * Clears the observer list so that this object no longer has any observers
     */
    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    /**
     * Marks this as having been changed;
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed
     * @see PathFindingObservable#notifyObservers(LinkedHashSet)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed
     * @see PathFindingObservable#clearChanged()
     * @see PathFindingObservable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers
     */
    public synchronized int countObservers() {
        return obs.size();
    }
}