package com.exosoft.Zerfall;

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

	Sound(String path) {
		try {
			File soundFile = new File(path);
		    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

		    // load the sound into memory (a Clip)
		    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(sound);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void play() {
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	boolean isPlaying() {
		return clip.isRunning();
	}
	
	void stop() {
		clip.stop();
	}
}
