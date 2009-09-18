// This file is part of the RECODER library and protected by the LGPL.

package recoder.service;

import recoder.*;
import recoder.abstraction.*;
import recoder.abstraction.Namespace;
import recoder.convenience.*;
import recoder.util.*;
import recoder.list.*;


abstract class ClassTypeTopSort implements Formats {
	    
    private ClassTypeMutableList classesDFS = new ClassTypeArrayList(32);

    private int[] indeg = new int[32];
    
    protected abstract ClassTypeList getAdjacent(ClassType c);
    
    private void initIndeg() {
	for (int i = 0; i < indeg.length; i++) {
	    indeg[i] = 0;
	}
    }

    private int incrIndeg(int index) {
	while (index >= indeg.length) {
	    int[] n = new int[indeg.length*2];
	    System.arraycopy(indeg, 0, n, 0, indeg.length);
	    indeg = n;
	}
	return ++indeg[index];
    }
    
    private int decrIndeg(int index) {
	return --indeg[index];
    }
    
    private void addClass(ClassType c) {
	if (c != null) {
	    int idx = classesDFS.indexOf(c);
	    if (idx == -1) {
		classesDFS.add(c);
		idx = classesDFS.size() - 1;
		ClassTypeList neighbors = getAdjacent(c);
		int s = neighbors.size();
		for (int i = 0; i < s; i++) {
		    addClass(neighbors.getClassType(i));
		}
	    }
	    incrIndeg(idx);
	}
    }
    
    private void sort(ClassType c, ClassTypeMutableList result) {
	if (c != null) {
	    int idx = classesDFS.indexOf(c);
	    if (idx == -1) {
		Debug.error(Format.toString("Could not find " + ELEMENT_LONG, c) +
			    "\nList: " + Format.toString("%N", result) + "\n" +
			    Debug.makeStackTrace());
		System.exit(0);
	    }
	    if (decrIndeg(idx) == 0) {
		result.add(c);
		ClassTypeList neighbors = getAdjacent(c);
		int s = neighbors.size();
		for (int i = 0; i < s; i++) {
		    sort(neighbors.getClassType(i), result);
		}
	    }
	}
    }
    
    public ClassTypeMutableList getAllTypes(ClassType c) {
	initIndeg();
	classesDFS.clear();
	addClass(c);
	ClassTypeMutableList result = 
	    new ClassTypeArrayList(classesDFS.size());
	sort(c, result);
	if (result.size() < classesDFS.size()) {
	    throw new RuntimeException("Cyclic inheritance detected!");
	}
	return result;
    }
}
