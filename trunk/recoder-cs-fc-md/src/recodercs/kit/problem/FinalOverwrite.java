// This file is part of the RECODER library and protected by the LGPL.

package recodercs.kit.problem;

import recodercs.abstraction.Member;

/**
   Problem report indicating that a member has been overwritten that
   was declared final, or was in a final type.
 */
public class FinalOverwrite extends Conflict {

    private Member member;
    
    public FinalOverwrite(Member member) {
	this.member = member;
    }

    public Member getMember() {
	return member;
    }    
}
