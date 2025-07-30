import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class PlayList   {
	
	private LinkedList<AudioFile> audioFiles;
   

	private int current;
	
	
	public PlayList() {
		
		this.audioFiles = new LinkedList<>();
		this.current = 0;
		
	}

	public replcae(){
		system.out.println("Replcaing song");
	}
	
	public PlayList (String m3uPathname)     {
		this.audioFiles = new LinkedList<>();
		
		this.loadFromM3U(m3uPathname);
		
	}
	
	public void add (AudioFile file) {
		audioFiles.add(file);
		
	}
	
	public void remove (AudioFile file) {
		audioFiles.remove(file);
	}
	
	public int size () {
		return audioFiles.size();
	}
	
	public AudioFile currentAudioFile () {
		if (audioFiles.isEmpty() || current < 0 || current >= audioFiles.size()) {
			return null;
		}
		else {
			return audioFiles.get(current);
		}
	}
	
	public void nextSong() {
		if (audioFiles.isEmpty() || current < 0 || current >= audioFiles.size()) {
			current = 0;
	    } else {
	    	
	    	current++;
	    	
	    	if (current >= audioFiles.size()) {
	    		current = 0;
	    	}
	    }
	}
	
	
	public void loadFromM3U (String path)   {
		Scanner scanner = null;
		current = 0;
		

		 audioFiles = new LinkedList<AudioFile> ();

		try {
			
			
			scanner = new Scanner(new File(path));			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				if(line.startsWith("#") || line.isBlank()) {
					
				}else 
				{
					if (line.endsWith(".wav")){
		
						audioFiles.add(new WavFile(line));
					}else {
						audioFiles.add(new TaggedFile(line));
					}
				}
				
			}
		}
	
          catch (Exception e) {
			
        	  throw new RuntimeException(e);
		} 
		
		finally {
			try {
				
				scanner.close();	
			} catch (Exception e) {
			}
		}
		
	}

	public void saveAsM3U(String pathname)  {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		try {
			
			writer = new FileWriter(pathname);
			for (AudioFile audiofile : audioFiles) {
				writer.write(audiofile.getPathname() + sep );
			}
    }
		catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
      }
		finally {
			try {
				
				writer.close();
			} catch (Exception e) {
			}
		}
}
		
	
	
	public List<AudioFile> getList(){
		 return audioFiles;
	}
	
	
	
	public void setSearch (String value) {
		
	}
	
	public int getCurrent() {
		return current;
	}
	
	public void setCurrent (int current) {
		this.current = current;
	}
	
	public void jumpToAudioFile (AudioFile file) {
		
	}
	
	
}