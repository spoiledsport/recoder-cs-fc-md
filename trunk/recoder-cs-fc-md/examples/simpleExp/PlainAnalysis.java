package simpleExp;

import java.io.PrintWriter;
import java.util.*;

import recodercs.CrossReferenceServiceConfiguration;
import recodercs.io.DefaultSourceFileRepository;
import recodercs.service.CrossReferenceSourceInfo;
import recodercs.service.DefaultCrossReferenceSourceInfo;
import recodercs.service.DefaultNameInfo;
import recodercs.*;
import recodercs.abstraction.ClassType;
import recodercs.abstraction.DeclaredType;
import recodercs.abstraction.Field;
import recodercs.abstraction.Method;
import recodercs.abstraction.Namespace;
import recodercs.abstraction.ProgramModelElement;
import recodercs.abstraction.Type;
import recodercs.csharp.*;
import recodercs.io.*;
import recodercs.list.*;
import recodercs.service.*;
import recodercs.util.ProgressEvent;
import recodercs.util.ProgressListener;

public class PlainAnalysis {
    private static int level;

    private static final String identStr = "    ";

    public static void main(String[] args) {
        CrossReferenceServiceConfiguration sc =
            new CrossReferenceServiceConfiguration();
            
        sc.getProjectSettings().setErrorHandler(new PlainAnalysisErrorHandler());
        
        sc.getChangeHistory().updateModel();
        
        
        
        System.out.println("\nFiles...");
        System.out.println(
            ((DefaultSourceFileRepository) sc.getSourceFileRepository())
                .information());

        System.out.println("\nNames...");
        System.out.println(((DefaultNameInfo) sc.getNameInfo()).information());
        System.out.println("\nReferences...");
        System.out.println(((DefaultCrossReferenceSourceInfo)sc.getCrossReferenceSourceInfo()).information());
        System.out.println();

        setLevel(0);

        NamespaceList nsl = sc.getNameInfo().getNamespaces();
        int count = nsl.size();

        System.out.println("Printing ");
        for (int i = 0; i < count; i++) {
            printNamespace(nsl.getNamespace(i));
        }

        long totalMem = Runtime.getRuntime().totalMemory();
        long usedMem = recodercs.util.Debug.getUsedMemory();
        System.out.println(
            "Memory used: " + usedMem + " (total: " + totalMem + ")");
    }

    /**
     * Method printNamespace.
     * @param namespace
     */
    private static void printNamespace(Namespace namespace) {
        addLevel();
        printString("Namespace:" + namespace.getFullName());
        DeclaredTypeList dl = namespace.getDeclaredTypes();
        int count = dl.size();
        for (int i = 0; i < count; i++) {
            printDeclaredType(dl.getDeclaredType(i));
        }
        addLevel();
        printString("References");
        CrossReferenceSourceInfo si = (CrossReferenceSourceInfo) namespace.getProgramModelInfo().getServiceConfiguration().getSourceInfo();
        NamespaceReferenceList list = si.getReferences(namespace);
        
        for (int i=0; i<list.size(); i++) {
            Reference ref=list.getPackageReference(i);
            printReference(ref);
        }
        removeLevel();
        removeLevel();
    }

    /**
     * Method printDeclaredType.
     * @param declaredType
     */
    private static void printDeclaredType(DeclaredType declaredType) {
        addLevel();
        if (declaredType instanceof ClassType) {
            printClassType((ClassType) declaredType);
        } else {
            printString("Declared Type:" + declaredType.getFullName());
        }
        removeLevel();
    }

    /**
     * Method printClassType.
     * @param classType
     */
    private static void printClassType(ClassType classType) {
        addLevel();
        printString("Class Type:" + classType.getFullName());
        addLevel();
        printString("Fields");
        FieldList fl = classType.getFields();
        int count = fl.size();
        for (int i = 0; i < count; i++) {
            printField(fl.getField(i));
        }
        printString("Methods");
        MethodList ml = classType.getMethods();
        count = ml.size();
        for (int i = 0; i < count; i++) {
            printMethod(ml.getMethod(i));
        }
        
        printString("References");

        CrossReferenceSourceInfo si = (CrossReferenceSourceInfo) classType.getProgramModelInfo().getServiceConfiguration().getSourceInfo();
        TypeReferenceList list = si.getReferences(classType);
        
        for (int i=0; i<list.size(); i++) {
            Reference ref=list.getTypeReference(i);
            printReference(ref);
        }
        
        
        removeLevel();
        removeLevel();
    }

    /**
     * Method printMethod.
     * @param method
     */
    private static void printMethod(Method method) {
        addLevel();
        printString("Method: " + method.getFullName());
        printReturnType(method.getReturnType());
        printSignature(method.getSignature());
        addLevel();
        printString("References");

        CrossReferenceSourceInfo si = (CrossReferenceSourceInfo) method.getProgramModelInfo().getServiceConfiguration().getSourceInfo();
        MemberReferenceList list = si.getReferences(method);
        
        for (int i=0; i<list.size(); i++) {
            Reference ref=list.getMemberReference(i);
            printReference(ref);
        }
        removeLevel();
        removeLevel();
    }

    /**
     * Method printSignature.
     * @param typeList
     */
    private static void printSignature(TypeList typeList) {
        addLevel();
        printString("Signature");
        int count = typeList.size();
        for (int i = 0; i < count; i++) {
            printType(typeList.getType(i));
        }
        removeLevel();
    }

    /**
     * Method printType.
     * @param type
     */
    private static void printType(Type type) {
        addLevel();
        if (type == null) {
            printString("NullType");
        } else {
            printString("Type:" + type.getFullName());
        }
        removeLevel();
    }

    /**
     * Method printReturnType.
     * @param type
     */
    private static void printReturnType(Type type) {
        addLevel();
        printString("Return Type:");
        if (type != null) {
            printType(type);
        } else {
            printString("void");
        }
        removeLevel();
    }

    /**
     * Method printField.
     * @param field
     */
    private static void printField(Field field) {
        addLevel();
        printString("Field:" + field.getFullName());
        printType(field.getType());
        removeLevel();
    }

    private static void printString(String str) {
        for (int c = 0; c < level; c++) {
            System.out.print(identStr);
        }
        System.out.println(str);

    }

    /**
     * Sets the level.
     * @param level The level to set
     */
    private static void setLevel(int level) {
        PlainAnalysis.level = level;
    }

    /**
     * Method removeLevel.
     */
    private static void removeLevel() {
        if (level > 0)
            level--;
    }

    /**
     * Method addLevel.
     */
    private static void addLevel() {
        level++;
    }
    
    private static void printReference(Reference ref) {
            addLevel();
            NonTerminalProgramElement parent=ref.getASTParent();
            while (!(parent instanceof CompilationUnit)) {
                parent = parent.getASTParent();
            }
            String location="";
            if (parent != null) {
                location = ((CompilationUnit) parent).getDataLocation().toString();
            }
            printString(location+" @"+ref.getStartPosition().toString());
            removeLevel();    
    }


}
