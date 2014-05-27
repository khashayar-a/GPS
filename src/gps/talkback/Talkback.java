package gps.talkback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class Talkback extends Thread {

	private Context context;
	private SequencePlayer sp;
	private Thread t;
	private HashMap<String, Uri> sounds;
	private ArrayList<Queue<SoundSequence>> queues;
	private String state;
	private int currentlyPlayingPrio;
	private boolean active = false;

	public Talkback(Context context) {
		active = true;
		this.context = context;
		this.getSoundUris();
		queues = new ArrayList<Queue<SoundSequence>>();
		Queue<SoundSequence> temp = new LinkedList<SoundSequence>();
		queues.add(temp);
		state = "idle";
	}

	public void run() {
		while (Thread.currentThread() != null || !sounds.isEmpty()) {
			if (state.equals("idle")) {
				SoundSequence se = this.getNext();
				if (se != null) {
					this.playSequence(se);
				}
			} else if (state.equals("playing") || state.equals("delay")) {
				SoundSequence se = this.peekNext();
				if (se != null) {
					sp.cancel();
					t = null;
					sp = null;
					this.playSequence(se);
				}
			} else if (state.equals("sleep")) {
				try {
					synchronized (Thread.currentThread()) {
						wait();
					}
				} catch (InterruptedException e) {
				}
			}
		}
		active = false;
	}

	private SoundSequence peekNext() {
		// Should we keep a queue of overridden sequences, and add a funciton to
		// validate relevance?
		for (int i = 0; i < queues.size(); i++) {
			if (!queues.get(i).isEmpty()) {
				if (i < currentlyPlayingPrio) {
					System.out.println("Found SE with lower prio in Prio " + i);
					currentlyPlayingPrio = i;
					return queues.get(i).poll();
				}
			}
		}
		return null;
	}

	private SoundSequence getNext() {
		for (int i = 0; i < queues.size(); i++) {
			if (!queues.get(i).isEmpty()) {
				System.out.println("Found SE in Prio " + i);
				currentlyPlayingPrio = i;
				return queues.get(i).poll();
			}
		}
		return null;
	}

	private void playSequence(SoundSequence se) {
		sp = new SequencePlayer(this, se);
		if (sp.isValid()) {
			t = new Thread(sp);
			t.start();
			state = "playing";
		} else {
			System.out.println("Invalid track in sequence");
		}
	}

	private HashMap<String, Uri> getSoundUris() {
		sounds = new HashMap<String, Uri>();
		File dir = new File(Environment.getExternalStorageDirectory(),
				"Recallin/sounds");
		if (dir.exists()) {
			File[] children = dir.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					File f = children[i];
					sounds.put(
							f.getName().substring(0,
									f.getName().lastIndexOf(".")),
							Uri.fromFile(f));
				}
			}
		}
		return sounds;
	}

	public void queueSound(int prio, int delay, String sound) {
		if (!state.equals("sleep")) {
			SoundSequence se = new SoundSequence(delay, sound);
			this.queueSequence(prio, delay, se);
		}
	}

	public void queueSound(int prio, int delay, String... sounds) {
		if (!state.equals("sleep")) {
			SoundSequence se = new SoundSequence(delay, sounds);
			this.queueSequence(prio, delay, se);
		}
	}

	public void queueSound(int prio, int delay, ArrayList<String> sounds) {
		if (!state.equals("sleep")) {
			SoundSequence se = new SoundSequence(delay, sounds);
			this.queueSequence(prio, delay, se);
		}
	}

	private void queueSequence(int prio, int delay, SoundSequence se) {
		if (queues.size() - 1 < prio) {
			while (queues.size() - 1 < prio) {
				Queue<SoundSequence> temp = new LinkedList<SoundSequence>();
				queues.add(temp);
				System.out.println("Queueing sequence to Prio " + prio);
			}
		}
		queues.get(prio).add(se);
	}

	public HashMap<String, Uri> getSounds() {
		return sounds;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void clear() {
		state = "idle";
		// MP.stop() ?
		for (int i = 0; i < queues.size(); i++) {
			queues.get(i).clear();
		}
	}

	public void sleep() {
		state = "sleep";
		// MP.stop() ?
		// this.clear() ?
	}

	public void wakeUp() {
		state = "idle";
		synchronized (this) {
			notify();
		}
	}

	public Context getContext() {
		return context;
	}

	public void skip() {
		t.interrupt();
	}

	public boolean isActive() {
		return active;
	}

}
