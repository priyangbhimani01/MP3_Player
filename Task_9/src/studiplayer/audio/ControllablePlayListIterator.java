package studiplayer.audio;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class ControllablePlayListIterator implements Iterator<AudioFile>{
	private List<AudioFile> audioFiles;
	private int currentposition=0;
	private List<AudioFile> filteredAndSortedFiles;
	private SortCriterion sortcriterion =  SortCriterion.DEFAULT;

	public ControllablePlayListIterator(List<AudioFile> audioFiles) {

		this.audioFiles=audioFiles;
		this.filteredAndSortedFiles = new ArrayList<AudioFile>(audioFiles);
	}
	
	public ControllablePlayListIterator(List<AudioFile> audioFiles, String search, SortCriterion sortCriterion) {
		this.audioFiles=audioFiles;
		  setCurrentIndex(0);
        if (search == null || search.isEmpty()) {
        	
            filteredAndSortedFiles = new ArrayList<>(audioFiles);
        } 
        else {
            filteredAndSortedFiles = new ArrayList<>();
            
            for (AudioFile file : audioFiles) {
                if (file.getAuthor().contains(search) || file.getTitle().contains(search) || file.getAlbum().contains(search) ) {
                    filteredAndSortedFiles.add(file);
                }
            }
        }

        this.setSortCriterion(sortCriterion);
        
    }
	public SortCriterion getSortCriterion() {
        return sortcriterion;
        
    }
	
	
	
	public void setSortCriterion(SortCriterion sortCriterion) {
		
		if (sortCriterion == SortCriterion.TITLE) {
			filteredAndSortedFiles.sort(new TitleComparator());
		}
		
		 else if ( sortCriterion == SortCriterion.AUTHOR) {
			
		      filteredAndSortedFiles.sort(new AuthorComparator());
		    
		} else if (sortCriterion == SortCriterion.ALBUM) {
			
		    filteredAndSortedFiles.sort(new AlbumComparator());
		    
		} else if ( sortCriterion == SortCriterion.DURATION) {
			
		    filteredAndSortedFiles.sort(new DurationComparator());
		}
		
		
		this.audioFiles = filteredAndSortedFiles;
	}
	
	public boolean IsEmpty() {
		return audioFiles.isEmpty();
	}
	public AudioFile receiveCurrentAudioFle() {
		
		if (audioFiles. isEmpty()) {
			return null;
		}
		  if ( getCurrentIndex() >=  audioFiles.size()) {
			setCurrentIndex(0);
			
		}
		
		
		return audioFiles.get(getCurrentIndex());
		
	}
	
	public void setCurrentIndex(int currentposition) {
		this.currentposition = currentposition;
	}

	
	public int getCurrentIndex() {
		return currentposition;
	}
	
	
	public boolean hasNext() {
		
		return currentposition < audioFiles.size();
	}
	
	public AudioFile next() {
		 
		
	     AudioFile song = audioFiles.get(getCurrentIndex());
	    
		if (getCurrentIndex() < audioFiles.size()) {
			
			  setCurrentIndex  (  getCurrentIndex() + 1);

		}else  {
			setCurrentIndex(0);
		}
		
		return song;

	}
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		
       int Jump = this.audioFiles.indexOf(file); 


      int difference = Jump - getCurrentIndex();
		
			
			if(difference < 0) {
				
				setCurrentIndex(0);
				int i = 0;
				while (i <= Jump) {
				    this.next();
				    i++;
				}
				
				
			}else if(difference > 0) {
				
				int i = 1;
				while (i <= difference) {
				    this.next();
				    i++;
				}
				
			}else  {
				setCurrentIndex(Jump + 1);
			}
			
			return this.audioFiles.get(Jump); 
		
		
	  }
}


