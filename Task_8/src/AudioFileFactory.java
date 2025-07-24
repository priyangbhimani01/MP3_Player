public class AudioFileFactory {
    public static AudioFile createAudioFile(String path) {
    	
    	
      int dotPosition = path.lastIndexOf('.');
        
        if (dotPosition == -1 || dotPosition == path.length() - 1) {
        	
            throw new IllegalArgumentException("Invalid file path: " + path);
       }
        
        String extension = path.substring(dotPosition + 1).toLowerCase();

        if (extension.equals("wav")) {
            return new WavFile(path);
      } else if (extension.equals("ogg")) {
            return new TaggedFile(path);
            
          } else if (extension.equals("mp3")) {
            return new TaggedFile(path);
            
       } else {
        	throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
        }
    }
}


