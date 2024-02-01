package youtube_dl;

public interface YoutubeDownloadEvent {

    void onStart();
    void onProgress(YoutubeDownloadProgressInfo info);
    void onFinish();

    void onError(String error);

}
