package gps.talkback;

import android.media.MediaPlayer;
import android.net.Uri;

public class SequencePlayer implements Runnable {

	private Talkback tb;
	private SoundSequence se;
	private MediaPlayer mp;
	private boolean valid = true;;

	public SequencePlayer(Talkback tb, SoundSequence se) {
		this.tb = tb;
		this.se = se;
		this.validate(this.se);
	}

	private void validate(SoundSequence se) {
		for (int i = 0; i < se.getSounds().size(); i++) {
			Uri temp = tb.getSounds().get(se.getSounds().get(i));
			if (temp == null) {
				valid = false;
			}
		}
	}

	public void run() {
		Uri currentTrack = tb.getSounds().get(se.getSounds().get(0));
		mp = MediaPlayer.create(tb.getContext(), currentTrack);
		mp.start();
		try {
			Thread.sleep(mp.getDuration());
		} catch (InterruptedException e1) {
			mp.stop();
		}

		for (int i = 1; i < se.getSounds().size(); i++) {
			try {
				tb.setState("delay");
				Thread.sleep(se.getDelay());
			} catch (InterruptedException e) {
			}
			tb.setState("playing");
			currentTrack = tb.getSounds().get(se.getSounds().get(i));
			mp = MediaPlayer.create(tb.getContext(), currentTrack);
			mp.start();
			try {
				Thread.sleep(mp.getDuration());
			} catch (InterruptedException e1) {
				mp.stop();
				continue;
			}
		}
		mp.release();
		tb.setState("idle");
	}

	public void cancel() {
		mp.stop();
		mp.release();
	}

	public boolean isValid() {
		return valid;
	}

}
