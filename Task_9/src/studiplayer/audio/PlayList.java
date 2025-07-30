package studiplayer.audio;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class PlayList implements Iterable<AudioFile>  {
	
	private LinkedList<AudioFile> audioFiles;
    

	private ControllablePlayListIterator iterator;
	private String search;
	private SortCriterion sortcriterion =  SortCriterion.DEFAULT;
	private AudioFile currentFile;

	
	
	public PlayList() {
		
		this.audioFiles = new LinkedList<>();
		
		this.search = "";
		
		InitIterator();
		
	}
	public void InitIterator() {
		this.iterator = new ControllablePlayListIterator(audioFiles, search, sortcriterion);
		iterator.setCurrentIndex(0);
	}
	public PlayList (String m3uPathname)     {
		this.audioFiles = new LinkedList<>();
		
		this.loadFromM3U(m3uPathname);
		
	}
	
	public void setSearch(String search) {
		this.search=search;
		InitIterator();		
	}
	
	public String getSearch() {
		return search;
		
	}
	
	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortcriterion = sortCriterion;
        this.iterator.setSortCriterion(sortCriterion);
        
        iterator.setCurrentIndex(0);
    }
	
	public SortCriterion getSortCriterion() {
		return this.sortcriterion;
        
    }
	
	public void add (AudioFile audiofile) {
		audioFiles.add(audiofile);
		InitIterator();	
	}
	
	public void remove (AudioFile audiofile) {
		audioFiles.remove(audiofile);
		resetIterator();
	}
	
	public int size () {
		return audioFiles.size();
	}
	
	public replace(){
		System.out.println("replace songs");
	}
	
	public AudioFile currentAudioFile () {
		return this.iterator.receiveCurrentAudioFle();
     
    }
	
	
	public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(audioFiles, search, sortcriterion);
    }
	
	public void nextSong() {
		System.out.println("has next " + iterator.hasNext());
		System.out.println("is empty "+ iterator.IsEmpty());
        if (!iterator.hasNext() || iterator.IsEmpty()) {
        	
        	System.out.println("reset iterator called");
            resetIterator();
            
        } else {
        	
            currentFile = iterator.next();
        }
    }

	
	
	public void loadFromM3U (String path)  {
		Scanner scanner = null;
		
		

		 audioFiles = new LinkedList<AudioFile> ();

		try {
			
			
			scanner = new Scanner(new File(path));			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				if(line.startsWith("#") || line.isBlank()) {
					
				}else 
				{ try {
					if (line.endsWith(".wav")){
		
						audioFiles.add(new WavFile(line));
					}else {
						audioFiles.add(new TaggedFile(line));
					} 
				}catch (NotPlayableException e) {
                        e.printStackTrace();
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

	@Override
	public String toString() {
		return audioFiles.toString();
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
	
	
	private void resetIterator() {
        this.iterator = new ControllablePlayListIterator(audioFiles, search, sortcriterion);
        this.currentFile = this.iterator.receiveCurrentAudioFle();  
        }
	
	
	public AudioFile jumpToAudioFile (AudioFile file) {
		AudioFile result = iterator.jumpToAudioFile(file);
        if (result != null) {
        	
            currentFile = result;
        }
        return currentFile;
	}
	
	
	
}