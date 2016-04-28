# CSIS658-Project

## Set up

- Get the latest version of OpenJDK
- Compile OpenJDK in debug mode.
- Once finished, copy test classes, cobertura files, and junit files to the resulting JDK bin.
- From a command prompt, navigate to the JDK bin and run "sh ./cobertura-instrument.sh &lt;directory_containing_java.util_assemblies&gt;"
  - The java.util directory should be &lt;OpenJDK_Image_Name&gt;/jdk/modules/java.base/java/util
- Run jUnit against each of the test classes, making sure to put the Cobertura JAR file on the classpath
  - Reference the Cobertura documentation on using the command line at https://github.com/cobertura/cobertura/wiki/Command-Line-Reference
- After running all of the tests, generate the report by running "sh ./cobertura-report.sh --format html --destination reports/coverage &lt;location_of_java.util_source_code&gt;"
