package tests;
/**
 * @author kis
 *
 * Error reporting for Diff class
 */
public class DiffException extends RuntimeException {
	
	long line1,line2,column1,column2;
	
	char c1, c2;
	
	public DiffException(long line1,long column1,long line2,long column2,char c1, char c2) {
		this.line1 = line1;
		this.line2 = line2;
		this.column1 = column1;
		this.column2 = column2;
		this.c1 = c1;
		this.c2 = c2;
	}	

	/**
	 * Returns the column1.
	 * @return long
	 */
	public long getColumn1() {
		return column1;
	}

	/**
	 * Returns the column2.
	 * @return long
	 */
	public long getColumn2() {
		return column2;
	}

	/**
	 * Returns the line1.
	 * @return long
	 */
	public long getLine1() {
		return line1;
	}

	/**
	 * Returns the line2.
	 * @return long
	 */
	public long getLine2() {
		return line2;
	}

	/**
	 * Returns the c1.
	 * @return char
	 */
	public char getChar1() {
		return c1;
	}

	/**
	 * Returns the c2.
	 * @return char
	 */
	public char getChar2() {
		return c2;
	}

}
