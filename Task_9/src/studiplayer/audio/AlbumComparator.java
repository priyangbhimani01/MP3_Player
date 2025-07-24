package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    
    public int compare(AudioFile file1, AudioFile file2) {
        String album1 = getAlbum(file1);
        
          String album2 = getAlbum(file2);

        if (album1 == null && album2 != null) {
              return -1;
        }
        if (album1 != null && album2 == null) {
             return 1;
        } 
        if (album1 == null && album2 == null ) {
        	    return 0;
        }

        
        
        return album1.compareTo(album2);
    }

    private String getAlbum(AudioFile file) {
        if (file instanceof TaggedFile) {
            
        	return ((TaggedFile) file).getAlbum();
        } else {
           
        	return null; 
        }
    }
}