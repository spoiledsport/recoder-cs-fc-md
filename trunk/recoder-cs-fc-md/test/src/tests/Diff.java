package tests;
import java.io.*;

/**
 * @author kis
 *
 * Compares the contents of two files ignoring the whitespaces.
 */
public class Diff {

	InputStreamReader input1;
	InputStreamReader input2;

	long column1, column2, line1, line2;

	// The current token in stream 1
	StringBuffer token1;
	// The current token in stream 2
	StringBuffer token2;

	/**
	 * Constructor for Diff.
	 */
	public Diff(InputStreamReader input1, InputStreamReader input2) {
		this.input1 = input1;
		this.input2 = input2;
		column1 = column2 =0;
		line1 = line2 = 1;
	}

	public void reset() throws IOException {
		input1.reset();
		input2.reset();
		column1 = column2 =0;
		 line1 = line2 = 1;
	}

	public void diff() throws IOException, DiffException {
		int c1, c2;
		// Read while whitespace...
		do {
			do {
				c1 = readChar1();
			} while (c1 != -1 && Character.isWhitespace((char) c1));
			do {
				c2 = readChar2();
			} while (c2 != -1 && Character.isWhitespace((char) c2));
		}
		while (c1 == c2 && c1 != -1 && c2 != -1);
		if (c1 == -1 && c2 == -1) {
			// The files are equivalent
			return;
		} else {
			throw new DiffException(line1, column1, line2, column2,(char) c1,(char) c2);
		}

	}

	private int readChar1() throws IOException {
		int c = input1.read();
		column1++;
		if ('\n' == (char) c) {
			column1 = 0;
			line1++;
		}
		return c;
	}

	private int readChar2() throws IOException {
		int c = input2.read();
		column2++;
		if ('\n' == (char) c) {
			column2 = 0;
			line2++;
		}
		return c;
	}

	public static void main(String[] args) {
		try {
			Diff d =
				new Diff(
					new InputStreamReader(new FileInputStream(args[0])),
					new InputStreamReader(new FileInputStream(args[1])));
			d.diff();
			System.out.println("The files are equivalent!");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DiffException de) {
			System.out.println("The files are not equivalent. Difference:");
			System.out.println(
				args[0] + ": " + de.getLine1() + "," + de.getColumn1()+":"+de.getChar1());
			System.out.println(
				args[1] + ": " + de.getLine2() + "," + de.getColumn2()+":"+de.getChar2());

		}

	}

}
