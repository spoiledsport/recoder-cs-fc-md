package metrics.metrics;

import java.util.ArrayList;

import recoder.csharp.ProgramElement;
import recoder.service.CrossReferenceSourceInfo;
import recoder.service.SourceInfo;

public abstract class DSMetricCalculator {
	
	protected CrossReferenceSourceInfo si;
	protected ArrayList<ProgramElement> types;
	protected String shortcut;
	protected String fullName;
	protected String description;

	
	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract void calculate();

	public ArrayList<ProgramElement> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<ProgramElement> types) {
		this.types = types;
	}

	public SourceInfo getSi() {
		return si;
	}

	public void setSi(CrossReferenceSourceInfo si) {
		this.si = si;
	}
	
}
