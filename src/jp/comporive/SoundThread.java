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
	
		//ドラムの初期化
		drum.initialize();
		
		while (!shutdown){
			//ドラム処理
			drum.process();
		}
		
		//ドラムの終了処理
		drum.shutdown();
	}
}