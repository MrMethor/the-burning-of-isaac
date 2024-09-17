import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound{
    Long currentFrame;
    Clip clip;
    AudioInputStream audioInputStream;
    private String filePath;

    public Sound(String filePath, boolean loop){
        this.filePath = filePath;
        try{
            this.audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
            this.clip = AudioSystem.getClip();
            this.clip.open(this.audioInputStream);
            this.clip.start();
            if(loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch(UnsupportedAudioFileException e){}
        catch(IOException e){}
        catch(LineUnavailableException e){}
    }

    public void stop(){
        currentFrame = 0L;
        this.clip.stop();
        this.clip.close();
    }

    public void pause(){
        this.currentFrame = this.clip.getMicrosecondPosition();
        this.clip.stop();
    }

    public void resume(){
        try{
            this.clip.close();
            this.audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
            this.clip.open(this.audioInputStream);
            this.clip.loop(Clip.LOOP_CONTINUOUSLY);
            this.clip.setMicrosecondPosition(this.currentFrame);
        }
        catch(UnsupportedAudioFileException e){}
        catch(IOException e){}
        catch(LineUnavailableException e){}
    }

    public void changeFile(String filePath, boolean sameFrame){
        pause();
        this.filePath = filePath;
        try{
            this.audioInputStream = AudioSystem.getAudioInputStream(new File(this.filePath).getAbsoluteFile());
            this.clip = AudioSystem.getClip();
            this.clip.open(this.audioInputStream);
        }
        catch(UnsupportedAudioFileException e){}
        catch(IOException e){}
        catch(LineUnavailableException e){}
        if(!sameFrame)
            this.currentFrame = 0L;
        resume();
    }
}