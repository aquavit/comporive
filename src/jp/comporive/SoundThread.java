package jp.comporive;

class SoundThread extends Thread{
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