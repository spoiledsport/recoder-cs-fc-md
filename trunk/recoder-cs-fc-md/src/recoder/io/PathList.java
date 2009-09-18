// This file is part of the RECODER library and protected by the LGPL.

package recoder.io;

import java.io.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import recoder.util.*;
import recoder.list.*;

/** 
    This class describes a list of search paths. Search paths may
    contain a mixture of directories, logical directories (e.g. "."),
    and archive files (e.g. ".jar", ".zip").
    @author RN
    @author AL
*/
public class PathList {

    /**
       Caches directory content queries. Map<File, NaturalHashSet>.
     */
    private MutableMap dirContents = new NaturalHashTable();
    
    /**
       Caches known directories. Map<File, File>.
     */
    private MutableMap knownDirs = new NaturalHashTable();

    /**
       Caches names of types relative to the search path that are known to
       be unknown. Set<String>.
     */
    private final MutableSet notFound = new NaturalHashSet();

    /** 
	Maps filenames to data locations. Map<String, DataLocation>.
    */
    private final MutableMap locations = new NaturalHashTable();

    /**
       Stores path entries. List<File|ZipFile>.
     */
    private final ObjectMutableList paths = new ObjectArrayList();



    /** creates a new empty path list */
    public PathList() { }

    /** creates a path list from the given path string
        @param pathStr a single path string, e.g. the content of <tt>CLASSPATH</tt>
    */
    public PathList(String pathStr) {
        add(pathStr);
    }

    /** creates a path list from the given strings.
        The single strings are interpreted as path strings.
        @param paths the array of path strings to be added
    */
    public PathList(String paths[]) {
        for (int i = 0; i < paths.length; i++) {
            add(paths[i]);
        }
    }

    /**
       Empties the caches of this service.
       @since 0.72
     */
    public void flushCaches() {
	dirContents.clear();
	knownDirs.clear();
	notFound.clear();	
    }

    private void addPath(String path) {
        File f = new File(path);
        if (f.isFile()) {
            try {
                paths.add(new ZipFile(f));
            }
            catch (IOException ioe) {
            	// Add as normal file
            	paths.add(f);
            }
        } else if (f.isDirectory()) {
            paths.add(f);
        }
    }

    /** adds the given paths to the list.
        @param pathStr the string containing the paths
        @return the number of paths added from the path string
    */
    public int add(String pathStr) {
        int result = 0;
        if (pathStr != null) {
	    String[] paths = StringUtils.split(pathStr, File.pathSeparatorChar);
	    result = paths.length;
	    for (int i = 0; i < result; i++) {
		String path = paths[i].trim();
                if (!path.equals("")) {
                    addPath(path);
                }
            }
            notFound.clear(); // clear the buffer of illegal requests
        }
        return result;
    }

    /**
       Returns a set of file names in the given directory.
     */
    protected Set getContents(File directory) {
        MutableSet result = (MutableSet)dirContents.get(directory);
        if (result == null) {
            dirContents.put(directory, result = new NaturalHashSet());
            String[] list = null;
	    list = directory.list();
            for (int i = 0; i < list.length; i++) {
                result.add(list[i]);
            }
        }
        return result;
    }

    private final static File NO_FILE = new File("");

    private File getDir(File parent, String name) {
	File attempt = new File(parent, name);
        File result = (File)knownDirs.get(attempt);
        if (result == null) {
            result = attempt;
            if (!result.exists()) {
                result = NO_FILE;
            }
            knownDirs.put(attempt, result);
        }
        return (result == NO_FILE) ? null : result;
    }

    private DataLocation getLocation(Object p, String relativeName) {
        if (p instanceof ZipFile) {
            ZipFile zf = (ZipFile)p;
            if (zf.getEntry(relativeName) != null) {
                return new ArchiveDataLocation(zf, relativeName);
            } else {
                // archives use unix-paths
                String hs = relativeName.replace(File.separatorChar, '/');
                if (zf.getEntry(hs) != null) {
                    return new ArchiveDataLocation(zf, hs);
                }
            }
        } else if (p instanceof File) {
            File dir = (File)p;
            int sep = relativeName.lastIndexOf(File.separatorChar);
            if (sep >= 0) {
		dir = getDir(dir, relativeName.substring(0, sep));
		if (dir == null) {
		    return null;
		}
		relativeName = relativeName.substring(sep + 1);
            }
	    if (getContents(dir).contains(relativeName)) {
		return new DataFileLocation(new File(dir, relativeName));
            }
        }
        return null;
    }

    /** 
	Looks for the file with the given relative file name
        in the path list and returns the according location object.
        If no such file can be found within the paths, the method returns
        <tt>null</tt>.
        @param  relativeName the relative name of the file
        @return the location object or <tt>null</tt> if the file could not be found.
    */
    public DataLocation find(String relativeName) {
        DataLocation result = (DataLocation)locations.get(relativeName);
        if ((result == null) && (!notFound.contains(relativeName))) {
            for (int i = 0; result == null && i < paths.size(); i++) {
                result = getLocation(paths.getObject(i), relativeName);
            }
            if (result != null) {
                locations.put(relativeName, result);
            } else {
                notFound.add(relativeName);
            }
        }
        return result;
    }

    /**
       Returns a relative name of the given absolute file name removing
       a directory path prefix if the prefix occurs in this path list.
       If the filename is a directory path that is already in this path list,
       a "." is returned. In any other case, the absolute file name is
       passed through.
       @param absoluteFilename an absolute file name.
       @return a name for this file, possibly relative to this search path.
     */
    public String getRelativeName(String absoluteFilename) {
	for (int i = 0; i < paths.size(); i++) {
	    Object o = paths.getObject(i);
	    if (o instanceof File) {
		File p = (File)o;
		if (p.isDirectory()) {
		    String pathfilename = p.getAbsolutePath();
		    if (absoluteFilename.startsWith(pathfilename)) {
			int pathfilenamelen = pathfilename.length();
			if (absoluteFilename.length() == pathfilenamelen) {
			    return ".";
			}
			if (pathfilename.charAt(pathfilenamelen - 1) != File.separatorChar) {
			    pathfilenamelen += 1; // cut one more
			}
			return absoluteFilename.substring(pathfilenamelen);
		    }
		}
	    }
	}
	return absoluteFilename;
    }

    /** Looks for files with the given relative file name
        in the path list and returns an array containing the full path names
        of each match. If no file could be located, this method returns an
        empty array.
        @param  relativeName the relative name of the file
        @return an array containing the full paths of all matching files
    */
    public DataLocation[] findAll(String relativeName) {
        DataLocation[] tmpRes = new DataLocation[paths.size()];
        int count = 0;
        for (int i = 0; i < paths.size(); i++) {
            DataLocation dl = getLocation(paths.getObject(i), relativeName);
            if (dl != null) {
                tmpRes[count++] = dl;
            }
        }
        // create the result array
        DataLocation[] result = new DataLocation[count];
        System.arraycopy(tmpRes, 0, result, 0, count);
        return result;
    }

    // the filter must be able to accept null parent directories
    // (e.g. for ZipEntries)
    public DataLocation[] findAll(FilenameFilter filter) {
        Vector res = new Vector();
        for (int i = 0, s = paths.size(); i < s; i++) {
            Object f = paths.getObject(i);
            if (f instanceof ZipFile) {
                ZipFile zf = (ZipFile)f;
                Enumeration menum = zf.entries();
                while (menum.hasMoreElements()) {
                    ZipEntry e = (ZipEntry)menum.nextElement();
                    String name = e.getName();
                    if (filter.accept(null, name)) {
                        DataLocation loc = (DataLocation)locations.get(name);
                        if (loc == null) {
                            loc = new ArchiveDataLocation(zf, name);
                            locations.put(name, loc);
                        }
                        res.addElement(loc);
                    }
                }
            } else {
                File fi = (File)f;
                if (fi.exists()) {
                    FileCollector fc = new FileCollector(fi);
                    while (fc.next(filter)) {
                        File file = fc.getFile();
			try {
			    String name = file.getCanonicalPath();
			    DataLocation loc =
				(DataLocation)locations.get(name);
			    if (loc == null) {
				loc = new DataFileLocation(file);
				locations.put(name, loc);
			    }
			    res.addElement(loc);
			} catch (IOException ioe) {}
                    }
                }
            }
        }
        DataLocation[] result = new DataLocation[res.size()];
        res.copyInto(result);
        return result;
    }


    /** 
	Returns the string representation of the path list.
        @return the concatenated pathstring.
    */
    public String toString() {
        String result;
        if (paths.isEmpty()) {
            result = "";
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < paths.size(); i++) {
                sb.append(File.pathSeparatorChar);
                Object f = paths.getObject(i);
                if (f instanceof ZipFile) {
                    sb.append(((ZipFile)f).getName());
                } else {
                    sb.append(((File)f).getPath());
                }
            }
            result = sb.toString().substring(1);
        }
        return result;
    }

}
