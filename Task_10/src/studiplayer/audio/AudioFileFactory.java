package studiplayer.audio;

public class AudioFileFactory {
    public static AudioFile createAudioFile(String path) throws NotPlayableException {
        int dotPosition = path.lastIndexOf('.');
        
        if (dotPosition == -1 || dotPosition == path.length() - 1) {
            throw new NotPlayableException(path, "Invalid file path: " + path);
        }
        
        String extension = path.substring(dotPosition + 1).toLowerCase();

        if (extension.equals("wav")) {
            return new WavFile(path);
        } else if (extension.equals("ogg") || extension.equals("mp3")) {
            return new TaggedFile(path);
        } else {
            throw new NotPlayableException(path, "Unknown suffix for AudioFile \"" + path + "\"");
        }
    }
}



