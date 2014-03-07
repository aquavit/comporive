package jp.comporive;

import java.util.concurrent.BlockingQueue;

class SoundThread extends Thread{
	private final BlockingQueue<Velocity> queue;
	public SoundThread(BlockingQueue<Velocity> queue) {
		this.queue = queue;
	}
	public boolean shutdown = false;
	Drummer drum = new Drummer();
	
	@Override
	public void run() {
	
		//�h�����̏�����
		drum.initialize();
		
		while (!shutdown){
			//�h��������
			drum.process();
		}
		
		//�h�����̏I������
		drum.shutdown();
	}
}