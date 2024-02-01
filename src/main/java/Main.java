import youtube_dl.YoutubeDL;
import youtube_dl.YoutubeDownloadEvent;
import youtube_dl.YoutubeDownloadProgressInfo;

import java.io.*;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        String videoUrl = "https://www.youtube.com/watch?v=6mR2LnwbdlI";
        String savePath = "test1111.mp3";

        new YoutubeDL(videoUrl, savePath).start(new YoutubeDownloadEvent() {
            @Override
            public void onStart() {
                System.out.println("Démarrage du téléchargement !");
            }

            @Override
            public void onProgress(YoutubeDownloadProgressInfo info) {
                System.out.println(info.percentage() + " ..... "+info.status()+" ..... "+info.eta());
            }

            @Override
            public void onFinish() {
                System.out.println("Terminé !");
            }

            @Override
            public void onError(String error) {
                System.out.println("Erreur : "+error);
            }
        });
    }

}