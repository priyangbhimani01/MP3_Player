
public class Utils {
	private static String[] originalOsSettings = {
			System.getProperty("file.separator"),
			System.getProperty("os.name")
	};
	
	public static char emulateWindows() {
		String[] settings = { "\\", "Windows" };
		return emulateOS(settings);
	}
	
	public static char emulateLinux() {
		String[] settings = { "/", "Linux" };
		return emulateOS(settings);
	}
	
	private static char emulateOS(String[] osSettings) {
		System.setProperty("file.separator", osSettings[0]);
		System.setProperty("os.name", osSettings[1]);		
		return osSettings[0].charAt(0);
	}
	
	public static void resetEmulation() {
		System.setProperty("file.separator", originalOsSettings[0]);
		System.setProperty("os.name", originalOsSettings[1]);		
	}
	
	public static boolean isWindows() {
        String osname = System.getProperty("os.name");
		return osname.toLowerCase().indexOf("win") >= 0; 
	}
}
