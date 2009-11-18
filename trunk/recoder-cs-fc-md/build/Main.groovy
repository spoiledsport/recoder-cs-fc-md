public class Main {
	
	public static void main(String[] args) {

		System.setProperty("line.separator","\r\n");

		if (args[].length == 0) usage()
		else {
			println "----------\nstarting with settings:\ninput directory: " + args[0] + "\npattern: " + args[1] + "\ndebug: " + args[2] 
			new LukeFileWalker(args[0], args[1], args[2]).walk()
		}

		def usage() {
			println 'parameters: "inputDirectory" "regex" "debug"'
		}
	}
	
}