package exosoft.Zerfall;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private Clip clip;
	DataLine.Info info;
	AudioInputStream sound;
	File soundFile;
	Sound(String path) {
		try {
			soundFile = new File(path);
			sound = AudioSystem.getAudioInputStream(soundFile);
			info = new DataLine.Info(Clip.class, sound.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(sound);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	void play() {
		clip.stop();
		clip.setMicrosecondPosition(0);
		clip.start();
	}

	boolean isPlaying() {
		return clip.isRunning();
	}

	void pause() {
		clip.stop();
	}

	void stop() {
		clip.stop();
		clip.setMicrosecondPosition(0);
	}

	long msPosition() {
		return clip.getMicrosecondPosition();
	}

	double positionRatio() {
		return (clip.getMicrosecondPosition() / clip.getMicrosecondLength());
	}
}