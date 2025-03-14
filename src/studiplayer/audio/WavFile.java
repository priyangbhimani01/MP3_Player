package studiplayer.audio;
import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	


	public WavFile() throws NotPlayableException {
		super("default/path/that/exists.wav");
	}
	
	public WavFile(String path) throws NotPlayableException  {
		super(path);
		
		readAndSetDurationFromFile();
		
	}
	
	public void readAndSetDurationFromFile() throws NotPlayableException {
	    try {
	        WavParamReader.readParams(pathname);
	        
	        float frameRate = WavParamReader.getFrameRate();
	        long numberOfFrames = WavParamReader.getNumberOfFrames();
	        
	        duration = computeDuration(numberOfFrames, frameRate);
	    } catch (Exception e) {
	        throw new NotPlayableException(getPathname(), "Error reading parameters", e);
	    }
	}

	
	public String toString() {
		return super.toString() + " - " +timeFormatter(duration);
	}
	
	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		
		return (long) ((numberOfFrames / frameRate) * 1000000);
	}
	

}
