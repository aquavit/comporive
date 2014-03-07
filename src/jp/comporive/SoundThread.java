package jp.comporive;

import java.util.concurrent.BlockingQueue;

class SoundThread extends Thread{
	private final BlockingQueue<Velocity> queue;
	public SoundThread(BlockingQueue<Velocity> queue) {
		this.queue = queue;
	}
	@Override
	public void run() {
	}
}