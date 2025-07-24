import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	


	public WavFile() {}
	
	public WavFile(String path) {
		super(path);
		
		readAndSetDurationFromFile();
		
	}
	
	public void readAndSetDurationFromFile() {
		WavParamReader.readParams(pathname);
		
		float frameRate = WavParamReader.getFrameRate();
        long numberOfFrames = WavParamReader.getNumberOfFrames();
        
        duration = computeDuration(numberOfFrames,frameRate);
        
	}
	
	public String toString() {
		return super.toString() + " - " +timeFormatter(duration);
	}
	
	
	static long computeDuration(long numberOfFrames, float frameRate) {
		
		return (long) ((numberOfFrames / frameRate) * 1000000);
	}
	

}
