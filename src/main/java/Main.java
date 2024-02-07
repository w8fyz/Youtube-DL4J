import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import youtube_dl.YoutubeDL;
import youtube_dl.YoutubeDownloadEvent;
import youtube_dl.YoutubeDownloadProgressInfo;

public class Main {


    public static float calculateBPM(String filePath) {
        Minim minim = new Minim(new Object());
        minim.
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