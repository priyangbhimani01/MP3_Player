import studiplayer.basic.BasicPlayer;

abstract class SampledFile extends AudioFile  {
	

	protected long duration;
	protected long position;
	
	
    public SampledFile() {
    	
    }
    
    public SampledFile(String path) {
    	super(path);
    	
    }
    
    public void play() {
    	BasicPlayer.play(this.pathname);
    	
    }
    
    public void togglePause() {
    	BasicPlayer.togglePause();
    }
    
    public void stop() {
    	BasicPlayer.stop();
    	
    }
    
    public String formatDuration() {
    	return timeFormatter(getDuration());
    }
    
    public String formatPosition() {
    	long position = BasicPlayer.getPosition();
    	return timeFormatter(position);
    }
    
    
    
    static String timeFormatter(long timeInMicroSeconds) {
        if (timeInMicroSeconds<0)    {
            throw new RuntimeException("Invalid time : " + timeInMicroSeconds);
      

        }
         long totalSeconds;
        if    (timeInMicroSeconds < Long.MAX_VALUE /1000000L)   {
          totalSeconds = timeInMicroSeconds / 1000000L;
            
  }     else {
               throw new RuntimeException("time value is very large: " + timeInMicroSeconds);
        }

        
        if (totalSeconds >= 100*60)   {
            throw new RuntimeException("Time value is exceeds maximum value: " + totalSeconds + " seconds");
        }

      
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d", minutes, seconds);
        
        
    }

    
    
    public long getDuration() {
    	return duration;
    }
    
}
