package recodercs.csharp.attributes;

import recodercs.csharp.NonTerminalProgramElement;

/**
 * @author orosz
 * Container interface for AttributeArgument class. 
 */
public interface AttributeArgumentContainer extends NonTerminalProgramElement {

  public int getAttributeParameterCount();
  
  public AttributeArgument getAttributeParameterAt(int idx);

}
