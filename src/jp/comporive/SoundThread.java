package jp.comporive;

class SoundThread extends Thread{
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