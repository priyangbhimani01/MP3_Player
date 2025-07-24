package studiplayer.ui;
import javafx.stage.FileChooser;
import studiplayer.audio.*;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Player extends Application{
	
	private String pathname;

	
	private static final String PLAYLIST_DIRECTORY = "playlists/";
	public static final String DEFAULT_PLAYLIST =  PLAYLIST_DIRECTORY +"DefaultPlayList.m3u";
	private boolean useCertPlayList = false;
	private PlayList playList;
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<SortCriterion>();
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    private static final String NO_CURRENT_SONG = " - ";
    
    // Attributes for buttons
	
	private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button nextButton;
    private Label playListLabel;
    private Label playTimeLabel;
    private Label currentSongLabel;
    
    private TextField searchTextField;
    private Button filterButton;
    
    // Rest Attributes
    private AudioFile currentSong;
    private boolean isPlaying=true;
    PlayerThread playerThread;
	TimerThread timerThread;
	
	public Player() {
        
    }
	 	

    public void setUseCertPlayList(boolean value) {
        this.useCertPlayList = value;
    }
	
	    
    public void loadPlayList(String pathname) {
    	if (pathname == null || pathname.isEmpty()) {
            playList = new PlayList();
        } else {
            if (!pathname.contains("playlists")) {
                pathname = PLAYLIST_DIRECTORY + pathname;
            }
            playList = new PlayList(pathname);
            
        }
    	
        
    }
    public void setPlayList(String pathname) {
        loadPlayList(pathname);
    }
    
    private Button createButton(String iconfile) {
    	Button button = null;
    	try {
    	URL url = getClass().getResource("/icons/" + iconfile);
    	Image icon = new Image(url.toString());
    	ImageView imageView = new ImageView(icon);
    	imageView.setFitHeight(20);
    	imageView.setFitWidth(20);
    	button = new Button("", imageView);
    	button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    	button.setStyle("-fx-background-color: #fff;");
    	} catch (Exception e) {
    	System.out.println("Image " + "icons/"
    	+ iconfile + " not found!");
    	System.exit(-1);
    	}
    	return button;
    }
    
       
    public void start(Stage newstage) throws Exception{
        
            newstage.setTitle("APA Player");
            
            // Loading userplaylist
            
            if (!useCertPlayList) {
    			
            	File selectedFile;
            	FileChooser filechoose = new FileChooser();
    			filechoose.setTitle("New Files from Playlist");
    			selectedFile = filechoose.showOpenDialog(newstage);
            	if(selectedFile == null) {
            		useCertPlayList = true;
            	}
            }
            if (useCertPlayList) {
            	pathname = DEFAULT_PLAYLIST;
            }
            loadPlayList(pathname);

            BorderPane Pane = new BorderPane();
             // 1. Create top filter form
        
            GridPane Grid = new GridPane();
            Grid.setHgap(10);  
            Grid.setVgap(5);   

            
            Label searchLabel = new Label("Search");
            Label sortLabel = new Label("Sort by");
            Grid.add(searchLabel, 0, 0);  
            Grid.add(sortLabel, 0, 1);  

            
            searchTextField = new TextField();
            Grid.add(searchTextField, 1, 0);  
            
            

            // Add sort choice box
            sortChoiceBox = new ChoiceBox<>();
            sortChoiceBox.getItems().addAll(SortCriterion.ALBUM, SortCriterion.AUTHOR, SortCriterion.DURATION, SortCriterion.TITLE);
            Grid.add(sortChoiceBox, 1, 1);  

            // Add display button
            filterButton = new Button("display");
            Grid.add(filterButton, 2, 1);  

            // Wrap GridPane in TitledPane
            TitledPane filterPane = new TitledPane("Filter", Grid);
            Pane.setTop(filterPane);

            filterButton.setOnAction(e -> {
            	
            	playList.setSortCriterion(sortChoiceBox.getValue());
            	playList.setSearch(searchTextField.getText());
            	
            	
            	SongTable refreshedSongTable = new SongTable(playList);
    	        Pane.setCenter(refreshedSongTable);
            });
            
            

            // 2. Create center SongTable
            SongTable songTable = new SongTable(playList);
            Pane.setCenter(songTable);

            // 3. Create bottom area with current song info and control buttons
            Label currentSongTextwithlabel = new Label("Current Song:");
            currentSongLabel = new Label(NO_CURRENT_SONG);
            Label playTimeTextwithLabel = new Label("Play Time:");
            playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
            Label playlistwithLabel= new Label("Play list:");
            playListLabel = new Label("playlists/"+pathname);
            

            GridPane currentSongInformation = new GridPane();
            currentSongInformation.setHgap(10);
            currentSongInformation.setVgap(5);
            currentSongInformation.add(currentSongTextwithlabel, 0, 0);
            currentSongInformation.add(currentSongLabel, 1, 0);
            currentSongInformation.add(playTimeTextwithLabel, 0, 1);
            currentSongInformation.add(playTimeLabel, 1, 1);
            currentSongInformation.add(playlistwithLabel, 0, 2);
            currentSongInformation.add(playListLabel, 1, 2);
            
            // Creating button with icon
            playButton = createButton("play.jpg");
            pauseButton = createButton("pause.jpg");
            stopButton = createButton("stop.jpg");
            nextButton = createButton("next.jpg");
            
            setButtonStates(false, true, true, false);
            
            // event handler for all button
            
            playButton.setOnAction(e -> {
            	currentSong = playList.currentAudioFile();
            	startThreads(false);
                setButtonStates(true, false, false, false);
            });
            
            pauseButton.setOnAction(e -> {
            	currentSong.togglePause();
            	if (isPlaying) {
                    isPlaying = false;
                } else {
                    isPlaying = true;
                }
            });
            
            stopButton.setOnAction(e -> {
                terminateThreads(false); 
                setButtonStates(false, true, true, false);
                updateSongInfo(null);
            });
            
            nextButton.setOnAction(e -> {
            	stopButton.fire();
            	playList.nextSong();
            	playButton.fire();
            });

            HBox buttonBox = new HBox(playButton, pauseButton, stopButton, nextButton);
            buttonBox.setSpacing(10);
            buttonBox.setAlignment(Pos.CENTER);

            VBox bottomArea = new VBox(currentSongInformation, buttonBox);
            bottomArea.setSpacing(10);
            bottomArea.setPadding(new Insets(10, 10, 10, 10));
            Pane.setBottom(bottomArea);
            
            

            Scene scene = new Scene(Pane, 650, 500);

            newstage.setScene(scene);
            newstage.show();
       
    }
    
    // Set disable method for buttons
    
    private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
        playButton.setDisable(playButtonState);
        pauseButton.setDisable(pauseButtonState);
        stopButton.setDisable(stopButtonState);
        nextButton.setDisable(nextButtonState);
    }
    
    // Updating song Information
    
    private void updateSongInfo(AudioFile af) {
        Platform.runLater(() -> {
            if (af == null) {
                currentSongLabel.setText(NO_CURRENT_SONG);
                playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
            } else {
                currentSongLabel.setText(af.toString());
                playTimeLabel.setText(af.formatPosition());
            }
        });
    }

    
    // Method for Playerthread
    
    public class PlayerThread extends Thread{
    	private boolean stopped;

        public void terminate() {
        	stopped = true;
        	currentSong.stop();
        }

        @Override
        public void run() {
            stopped = false;
            while (!stopped) {
                if (currentSong != null) {
                    
                    try {
						currentSong.play();
						System.out.println("playing: " + currentSong);
					} catch (NotPlayableException e) {
						
						e.printStackTrace();
					}

                    if (!stopped) {
                        playList.nextSong();
                        currentSong = playList.currentAudioFile();
                        updateSongInfo(currentSong);
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    // Method for Timerthread
    
    public class TimerThread extends Thread{
    	private boolean stopped;

        public void terminate() {
            stopped = true;
        }

        @Override
        public void run() {
            stopped = false;

            while (!stopped) {
                if (currentSong != null) {
                    
                	updateSongInfo(currentSong);
                    try {
                        Thread.sleep(1000); // Update the playback time every second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    // Starting thread
    
    private void startThreads(boolean onlyTimer) {
        if (!onlyTimer) {
            playerThread = new PlayerThread();
            playerThread.start();
        }

        timerThread = new TimerThread();
        timerThread.start();
    }
    
    // Terminating thread

    private void terminateThreads(boolean onlyTimer) {
        if (!onlyTimer && playerThread != null) {
            playerThread.terminate();
            playerThread = null;
        }

        if (timerThread != null) {
            timerThread.terminate();
            timerThread = null;
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
