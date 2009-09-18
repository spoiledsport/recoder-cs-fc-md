package recoder.convenience;

import recoder.csharp.*;

/**
 * @author orosz
 *
 * This is an extension to the standard tree walker, which also provides 
 * information about the current depth in the tree. (This may be useful for
 * printing a syntax tree).
 * 
 */
public class LeveledTreeWalker extends AbstractTreeWalker {

	/** An own stack to store the levels of each object */
	private int[] levels;

	protected LeveledTreeWalker(int initialStackCapacity) {
		super(initialStackCapacity);
		levels = new int[initialStackCapacity];
	}

	public LeveledTreeWalker(ProgramElement root) {
		super(root);
		levels = new int[stack.length];
	}

	public LeveledTreeWalker(ProgramElement root, int initialStackCapacity) {
		super(root, initialStackCapacity);
		levels = new int[initialStackCapacity];
	}

	/**
	   Reset the walker to a new start position reusing existing objects.
	 */
	public void reset(ProgramElement root) {
		super.reset(root);
	}

	/** The level of the current object */
	private int currentLevel;

	public boolean next() {
		if (count == 0) {
			current = null;
			return false;
		}
		current = stack[--count]; // pop	
		currentLevel = levels[count];

		if (current instanceof NonTerminalProgramElement) {
			NonTerminalProgramElement nt = (NonTerminalProgramElement) current;
			int s = nt.getChildCount();
			if (count + s >= stack.length) {
				ProgramElement[] newStack =
					new ProgramElement[Math.max(stack.length * 2, count + s)];
				System.arraycopy(stack, 0, newStack, 0, count);
				stack = newStack;

				int[] newLevels = new int[stack.length];
				System.arraycopy(levels, 0, newLevels, 0, count);
				levels = newLevels;
			}
			for (int i = s - 1; i >= 0; i -= 1) {
				stack[count] = nt.getChildAt(i); // push
				levels[count] = currentLevel + 1;
				count++;
			}
		}
		return true;
	}

	/** Returns the level of the current object inside the tree (starting from 0)
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
}
