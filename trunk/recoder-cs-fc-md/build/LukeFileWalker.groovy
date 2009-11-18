
public class LukeFileWalker {
	
	def filePattern
	def path
	def debug
	def fs = File.separator
	
	public LukeFileWalker(path, filePattern, debug) {
		this.path = path
		this.filePattern = (/${filePattern}/)
		this.debug = debug
		System.setProperty("line.separator","\r\n");
	}
	
	def walk () {
		new File(this.path).eachFileRecurse () { file -> 
			if (file.name =~ this.filePattern) {
				File newFile = new File(file.getPath() + "_new.cs")
				newFile.write file.getText() + "\r\n\r\n", "ISO-8859-1"
				if (debug) println file.getPath() + "\n\t==> " + file.getPath() + "_new.cs"
			}
		}
	}
	
	
}

