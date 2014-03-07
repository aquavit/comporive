package jp.comporive;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**�h���}�[*/
public class Drummer {

	static double baseBpm = 60.0;	//�x�[�X�t���[��
	static double rateFrame = 1000.0 / 60.0;	//rateFrame
	
	//BPM�̎Z�o�p
	double nowBpm = 0.0;	//���݂�bpm
	double targetBpm = 0.0;	//�^�[�Q�b�g�ƂȂ�bpm
	
	//private static int BUF_SIZE = 30;
	
	//�h�����f�[�^
	private SoundPool sound = null;
	private int dMax = 10;	//�������p��
	private int id;	//ID
	private long wait = 0;	//�h������炷�|�C���g
	private long target = 0;	
	//private long list[] = new long[BUF_SIZE];
	//private int focus = 0;
	
	
	/**������*/
	public void initialize(){
		//������ۑ�����
		sound = new SoundPool(dMax, AudioManager.STREAM_MUSIC, 0);
		id = sound.load(AppliData.context, R.raw.a0, 1);
		
		//
		long now = System.currentTimeMillis();
		target =  1000;
		samMill = 0;
		
		/*
		//�炷�^�C�~���O���v�Z����
		for (int i=0; i<BUF_SIZE; i++){
			list[i] = now+1000 + (1000*i);
		}
		*/
		//
		//calcBpm();
	}
	
	/**�I������*/
	public void shutdown(){
		
	}
	
	/**����*/
	public void process(){
		//calcBpm();
		
		//���Ԃ��Z�o����
		//calcTime();
		
		//bpm�����v�Z����
		playSound();
	}
	
	int cnt = 0;

	/**����炷*/
	private void playSound(){
		//BPM���Z�o����
		calcBpm();
		
		//���Ԃ��Z�o����
		calcTime();
		
		//bpm��炷
		playDrum();
		
		/*
		if (++cnt >= 60){
		
			cnt = 0;
		}
		*/
	}
	
	/**�h������炷*/
	private void playDrum(){
		Log.e("Time", "target = "+target);
		Log.e("Time", "sam = "+samMill);
		
		//�^�[�Q�b�g�܂ł̉����o�Ă��Ȃ�
		if (target > samMill)
			return;

		//
		target = samMill + wait;
		
		//����炷
		sound.play(id, 1.0F, 1.0F, 1, 0, 1.0F);

		Log.e("Sound", "PLAY SOUND--------------------");
	}
	
	/**bpm��n��*/
	public void setBpm(double bpm){
		//Bpm
		targetBpm = bpm;
	}
	
	
	/**BPM�Z�o*/
	private void calcBpm(){
		
		//�����
		nowBpm = 180.0;
		
		//�h������炷�~���b���v�Z����
		if (nowBpm <= 0.0){
			wait = 0;
		}else{
			//60BPM �� 1000�~��(1�b)�Ɉ��Ȃ̂ŁA�������Z�o����
			double rate = 60.0 / nowBpm;
			wait = (long)(1000.0 * rate);
		}
	}
	
	/**���Ԍv��*/
	private void calcTime(){
		//�������̏���
		if (oldMill == -1){
			oldMill = System.currentTimeMillis();
			return;
		}
			
		//���݂̎��Ԃ��擾����
		long nowMill = System.currentTimeMillis();

		//���݂̎��� -�@1�t���[���O�̎��ԁ@ 
		long sub = nowMill - oldMill;
		
		//�v���X����
		samMill += sub;
		
		//
		oldMill = nowMill;
	}

	private long oldMill = -1; //�̂̃~���T�C�Y
	private long samMill = 0;	//���~��

}
