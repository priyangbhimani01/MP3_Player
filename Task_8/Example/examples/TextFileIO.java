import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Demonstrates how text files can be read using the Java API.
 */
public class TextFileIO {

	/**
	 * Creates a textfile from "path" and saves the strings given in "lines"
	 * to the file, one string per line. 
	 * @param path
	 * @param lines
	 */
	static private void writeFile(String path, String[] lines) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		
		try {
			// create the file if it does not exist, otherwise reset the file
			// and open it for writing
			writer = new FileWriter(path);
			for (String line : lines) {
				// write the current line + newline char
				writer.write(line + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + path + "!");
		} finally {
			try {
				System.out.println("File " + path + " written!");
				// close the file writing back all buffers
				writer.close();
			} catch (Exception e) {
				// ignore exception; probably because file could not be opened
			}
		}
	}
	
	/**
	 * Reads the text file denoted by "path" and returns a list of strings (one per line)
	 * @param path
	 */
	static private List<String> readFile(String path) {
		List<String> lines = new ArrayList<>();
		Scanner scanner = null;
		
		try {
			// open the file for reading
			scanner = new Scanner(new File(path));
			int lineNo = 1;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// create line with line number prefix
				String lineWithNumber = String.format("%03d: %s", lineNo++, line);
				// add enhanced line to result list
				lines.add(lineWithNumber);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				System.out.println("File " + path + " read!");
				scanner.close();	
			} catch (Exception e) {
				// ignore; probably because file could not be opened
			}
		}
		
		return lines;
	}

	/**
	 * Writes some text to a file, reads the file again and outputs the lines read to the console
	 * to help the user verify that everything works.
	 */
	public static void main(String[] args) {
		String[] lines = {
				"# Eine Kommentarzeile",
				"erste Zeile",
				"zweite Zeile",
				"######################"
		};
		writeFile("test.txt", lines);
		List<String> linesRead = readFile("test.txt");
		for (String line : linesRead) {
			System.out.println(line);
		}
	}
}
