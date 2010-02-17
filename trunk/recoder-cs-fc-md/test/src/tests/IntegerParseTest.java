package tests;
import recodercs.csharp.CSharpProgramFactory;

/**
 * @author joey
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class IntegerParseTest {

	public static void main(String[] args) {
		System.out.print(CSharpProgramFactory.parseInt(""+((long)Integer.MAX_VALUE+Integer.MAX_VALUE-10)));
	}

}
