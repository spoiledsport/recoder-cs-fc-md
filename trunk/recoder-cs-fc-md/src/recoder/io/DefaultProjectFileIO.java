// This file is part of the RECODER library and protected by the LGPL

package recoder.io;

import java.io.*;
import java.util.*;

import recoder.*;
import recoder.convenience.Naming;
import recoder.list.*;
import recoder.csharp.*;
import recoder.util.*;

/**
   Imports and exports simple properties files.
   @since 0.63
 */
public class DefaultProjectFileIO extends ProjectFileIO implements PropertyNames {

    private File file;

    /**
       Sets up a new project file IO facility that reads from and/or writes
       to the specified (.properties) file.
       @param system the service configuration to initialize.
       @param projectFile the project file to load and/or save.
     */
    public DefaultProjectFileIO(ServiceConfiguration system, File projectFile) {
	super(system);
	if (projectFile == null) {
	    throw new IllegalArgumentException("Null project file");
	}
	this.file = projectFile;
    }

    /**
       Returns the project file assigned.
       @return the current project file.
     */
    public File getProjectFile() {
	return file;
    }

    public String[] load() throws IOException {
	InputStream in = new FileInputStream(file);
	Properties props = new Properties(); //defaults
	props.load(in);
	ProjectSettings ps = getProjectSettings();
	Enumeration menum = props.propertyNames();
	while (menum.hasMoreElements()) {
	    String key = (String)menum.nextElement();
	    String oldValue = ps.getProperty(key);
	    String newValue = props.getProperty(key);
	    if (key.equals(OUTPUT_PATH)) {
		newValue = resolveFilename(file.getParent(), newValue);
	    } else if (key.equals(INPUT_PATH)) {
		newValue = resolvePathnames(file.getParent(), newValue);
	    }
	    if (oldValue == null || !newValue.equals(oldValue)) {
		ps.setProperty(key, newValue);
	    }
	}
	// normalize input path and output path and unit paths
	String prop = props.getProperty("units");
	String[] res;
	if (prop == null) {
	    res = new String[0];
	} else {
	    SourceFileRepository sfr = getSourceFileRepository();
	    String parentDir = file.getParent();
	    StringTokenizer unitNames = new StringTokenizer(prop, ", \n");
	    Vector v = new Vector();
	    while (unitNames.hasMoreTokens()) {
		String filename = unitNames.nextToken();
		if (filename != null && filename.length() > 0) {
		    filename = filename.replace('/', File.separatorChar);
		    // filename = resolveFilename(parentDir, filename);
		    v.addElement(filename);
		}
	    }
 	    res = new String[v.size()];
	    v.copyInto(res);	    
	}
	in.close();
	return res;
    }

    private String resolveFilename(String parentDir, String relativePath) {
	if (parentDir.length() == 0 || new File(relativePath).isAbsolute()) {
	    return relativePath;
	}
	String result = parentDir + File.separatorChar + relativePath;
	return result;
    }

    private String resolvePathnames(String parentDir, String relativePathList) {
	StringBuffer newpath = new StringBuffer();
	StringTokenizer paths = new StringTokenizer(relativePathList, File.pathSeparator);
	boolean firstToken = true;
	while (paths.hasMoreTokens()) {
	    String filename = paths.nextToken();
	    if (!firstToken) {
		newpath.append(File.pathSeparator);
	    }
	    newpath.append(resolveFilename(parentDir, filename));
	    firstToken = false;
	}
	return newpath.toString();
    }

    /**
       Saves the project properties to the assigned project file and adds all
       known compilation units.
     */
    public void save() throws IOException {
	OutputStream out = new FileOutputStream(file);
	StringBuffer units = new StringBuffer(1024);
	CompilationUnitList cus =
	    getSourceFileRepository().getCompilationUnits();
	for (int i = 0, s = cus.size(); i < s; i += 1) {
	    CompilationUnit cu = cus.getCompilationUnit(i);
// TODO: Implement a new mechanism for C#
	    units.append(Naming.toCanonicalDummyFilename(cu).replace(File.separatorChar, '/'));
	    if (i < s - 1) {
		units.append(',');
	    }
	}	
	Properties properties = getProjectSettings().getProperties();
	properties.put("units", units.toString());
	String path = properties.getProperty(OUTPUT_PATH);
	path = FileUtils.getRelativePath(FileUtils.getUserDirectory(), 
					 new File(path));
	properties.put(OUTPUT_PATH, path);
	path = properties.getProperty(INPUT_PATH);
	StringBuffer newpath = new StringBuffer();
	StringTokenizer tok = new StringTokenizer(path, File.pathSeparator);
	while (true) {
	    newpath.append(FileUtils.getRelativePath(FileUtils.getUserDirectory(), 
					 new File(tok.nextToken())));
	    if (tok.hasMoreTokens()) {
		newpath.append(File.pathSeparator);
	    } else {
		break;
	    }
	}
	properties.put(INPUT_PATH, newpath.toString());	
	properties.save(out, "RECODER Project File");
	out.close();
    }
}
