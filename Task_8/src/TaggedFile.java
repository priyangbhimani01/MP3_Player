import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	

	private String album = "";

	
	public TaggedFile() {}
	
	public TaggedFile(String path) {
		super(path);
		readAndStoreTags();
	}
	
	public String getAlbum() {
		
		return album;
	}
	
	

	public void readAndStoreTags() {
				
				Map<String, Object> tagMap = TagReader.readTags(pathname);
			if(tagMap != null) {
				if(tagMap.containsKey("title")) {
					title = ((String) tagMap.get("title")).trim();
				}
				
				 if(tagMap.containsKey("author")) {
					  author = ((String) tagMap.get("author")).trim();
				}
				
				 if(tagMap.containsKey("album")) {
					 album = ((String) tagMap.get("album")).trim();
				}
				
				 if(tagMap.containsKey("duration")) {
				     duration = (long) tagMap.get("duration");
				}
				
				
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
	
	
     

