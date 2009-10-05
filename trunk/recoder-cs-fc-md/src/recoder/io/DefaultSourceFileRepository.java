// This file is part of the RECODER library and protected by the LGPL.

package recoder.io;

import java.beans.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import recoder.*;
import recoder.convenience.*;
import recoder.list.*;
import recoder.csharp.*;
import recoder.csharp.declaration.*;
import recoder.csharp.reference.*;
// import recoder.kit.*;
import recoder.service.*;
import recoder.util.*;

/**
 * TODO: Implement the described behaviour.
 * The Csharp Source repository. On initialization it parses all the 
 * sources it founds, and maintains a list of CompilationUnits. 
 * If a CompilationUnit containing a specific type is requested, The whole 
 * syntax forest is searched, and all matches are found. If there are more 
 * than one matches (name clash), the system throws a RecoderException.  
    @author RN
    @author AL
    @author kis
*/
public class DefaultSourceFileRepository
    extends AbstractService
    implements SourceFileRepository, ChangeHistoryListener, PropertyChangeListener {

    private final static boolean DEBUG = false;

    /**
       Cache: data location to compilation units.
     */
    private final MutableMap location2cu = new NaturalHashTable();

    /** Cache: The compilation units not attached to the model. The service 
     * stores the compilation units here on initialization. */ 
    private final MutableMap unattached_location2cu = new NaturalHashTable();

    /**
       Set of units that have been changed and have to be rewritten.
     */
    private final MutableSet changedUnits = new IdentityHashSet();

    /**
       Set of units that are obsolete and should be deleted.
     */
    private final MutableSet deleteUnits = new IdentityHashSet();

    /**
       The change history service.
     */
    private ChangeHistory changeHistory;

    /**
       Cached search path list.
     */
    private PathList searchPathList;

    /**
       Cached output path.
     */
    private File outputPath;

    /**
       Progress listener management.
     */
    ProgressListenerManager listeners = new ProgressListenerManager(this);

    /**
     @param config the configuration this services becomes part of.
    */
    public DefaultSourceFileRepository(ServiceConfiguration config) {
        super(config);
    }

    public void initialize(ServiceConfiguration cfg)
        throws InitializationException {
        super.initialize(cfg);
        changeHistory = cfg.getChangeHistory();
        changeHistory.addChangeHistoryListener(this);
        ProjectSettings settings = cfg.getProjectSettings();
        settings.addPropertyChangeListener(this);
        searchPathList = settings.getSearchPathList();
        outputPath = new File(settings.getProperty(PropertyNames.OUTPUT_PATH));
        try {
            suckUp();
        } catch (ParserException pe) {
            throw new InitializationException(pe);
        }
    }

    protected final PathList getSearchPathList() {
        return searchPathList;
    }

    protected final File getOutputPath() {
        return outputPath;
    }

    public void addProgressListener(ProgressListener l) {
        listeners.addProgressListener(l);
    }

    public void removeProgressListener(ProgressListener l) {
        listeners.removeProgressListener(l);
    }

    public void propertyChange(PropertyChangeEvent evt) throws ModelException {
        String changedProp = evt.getPropertyName();
        if (changedProp.equals(ProjectSettings.INPUT_PATH)) {
            // should check for admissibility of the new path
            // if it has been added only, there is nothing to do
            // otherwise, for all class types check if the location
            // would have changed; if so, invalidate them
            throw new ModelException("The current implementation doesn't support changing the input and output path.");
            //			searchPathList =
            //				serviceConfiguration.getProjectSettings().getSearchPathList();
        } else if (changedProp.equals(ProjectSettings.OUTPUT_PATH)) {
            throw new ModelException("The current implementation doesn't support changing the input and output path.");
            //			outputPath =
            //				new File(
            //					serviceConfiguration.getProjectSettings().getProperty(
            //						ProjectSettings.OUTPUT_PATH));
        }
    }

    // remove cu from repository
    private void deregister(CompilationUnit cu) {
        DataLocation loc = cu.getDataLocation();
        if (loc != null) {
            if (location2cu.get(loc) == cu) {
                location2cu.remove(loc);
                changedUnits.remove(cu); // no need to write it back
                if (DEBUG)
                    Debug.log("Deregistering " + loc);
                DataLocation orig = cu.getOriginalDataLocation();
                if (!loc.equals(orig)) {
                    // remove it except when from original location
                    deleteUnits.add(loc);
                }
            }
        }
    }

    // add cu to repository
    private void register(CompilationUnit cu) {
        DataLocation loc = cu.getDataLocation();
        if (loc == null) {
            changedUnits.add(cu); // assume the file is not up to date
            loc = createDataLocation(cu);
            cu.setDataLocation(loc);
        }
        if (location2cu.get(loc) != cu) {
            if (DEBUG)
                Debug.log("Registering " + loc);
            deleteUnits.remove(loc);
            location2cu.put(loc, cu);
        }
    }

    /** check if the given program element can influence on the canonical
     name of the compilation unit
     In C# there aren't such elements.
     */
    private boolean isPartOfUnitName(ProgramElement pe) {
        //		if (pe instanceof Identifier || pe instanceof NamespaceReference) {
        //			return isPartOfUnitName(pe.getASTParent());
        //		}
        //		if (pe instanceof NamespaceSpecification) {
        //			return true;
        //		}
        //		if (pe instanceof TypeDeclaration) {
        //			NonTerminalProgramElement parent = pe.getASTParent();
        //			return (parent instanceof CompilationUnit)
        //				&& (((CompilationUnit) parent).getPrimaryTypeDeclaration() == pe);
        //		}
        return false;
    }

    // possible optimization: react on replacements
    public void modelChanged(ChangeHistoryEvent changes) {
        TreeChangeList changed = changes.getChanges();
        for (int i = changed.size() - 1; i >= 0; i -= 1) {
            TreeChange tc = changed.getTreeChange(i);
            ProgramElement pe = tc.getChangeRoot();
            CompilationUnit cu = tc.getCompilationUnit();
            if (pe == cu) {
                if (tc instanceof AttachChange) {
                    register(cu);
                } else if (tc instanceof DetachChange) {
                    deregister(cu);
                }
            } else {
                if (isPartOfUnitName(pe)) {
                    // re-register under new location
                    DataLocation loc = cu.getDataLocation(); // old location
                    DataLocation loc2 = createDataLocation(cu); // new location
                    if (!loc.equals(loc2)) {
                        deregister(cu);
                        cu.setDataLocation(loc2);
                        register(cu);
                    }
                }
                changedUnits.add(cu);
            }
            if (cu == null) {
                Debug.log("Null Unit changed in " + tc);
            }
        }
    }

    /** Searches for the location of the source file for the given class.
     @param classname the name of the class for which the source file should be
     looked up.
    */
    public DataLocation findSourceFile(String classname) {
        // In C# this 
        //	// possible optimzation: cache it !!!
        //	String file = Naming.dot(Naming.makeFilename(classname), "java");
        //	return getSearchPathList().find(file);
        return null;
    }

    protected CompilationUnit getCompilationUnitFromLocation(DataLocation loc)
        throws ParserException {
        Debug.asserta(loc, "Null location for compilation unit");
        CompilationUnit result = (CompilationUnit) location2cu.get(loc);
        if (result == null) {
            // Check the unattached sources 
            result = (CompilationUnit) unattached_location2cu.get(loc); 
        }
        if (result != null) {
        	// Attach the unattached CU to the model
        	location2cu.put(loc,result);
        	changeHistory.attached(result);
        	changeHistory.updateModel();
            return result;
        }
        
        // ok - lets parse the sources
        try {
            Reader in;
            if (!loc.hasReaderSupport() || (in = loc.getReader()) == null) {
                Debug.error("Location of source file provides no reader");
                return null;
            }
            result =
                serviceConfiguration.getProgramFactory().parseCompilationUnit(
                    in);
            in.close();
            loc.readerClosed();
            result.setDataLocation(loc);
            unattached_location2cu.put(loc,result);
            location2cu.put(loc, result); //let this be done by the history
            changeHistory.attached(result);
            changeHistory.updateModel();
        } catch (IOException e) {
            Debug.error("Exception while reading from input stream: " + e);
        } catch (ParserException pe) {
            Debug.error("Exception while parsing unit " + loc);
            throw pe;
        }
        return result;
    }

    public CompilationUnit getCompilationUnitFromFile(String filename)
        throws ParserException {
        Debug.asserta(filename);
        File f = new File(filename);
        DataLocation loc = null;
        if (f.isFile() && f.isAbsolute()) {
            String newfilename = getSearchPathList().getRelativeName(filename);
            if (newfilename.equals(filename)) {
                // not part of the regular search path; load anyway...
                loc = new DataFileLocation(f);
            } else {
                loc = getSearchPathList().find(newfilename);
            }
        } else {
            loc = getSearchPathList().find(filename);
            // System.err.println("Location for " + filename +": " + loc);
        }
        return loc != null ? getCompilationUnitFromLocation(loc) : null;
    }

    public CompilationUnitList getCompilationUnitsFromFiles(String[] filenames)
        throws ParserException {
        Debug.asserta(filenames);
        CompilationUnitMutableList res = new CompilationUnitArrayList();
        listeners.fireProgressEvent(
            0,
            filenames.length,
            "Importing Source Files");
        for (int i = 0; i < filenames.length; i += 1) {
            listeners.fireProgressEvent(
                i,
                new StringBuffer("Parsing ").append(filenames[i]).toString());
            CompilationUnit cu = getCompilationUnitFromFile(filenames[i]);
            if (cu != null) {
                res.add(cu);
            }
        }
        listeners.fireProgressEvent(filenames.length);
        return res;
    }

    /** Gets a CompilationUnit, that contains a type of a given name.
     */
    public CompilationUnit getCompilationUnit(String typename)
        throws ModelException {
        Enumeration menum = location2cu.elements();
        TypeFinderVisitor tfv = new TypeFinderVisitor();
        TypeDeclarationMutableList tl = new TypeDeclarationArrayList();
        CompilationUnit cu = null;
        while (menum.hasMoreElements()) {
            CompilationUnit cutmp = (CompilationUnit) menum.nextElement();
            TypeDeclarationList res = tfv.findType(typename, cutmp);
            tl.add(res);
            if (tl.size() > 1) {
                throw new ModelException(
                    "Name clash in model, while searching for type named "
                        + typename);
            }
            if (res.size() == 1) {
                cu = cutmp;
            }

        }
        if (cu == null) {
            // Maybe the compilation unit has not been attached yet...
            menum = unattached_location2cu.elements();
            tl = new TypeDeclarationArrayList();
            while (menum.hasMoreElements()) {
                CompilationUnit cutmp = (CompilationUnit) menum.nextElement();
                TypeDeclarationList res = tfv.findType(typename, cutmp);
                tl.add(res);
                if (tl.size() > 1) {
                    throw new ModelException(
                        "Name clash in model, while searching for type named "
                            + typename);
                }
                if (res.size() == 1) {
                    cu = cutmp;
                }

            }
            if (cu != null) {
                location2cu.put(cu.getDataLocation(),cu);
                changeHistory.attached(cu);
                changeHistory.updateModel();
            }
        }
        return cu;

    }

    public CompilationUnitList getCompilationUnits() {
        changeHistory.updateModel(); // update arrival of CU's
        return getKnownCompilationUnits();
    }

    public CompilationUnitList getKnownCompilationUnits() {
        int n = location2cu.size();
        CompilationUnitMutableList res = new CompilationUnitArrayList(n);
        Enumeration menum = location2cu.elements();
        for (int i = 0; i < n; i++) {
            res.add((CompilationUnit) menum.nextElement());
        }
        return res;
    }

    public final static FilenameFilter CSHARP_FILENAME_FILTER =
        new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith("_new.cs");
        }
    };
    
    public final static FilenameFilter CSHARP_FILENAME_FILTER_NEW =
        new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".cs_new");
        }
    };

    public CompilationUnitList getAllCompilationUnitsFromPath()
        throws ParserException {
        return getAllCompilationUnitsFromPath(CSHARP_FILENAME_FILTER);
    }

    /**
     * Gets all Compilation Units from the PathList. 
     * TODO: The Current implementation discards the filter parameter.
     * */
    public CompilationUnitList getAllCompilationUnitsFromPath(FilenameFilter filter)
        throws ParserException {
        return getKnownCompilationUnits();
    }

    /** Parses every CompilationUnit it can find. */
    private void suckUp() throws ParserException {
        DataLocation[] locations =
            getSearchPathList().findAll(CSHARP_FILENAME_FILTER);
        listeners.fireProgressEvent(
            0,
            locations.length,
            "Importing Source Files From Path");
        for (int i = 0; i < locations.length; i++) {
            listeners.fireProgressEvent(i, "Parsing " + locations[i]);
            suckUpCompilationUnitFromLocation(locations[i]);
        }
    }

    private CompilationUnit suckUpCompilationUnitFromLocation(DataLocation loc)
        throws ParserException {
        Debug.asserta(loc, "Null location for compilation unit");
        Debug.log(loc.toString());
        CompilationUnit result = null;
        // ok - lets parse the sources
        try {
            Reader in;
            if (!loc.hasReaderSupport() || (in = loc.getReader()) == null) {
                Debug.error("Location of source file provides no reader");
                return null;
            }
            result =
                serviceConfiguration.getProgramFactory().parseCompilationUnit(
                    in);
            in.close();
            loc.readerClosed();
            result.setDataLocation(loc);
            unattached_location2cu.put(loc, result);
            //let this be done by the history
        } catch (IOException e) {
            Debug.error("Exception while reading from input stream: " + e);
        } catch (ParserException pe) {
            Debug.error("Exception while parsing unit " + loc);
            throw pe;
        }
        return result;
    }

    public boolean isUpToDate(CompilationUnit cu) {
        Debug.asserta(cu);
        if (cu.getDataLocation() == null) {
            return false;
        }
        return !changedUnits.contains(cu);
    }

    // create a new data location given the current position of the unit
    protected DataLocation createDataLocation(CompilationUnit cu) {
        String filename = Naming.toCanonicalDummyFilename(cu);
        File f = new File(getOutputPath(), filename);
        return new DataFileLocation(f);
    }

    private void printUnit(CompilationUnit cu) throws IOException {
        DataLocation location = cu.getDataLocation();
        // create output path name
        if (location == null || cu.getOriginalDataLocation() == location) {
            if (location != null) {
                location2cu.remove(location);
            }
            location = createDataLocation(cu);
            cu.setDataLocation(location);
            location2cu.put(location, cu);
        }
        if (!location.isWritable()) {
            throw new IOException(
                "Data location for " + location + " is not writable");
        }
        if (location instanceof DataFileLocation) {
            File f = ((DataFileLocation) location).getFile();
            File parent = new File(f.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        Writer w = location.getWriter();
        PrettyPrinter pprinter =
            serviceConfiguration.getProgramFactory().getPrettyPrinter(w);
        cu.accept(pprinter);
        w.flush();
        w.close();
        location.writerClosed();
    }

    public void print(CompilationUnit cu) throws IOException {
        Debug.asserta(cu);
        printUnit(cu);
        changedUnits.remove(cu);
    }

    public void printAll(boolean always) throws IOException {
        changeHistory.updateModel(); // update arrival of new cu's
        int size = always ? location2cu.size() : changedUnits.size();
        listeners.fireProgressEvent(0, size, "Exporting Source Files");
        CompilationUnit[] units = new CompilationUnit[size];
        Enumeration menum =
            always ? location2cu.elements() : changedUnits.elements();
        for (int i = 0; i < size; i += 1) {
            units[i] = (CompilationUnit) menum.nextElement();
        }
        if (DEBUG) {
            Debug.log("printing...");
        }
        for (int i = 0; i < size; i += 1) {
            if (DEBUG) {
                Debug.log("units[i].getName()");
            }
            printUnit(units[i]);
            listeners.fireProgressEvent(i + 1, units[i]);
        }
        changedUnits.clear();
    }

    /**
       Deletes all superfluous (renamed, detached) compilation unit files.
       Does not remove source files from other sources.
     */
    public void cleanUp() {
        Enumeration menum = deleteUnits.elements();
        while (menum.hasMoreElements()) {
            DataLocation loc = (DataLocation) menum.nextElement();
            if (loc instanceof DataFileLocation) {
                File f = ((DataFileLocation) loc).getFile();
                f.delete();
            }
        }
        deleteUnits.clear();

    }

    public String information() {
        return ""
            + location2cu.size()
            + " compilation units ("
            + changedUnits.size()
            + " currently changed)";
    }

}
