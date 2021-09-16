import processing.core.PApplet;
import ddf.minim.*;
import ddf.minim.analysis.FFT;
public class MusicVisualizer extends PApplet {
    private Minim minim;
    private AudioPlayer audio;
    private int yCenter;
    private int xCenter;
    private FFT fft;
    private final int blue = color(256, 100, 200);
    private final int green = color(60, 200, 80);
    public static void main(String[] args) {
        PApplet.main("MusicVisualizer");
    }
    public void settings() {
        size(1000, 500);
    }
    public void setup() {
        background(40,40,40);
        yCenter = height / 2;
        xCenter = width / 2;
        // minim is a library that easily helps us link processing, audio, and visuals.=
        minim = new Minim(this);
        audio = minim.loadFile("resources/snakeskin.mp3");
        audio.setGain(-10);
        // variable that looks at every frequency value
        fft = new FFT(audio.bufferSize(), audio.sampleRate());

        // voice will loop continuously
        audio.loop();

    }
    public void draw() {
        background(40,40,40);
        //turns channels into arrays of numbers
        float[] leftChannel = audio.left.toArray();
        float[] rightChannel = audio.right.toArray();

        fft.forward(audio.mix);
        // draws line for each part of every second of song
        for (int i = 0; i < leftChannel.length - 1; i++) {
            drawChannel(leftChannel, i,-1,blue);
            drawChannel(rightChannel, i, 1,green);
        }
        // draws circles for each part of every second of song
        for (int i = 0; i < fft.specSize(); i++) {
            drawFrequency(i);
        }
    }
    private void drawChannel(float[] channel,int index,int direction,int color){
        // multiple lines are drawn
        strokeWeight(6);
        for (int i = 1; i <= 5; i++) {
            stroke(color, (float) 100 / sq(i));
            line(index, yCenter + (direction * abs(channel[index] * 150)), index + 1, yCenter + (direction * abs(channel[index + 1] * 150)));
        }
    }
    private void drawFrequency(int index){
        // no stroke
        stroke(200,40,60,50);
        for (int i = 1; i <3; i++){
            //draw circles that get increasingly larger and more transparent
            fill(200,40,60,(float)200/sq(i));
            circle(xCenter, yCenter, fft.getBand(index) * 3*sq(i));
        }



    }
}
