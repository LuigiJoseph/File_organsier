import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FileOrganizer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the path for the folder you want to organize: ");
        String sourceFolder = scanner.nextLine();

        System.out.println("Enter how many characters to use for the prefix: ");
        int prefixLength = scanner.nextInt();
        
        // Input parameters
        // String sourceFolder = "path_to_your_folder"; // Replace with your folder path
        // int prefixLength = 5; // Number of characters to consider as the prefix

        organizeFilesByPrefix(sourceFolder, prefixLength);

        scanner.close();
    }

    public static void organizeFilesByPrefix(String sourceFolder, int prefixLength) {
        File folder = new File(sourceFolder);

        // Check if source folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("The provided path is not a valid folder.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No files found in the folder.");
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                try {
                    // Get the prefix (first `prefixLength` characters of the file name)
                    String fileName = file.getName();
                    if (fileName.length() < prefixLength) {
                        System.out.println("Skipping file: " + fileName + " (name is shorter than prefix length)");
                        continue;
                    }
                    String prefix = fileName.substring(0, prefixLength);

                    // Create a new folder based on the prefix
                    File prefixFolder = new File(folder, prefix);
                    if (!prefixFolder.exists()) {
                        boolean created = prefixFolder.mkdir();
                        if (!created) {
                            System.out.println("Failed to create folder: " + prefixFolder.getAbsolutePath());
                            continue;
                        }
                    }

                    // Move the file to the new folder
                    Path targetPath = Path.of(prefixFolder.getAbsolutePath(), fileName);
                    Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Moved file: " + fileName + " -> " + prefixFolder.getAbsolutePath());
                } catch (IOException e) {
                    System.err.println("Error processing file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("File organization complete.");
    }
}
