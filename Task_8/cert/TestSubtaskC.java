import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class TestSubtaskC {
	static private List<String> readFile(String filename) {
		List<String> lines = new ArrayList<>();
		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(filename));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isBlank() || line.charAt(0) == '#') {
					continue;
				}
				lines.add(line);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				System.out.println("File " + filename + " read!");
				scanner.close();
			} catch (Exception e) {
				// ignore; probably because file could not be opened
			}
		}

		return lines;
	}

	@Test
	public void testSaveAsM3U() {
		PlayList pl = new PlayList();
		AudioFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		AudioFile tf2 = new TaggedFile("audiofiles/Motiv 5. Symphonie von Beethoven.ogg");
		AudioFile tf3 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");

		pl.getList().add(tf1);
		pl.getList().add(tf2);
		pl.getList().add(tf3);
       
		pl.saveAsM3U("test.m3u");
		
		List<String> lines = readFile("test.m3u");
		assertEquals("m3u file does not contain the correct number of lines!",
				3, lines.size());
		String expected = tf1.getPathname() + "::"
				+ tf2.getPathname() + "::"
				+ tf3.getPathname();
		String actual = lines.get(0) + "::"
				+ lines.get(1) + "::"
				+ lines.get(2);
		System.out.println("expected "+expected);
		System.out.println("actual "+ actual);
		assertEquals("content of m3u file is not correct!", expected, actual);
	}
}
