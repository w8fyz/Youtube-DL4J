import ddf.minim.AudioInput;
import ddf.minim.AudioListener;
import ddf.minim.AudioSource;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;

public class BeatListener implements AudioListener {

    private AudioSource source;

    BeatListener(AudioSource source) {
        this.source = source;
        this.source.addListener(this);
    }
    @Override
    public void samples(float[] floats) {
            System.out.println("Draw ?");
    }

    @Override
    public void samples(float[] floats, float[] floats1) {
        System.out.println("Draw ?");
    }
}
