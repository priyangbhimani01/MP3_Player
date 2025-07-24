package studiplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SampledFile;
import studiplayer.audio.TaggedFile;

public class SongTable extends TableView<Song> {
	private ObservableList<Song> tableData;
	private PlayList playList;
	
	/**
	 * Intialisiert die Tabelle mit den Daten aus der PlayList und setzt Tabellen-Header
	 * @param playList
	 */
	public SongTable(PlayList playList) {
		this.playList = playList;
		this.tableData = FXCollections.observableArrayList();
		setItems(tableData);
		
        TableColumn<Song, String> interpretColumn = new TableColumn<>("Artist");
        interpretColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("interpret"));
        interpretColumn.setSortable(false);
		TableColumn<Song, String> titelColumn = new TableColumn<>("Title");
		titelColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("titel"));
        titelColumn.setSortable(false);
		TableColumn<Song, String> albumColumn = new TableColumn<>("Album");
		albumColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("album"));
		albumColumn.setSortable(false);
		TableColumn<Song, String> laengeColumn = new TableColumn<>("Duration");
		laengeColumn.setCellValueFactory(
				new PropertyValueFactory<Song, String>("laenge"));
		laengeColumn.setSortable(false);
		getColumns().add(interpretColumn);
		getColumns().add(titelColumn);
		getColumns().add(albumColumn);
		getColumns().add(laengeColumn);
        setEditable(false);
        refreshSongs();
	}

	/**
	 * Registert eine Ereignisbehandlung für den Fall, dass eine Zeile mit der
	 * Maus angeklickt wird.
	 * @param handler
	 */
	public void setRowSelectionHandler(EventHandler<? super MouseEvent> handler) {
        setOnMouseClicked(handler);		
	}
	
	/**
	 * Zeigt die Tabelle neu an nach Änderungen (Einträge, Konfiguration) an der PlayListe 
	 */
	public void refreshSongs() {
		tableData.clear();
		for (AudioFile af : playList) {
			String album = "";
			String laenge = "";
			
			if (af instanceof TaggedFile) {
				album = ((TaggedFile) af).getAlbum();
			}
			if (af instanceof SampledFile) {
				SampledFile sf = (SampledFile) af;
				laenge = sf.formatDuration();
			}
			
			tableData.add(new Song(af, af.getAuthor(), af.getTitle(), album, laenge));
		}		
	}

	/**
	 * Selektiert den Song "song" in der Tabelle
	 * @param song
	 */
	public void selectSong(AudioFile song) {
		AudioFile currentAudioFile = playList.currentAudioFile();
		int index = 0;
		
		for (Song s : tableData) {
			if (s.getAudioFile() == currentAudioFile) {
				getSelectionModel().select(index);
			}
			++index;
		}
	}
}
