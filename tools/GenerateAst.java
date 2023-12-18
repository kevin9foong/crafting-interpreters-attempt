package tools;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Script to generate the AST classes.
 * Not officially part of the interpreter.
 */
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64); // 64 is the exit code for command line usage error based on unix sysexits.h
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
            // LHS is the class name, RHS is the fields for the constructor, : is delimiter
            "Binary : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal : Object value",
                "Unary : Token operator, Expr right"
                ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types)
        throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package jlox;");
        writer.println();

        writer.println("import java.util.List;");
        writer.println();

        writer.println("abstract class " + baseName + " {");

        // define the subclass for base class
        for (String type : types) {
            String className = type.split(":")[0].trim(); // get the class name
            String fields = type.split(":")[1].trim(); // get the fields
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");

        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName,
        String className, String fieldList) {
            writer.println("\tstatic class " + className + " extends " +
                baseName + " {");

            writer.println("\t\t" + className + "(" + fieldList + ") {");

            String[] fields = fieldList.split(", ");
            for (String field : fields) {
                String name = field.split(" ")[1]; // get the field name
                writer.println("\t\t\tthis." + name + " = " + name + ";");
            }

            writer.println("\t\t}");

            writer.println();
            for (String field : fields) {
                writer.println("\t\tfinal " + field + ";");
            }

            writer.println("\t}");
    }
}
