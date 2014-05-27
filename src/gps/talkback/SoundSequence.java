package gps.talkback;

import java.util.ArrayList;

public class SoundSequence {
	// Note: Does a sequence need to be validated to check if all sounds are
	// present? And what happens if some sounds are missing, play the sounds
	// that exists or invalidate whole sequence?
	private int delay;
	private ArrayList<String> sounds;

	public SoundSequence(int delay, ArrayList<String> sounds) {
		this.delay = delay;
		this.sounds = sounds;
	}

	public SoundSequence(int delay, String... sounds) {
		this.delay = delay;
		this.sounds = new ArrayList<String>();
		for (String sound : sounds)
			this.sounds.add(sound);
	}

	public SoundSequence(int delay, String sound) {
		this.delay = delay;
		this.sounds = new ArrayList<String>();
		sounds.add(sound);
	}

	public int getDelay() {
		return delay;
	}

	public ArrayList<String> getSounds() {
		return sounds;
	}

}
