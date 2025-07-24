package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
	
    @Override
    public int compare(AudioFile file1, AudioFile file2) {
    	
        long duration1 = getDuration(file1);
        
        long duration2 = getDuration(file2);

      return (int) (duration1 - duration2);
    }

    private long getDuration(AudioFile file) {
        if (file instanceof SampledFile) {
        	
            return ((SampledFile) file).getDuration();
        } else {
            return 0; 
        }
    }
}