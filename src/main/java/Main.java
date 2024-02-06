import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import youtube_dl.YoutubeDL;
import youtube_dl.YoutubeDownloadEvent;
import youtube_dl.YoutubeDownloadProgressInfo;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Main {


    public static void analyzeAudioFile(String filePath) {
        try {
            File file = new File(filePath);
            AudioFile audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            // Get BPM
            String bpm = tag.getFirst(FieldKey.BPM);
            if (bpm != null) {
                System.out.println("BPM: " + bpm);
            } else {
                System.out.println("BPM: Not found");
            }

            // Get key
            String key = tag.getFirst(FieldKey.KEY);
            if (key != null) {
                System.out.println("Key: " + key);
            } else {
                System.out.println("Key: Not found");
            }

        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "coucou"; // Replace with the actual path to your audio file

        YoutubeDL youtube = new YoutubeDL("https://www.youtube.com/watch?v=neO5XriOz2M", filePath);
        youtube.start(new YoutubeDownloadEvent() {
            @Override
            public void onStart() {
                System.out.println("STARTING");
            }

            @Override
            public void onProgress(YoutubeDownloadProgressInfo info) {
                System.out.println(info.status()+" ... "+info.eta());
            }

            @Override
            public void onFinish() {
                System.out.println("FINISHED");
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        analyzeAudioFile(youtube.getOutputPath()+".opus");
    }

}