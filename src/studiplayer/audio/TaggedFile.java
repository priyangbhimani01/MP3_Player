package studiplayer.audio;
import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	

	private String album = "";

	
	public TaggedFile() throws NotPlayableException {
		super("default/path/that/exists.wav");
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		
		readAndStoreTags();
	}
	
	public String getAlbum() {
		
		return album;
	}
	
	
	

	public void readAndStoreTags() throws NotPlayableException {
	    try {
	        Map<String, Object> tagMap = TagReader.readTags(pathname);
	        
	        if (tagMap != null) {
	            if (tagMap.containsKey("title")) {
	                title = ((String) tagMap.get("title")).trim();
	            }
	            
	            if (tagMap.containsKey("author")) {
	                author = ((String) tagMap.get("author")).trim();
	            }
	            
	            if (tagMap.containsKey("album")) {
	                album = ((String) tagMap.get("album")).trim();
	            }
	            
	            if (tagMap.containsKey("duration")) {
	                duration = (long) tagMap.get("duration");
	            }
	        }
	    } catch (Exception e) {
	        throw new NotPlayableException(pathname, "Error reading tags", e);
	    }
	}
	
				
		    
				
		
	
	public String toString() {
		if (album != null && !album.isEmpty() ) {
			
			
			return   super.toString() + " - " + album + " - " + timeFormatter(duration);
		}  else {
			
			return super.toString() + " - " + timeFormatter(duration);
 		}
	}
        
	
	}
	
	
     

