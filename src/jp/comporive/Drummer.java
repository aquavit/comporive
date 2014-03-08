package jp.comporive;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;

/**�h���}�[*/
public class Drummer {

	private static double SEC = 1000000000.0;
	static double baseBpm = 60.0;	//�x�[�X�t���[��
	
	//BPM�̎Z�o�p
	double nowBpm = 60.0;	//���݂�bpm
	double targetBpm = 0.0;	//�^�[�Q�b�g�ƂȂ�bpm

	SoundPool sound = null;
	
	//�y��̃f�[�^
	class SoundData{
		byte[] s = null;
		Timer timer = null;
		int bufSize = 10;	//���y�̓������p��
		AudioTrack at;
	//	int id = 0;	//ID
		long waitTime = 0;	//�ҋ@����
		long targetTime = 0;	//���ԎZ�o
		long sub = 0;
		//boolean play = false;
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
			
	//		sound.play(drum.id, 1.0F, 1.0F, 1, 0, 1.f);
			drum.at.write(drum.s, 0, drum.s.length);
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
			
			//�X�l�A�ǂݍ���
			snear.at.write(snear.s, 0, snear.s.length);
			
			
//			sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	class SnearTask extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
	//		sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
		}
	}
	
	int minBufferSize = 0;
	
	
	public Drummer(){		
		
	}
	/**������*/
	public void initialize(){
		
		//sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
		
		initDrum();
		initSnear();
		
		samMill = 0;
		long rate = (long) (1000.0*(60.0 / nowBpm));
		drum.at.play(); 
		snear.at.play();
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
	//	sound.play(snear.id, 1.0F, 1.0F, 1, 0, 1.f);
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
	
	/**�T�E���h�f�[�^�̓ǂݍ���*/
	private void readSoundData(int soundId, SoundData soundData){
		
		//�I�[�f�B�I�g���b�N�̍쐬
		soundData.at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
	
		//�h�����f�[�^���Ăяo��
        try {
        	InputStream in = AppliData.context.getResources().openRawResource(soundId);
            DataInputStream dis = new DataInputStream(in);
			soundData.s = new byte[in.available()];
			dis.readFully(soundData.s);	
			in.close();
			dis.close();
		} catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        }
         catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	/**�h����������*/
	private void initDrum(){
		drum.timer = new Timer();
		
		//�T�E���h�f�[�^�̓ǂݍ���
		readSoundData(R.raw.a0, drum);

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

		//�T�E���h�f�[�^�̓ǂݍ���
		readSoundData(R.raw.a0, snear);

		//sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		//snear.id = sound.load(AppliData.context, R.raw.a4, 1);
		
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
