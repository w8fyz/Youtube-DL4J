package youtube_dl;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDL {

    private String url;
    private String outputName;

    public YoutubeDL(String url, String outputName) {
        this.url = url;
        this.outputName = outputName;
    }

    public String getOutputPath() {
        return "/Users/thibeau/Downloads/ytdl" + "/"+outputName;
    }

    private YoutubeDownloadProgressInfo getInfoFromLine(String line) {
        if(line.startsWith("[download]")) {
            final String regex = "\\[download\\]\\s+(\\d+\\.\\d+)%.*ETA (\\d+:\\d+)";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                if(matcher.groupCount() >= 2) {
                    return new YoutubeDownloadProgressInfo(matcher.group(1)+"%", matcher.group(2), "Téléchargement en cours");
                }
            }
        } else if(line.startsWith("[ffmpeg]")) {
            return new YoutubeDownloadProgressInfo("100%", "00:00", "Converssion du format vers WAV");
        }
        return null;
    }

    private void executeProcess(String[] command, YoutubeDownloadEvent event) throws IOException, InterruptedException {
        System.out.println("New process started");
        ProcessBuilder process = new ProcessBuilder(command);
        Process processObject = process.start();
        InputStream inputStream = processObject.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        process.redirectError(ProcessBuilder.Redirect.INHERIT);
        process.redirectInput(ProcessBuilder.Redirect.INHERIT);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            YoutubeDownloadProgressInfo info = getInfoFromLine(line);
            if(info != null) {
                event.onProgress(info);
            }
        }
        processObject.waitFor();
    }

    public void start(YoutubeDownloadEvent event) {
        event.onStart();
        try {
            String[] command = {"youtube-dl", "-f", "bestaudio", "--extract-audio", "-o",
                    getOutputPath()+".opus", url};
            executeProcess(command, event);

            String[] command2 = {"ffmpeg", "-i", getOutputPath()+".opus", "-c:a", "pcm_f32le" ,getOutputPath()+".wav"};
            executeProcess(command2, event);

            event.onFinish();
    } catch (Exception e) {
        e.printStackTrace();
        event.onError(e.getMessage());
    }
    }

}
