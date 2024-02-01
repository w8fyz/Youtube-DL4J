package youtube_dl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeDL {

    private String url;
    private String outputName;

    public YoutubeDL(String url, String outputName) {
        this.url = url;
        this.outputName = outputName;
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
            return new YoutubeDownloadProgressInfo("100%", "00:00", "Converssion du format vers MP3");
        }
        return null;
    }

    public void start(YoutubeDownloadEvent event) {
        try {
            String[] command = {"youtube-dl", "-f", "bestaudio", "--extract-audio", "--audio-format", "mp3", "-o",
                "/Users/thibeau/Downloads/ytdl" + "/"+outputName+".mp3", url};
            Process process = Runtime.getRuntime().exec(command);
            event.onStart();
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                YoutubeDownloadProgressInfo info = getInfoFromLine(line);
                if(info != null) {
                    event.onProgress(info);
                }

            }
            process.waitFor();
            event.onFinish();
    } catch (Exception e) {
        e.printStackTrace();
        event.onError(e.getMessage());
    }
    }

}
