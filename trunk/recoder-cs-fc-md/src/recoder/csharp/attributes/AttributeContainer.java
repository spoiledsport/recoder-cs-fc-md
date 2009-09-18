package recoder.csharp.attributes;

import recoder.csharp.NonTerminalProgramElement;

/**
 * @author orosz
 * Container interface for Attribute class.
 */
public interface AttributeContainer extends NonTerminalProgramElement {

  public int getAttributeCount();
  
  public Attribute getAttributeAt(int idx);

}
