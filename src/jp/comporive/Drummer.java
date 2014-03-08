package jp.comporive;

import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.SoundPool;

/**�h���}�[*/
public class Drummer {

	private static double SEC = 1000000000.0;
	static double baseBpm = 60.0;	//�x�[�X�t���[��
	
	//BPM�̎Z�o�p
	double nowBpm = 180.0;	//���݂�bpm
	double targetBpm = 0.0;	//�^�[�Q�b�g�ƂȂ�bpm

	SoundPool sound = null;
	
	//�y��̃f�[�^
	class SoundData{
		Timer timer = null;
		int bufSize = 10;	//���y�̓������p��
		int id = 0;	//ID
		long waitTime = 0;	//�ҋ@����
		long targetTime = 0;	//���ԎZ�o
		long sub = 0;
		boolean play = false;
	}
	
	Timer timer = new Timer();
	SoundData drum = new SoundData();//�h�����f�[�^
	SoundData snear = new SoundData();	//�X�l�A�f�[�^

	
	class DrumTask extends TimerTask{
		
		long waitTime = 0;
		public DrumTask(long rate){
			
			this.waitTime = (long)((double)rate * 0.5);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//long progress = System.currentTimeMillis();
			
			sound.play(drum.id, 1.0F, 1.0F, 1, 0, 1.f);
			
			//
			//progress = System.currentTimeMillis() - progress;
			
			try {
				//�ҋ@����-�o�ߎ���
				//long wait = waitTime - progress;
				
				//�ҋ@���Ԃ���������A�ҋ@
				//if (wait > 0){
					Thread.sleep(waitTime);
		//		}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	class SnearTask extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	/**������*/
	public void initialize(){
		
		sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		initDrum();
		initSnear();
		
		samMill = 0;
		
		
		long rate = (long) (1000.0*(60.0 / nowBpm));
		
		timer.schedule(new DrumTask(rate), 0, rate);
	//	drum.timer.schedule(new DrumTask(), 0, 500);
	//	snear.timer.schedule(new SnearTask(), 0, 250);
		
		//sound.setLoop(drum.id, -1);
	}
	
	/**�I������*/
	public void shutdown(){
		
	}
	
	/**����*/
	public void process(){
		
		//bpm�����v�Z����
		playSound();
	}
	

	/**����炷*/
	private void playSound(){
		/*
		if (!drum.play){
			//sound.setLoop(drum.id, -1);
			sound.play(drum.id, 1.0F, 1.0F, 1, 0, 1.f);
			drum.play = true;
		}
		*/
		
		
		//BPM���Z�o����
	//	calcBpm();
		
		//���Ԃ��Z�o����
	//	calcTime();
		
		//bpm��炷
		//playDrum();
	
	//	playSnear();
	}
	
	/**�h������炷*/
	private void playDrum(){
		if (drum == null) return;
		//Log.e("Time", "target = "+target);
		//Log.e("Time", "sam = "+samMill);
		
		//�^�[�Q�b�g�܂ł̉����o�Ă��Ȃ�
		if (drum.targetTime > samMill)
			return;

	//	Log.e("Sound", "PLAY SOUND="+(target - samMill));
		
		//
		/*
		//samMill = drum.targetTime;
		{
			//long sub = drum.targetTime - samMill + drum.sub -100;
			//drum.sub = (0>sub)? 0:sub;
			drum.targetTime += drum.waitTime;
		}
		*/
		
	}
	
	private void playSnear(){
		if (snear == null) return;
		
		//�^�[�Q�b�g�܂ł̉����o�Ă��Ȃ�
		if (snear.targetTime > samMill)
			return;

		snear.targetTime += drum.waitTime;
		
		//����炷
		sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
	}
	
	/**bpm��n��*/
	public void setBpm(double bpm){
		final double offset = 5.0;	//�I�t�Z�b�g�l
	
		//bpm��0�ȉ��̏ꍇ�́A�Đ����Ȃ�
		if (bpm <= 0.0){
			timer.cancel();
			return;
		}
		
		//���l�ȏ�ω����Ȃ��ꍇ�͕Ԃ�
		if (nowBpm+offset >= bpm && nowBpm-offset<=bpm){
			return;
		}
		
		//�I������
		timer.cancel();
	
		//Bpm
		long rate = (long) (1000.0*(60.0 / bpm));
		timer.schedule(new DrumTask(rate), 0, rate);
	}
		
	/**BPM�Z�o*/
	public void calcBpm(){
		return;
		/*
		//�����
		//�h������炷�~���b���v�Z����
		if (nowBpm <= 0.0){
			drum.waitTime = 0;
		}else{
			//60BPM �� 1000�~��(1�b)�Ɉ��Ȃ̂ŁA�������Z�o����
			double rate = 60.0 / nowBpm;
			
			
	
			drum.waitTime = (long)(SEC * rate);
		}*/
	}
	
	
	/**�h����������*/
	private void initDrum(){
		drum.timer = new Timer();
		drum.id = sound.load(AppliData.context, R.raw.a0, 1);
		
		//60BPM �� 1000�~��(1�b)�Ɉ��Ȃ̂ŁA�������Z�o����
		double rate = 60.0 / nowBpm;
		rate = SEC * rate;
		
		drum.waitTime = (long)rate;
		drum.targetTime = (long)rate;

		//����炷
		

		//sound.setRate(drum.id, 1.f);		
	}
	
	/**�X�l�A������*/
	private void initSnear(){
		snear.timer = new Timer();
		//sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		snear.id = sound.load(AppliData.context, R.raw.a4, 1);
		
		double rate = 60.0 / nowBpm;
		rate = (SEC * rate)*1.5;
		
		snear.waitTime = (long)rate;
		snear.targetTime = (long)rate;
	}
	
	/**���Ԍv��*/
	public void calcTime(){
		return;
		/*
		//�������̏���
		if (oldMill == -1){
			oldMill = getTime();
			return;
		}
			
		//���݂̎��Ԃ��擾����
		//long nowMill = System.currentTimeMillis();
		long nowMill = getTime();

		//���݂̎��� -�@1�t���[���O�̎���
		samMill += nowMill - oldMill;
		
		//
		oldMill = nowMill;
		*/
	}
	
	private long getTime(){return System.nanoTime();}

	private long oldMill = -1; //�̂̃~���T�C�Y
	private long samMill = 0;	//���~
}
