package recoder.csharp;

/**
 * @author kis
 *
 * Interface to hold a MemberName.
 */
public interface MemberNameContainer extends NonTerminalProgramElement {

	MemberName getMemberName();
	void setMemberName(MemberName mname);

}
