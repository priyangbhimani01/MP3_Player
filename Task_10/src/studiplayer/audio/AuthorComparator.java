


package studiplayer.audio;
import java.util.Comparator;



public class AuthorComparator implements Comparator<AudioFile> {

    
    public int compare(AudioFile o1, AudioFile o2) {
    	    	
        
        String author2 = o2.getAuthor();
        
         String author1 = o1.getAuthor();

        if (author2 == null) {
        	
            author2 = "";
        }
        if (author1 == null) {
        	
            author1 = "";
        }

        if (author1.isEmpty() && author2.isEmpty()) {
            return 0;
        }

        if (author1.isEmpty()) {
            return -1;
        }
        if (author2.isEmpty()) {
            return 1;
        }

        return author1.compareTo(author2);
    }
}