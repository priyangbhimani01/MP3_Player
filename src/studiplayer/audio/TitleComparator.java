package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {
   
	@Override
    public int compare(AudioFile file1, AudioFile file2) {
		
        String title1 = file1.getTitle();
        
        String title2 = file2.getTitle();

        if (title2.isEmpty() && title1.isEmpty()) {
        	
              return 0;
        }

        if (title1.isEmpty()) {
            return -1;
        }
        if (title2.isEmpty()) {
              return 1;
        }

        return title1.compareTo(title2);
    }
}