package studiplayer.cert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import studiplayer.audio.AlbumComparator;
import studiplayer.audio.AudioFile;
import studiplayer.audio.AudioFileFactory;
import studiplayer.audio.AuthorComparator;
import studiplayer.audio.DurationComparator;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.TitleComparator;

public class ComparatorTest {

    /**
     * Methode testet, ob die Comparator-Methoden richtige Rueckgabewerte
     * produzieren, auch wenn die Argumente ungleichen Typ haben
     */
    @Test
    public void testRetValsOfComparators() {
        AudioFile mp3 = null, wav = null, midi1 = null, midi2 = null;
        // Ein Pseudo-Audiofile erzeugen, das nicht vom Typ SampledFile ist
        // Das folgende Konstrukt ist eine innere Klasse.
        class MidiFile extends AudioFile {
            MidiFile(String path) throws NotPlayableException {
                super(path);
            }

            public String formatDuration() {
                return null;
            }

            public String formatPosition() {
                return null;
            }

            public void play() throws NotPlayableException {
            }

            public void stop() {
            }

            public void togglePause() {
            }

            public String[] fields() {
                return null;
            }
        }
        
        // Wir bauen uns mehrere AudioFile Objekte. Darunter auch Objekte der
        // hier eigens abgeleiteten Klasse MidiFile.
        try {
            mp3 = AudioFileFactory.createAudioFile("audiofiles/Rock 812.mp3");
            wav = AudioFileFactory.createAudioFile(
            		"audiofiles/wellenmeister - tranquility.wav");
            midi1 = new MidiFile("audiofiles/kein.wav.sondern.ogg");
            midi2 = new MidiFile("audiofiles/Rock 812.mp3");
        } catch (NotPlayableException e) {
            fail(e.getMessage());
        }

        AuthorComparator ic = new AuthorComparator();
        // lexikographisch: Eisbach < wellenmeister
        assertTrue("Sortierung nach Interpret falsch", ic.compare(mp3, wav) < 0);
        
        // lexikographisch: wellenmeister > Eisbach
        assertTrue("Sortierung nach Interpret falsch", ic.compare(wav, mp3) > 0);

        TitleComparator tc = new TitleComparator();
        // lexikographisch: Rock < tranquility
        assertTrue("Sortierung nach Titel falsch", tc.compare(mp3, wav) < 0);
        
        // lexikographisch: tranquility > Rock
        assertTrue("Sortierung nach Titel falsch", tc.compare(wav, mp3) > 0);

        DurationComparator dc = new DurationComparator();
        // 05:31 > 02:21 , entsprechendes gilt für die Dauer in Mikrosekunden
        assertTrue("Sortierung nach Dauer falsch", dc.compare(mp3, wav) > 0);

        // 02:21 < 05:31 , entsprechendes gilt für die Dauer in Mikrosekunden
        assertTrue("Sortierung nach Dauer falsch", dc.compare(wav, mp3) < 0);

        // Fuer den Vergleich mit einem Midi-File ist entscheidend, in welcher
        // Klasse das Attribut für die Dauer aufgehängt ist. Falls das Attribut
        // in AudioFile definiert ist, kann man midi1 und mp3 dem Wert nach vergleichen.
        // Ansonsten muss das midi1 Objekt das kleinere sein, da es dann den falschen
        // (fremden) Typ hat.
        // Die Implementierungen können sich hier unterscheiden, und daher
        // kein Test der Ordnungsbeziehung zwischen midi1 und mp3

        AlbumComparator ac = new AlbumComparator();

        // Identität, also gleich
        assertEquals("Sortierung nach Album falsch", 0, ac.compare(mp3, mp3));

        // Ein WavFile hat sicher kein Attribut fuer Album, das haben nur TaggedFiles.
        // Implementierungen, die ein entsprechendes Attribut in SampledFile oder
        // gar AudioFile implementieren, sind definitiv falsch!
        // Daher muss das wav file kleiner sein als das mp3 file
        assertEquals("Sortierung nach Album falsch", 1, ac.compare(mp3, wav));
        assertEquals("Sortierung nach Album falsch", -1, ac.compare(wav, mp3));

        // Das im Test von AudioFile abgeleitete MidiFile hat kein Attribut
        // fuer Album.
        // Implementierungen, die ein entsprechendes Attribut in AudioFile
        // implementieren, sind definitiv falsch! Ein solches Attribut leasst sich
        // in der Klasse AudioFile nicht rechtfertigen.
        // 
        // Daher muss das MidiFile midi1 kleiner sein als das mp3 file        
        assertEquals("Sortierung nach Album fuer Nicht-TaggedFile falsch", 1,
                ac.compare(mp3, midi1));
        assertEquals("Sortierung nach Album fuer Nicht-TaggedFile falsch", -1,
                ac.compare(midi1, mp3));

        // Fuer den AlbumComparator sind beide MidiFiles vom falschen Typ.
        // Fuer ihn sind sie also gleich (siehe Spezifikation)
        assertEquals("Sortierung nach Album fuer Nicht-TaggedFile falsch", 0,
                ac.compare(midi1, midi2));
       
    }
}
