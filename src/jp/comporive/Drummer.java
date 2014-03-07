package jp.comporive;

import android.media.AudioManager;
import android.media.SoundPool;

/**�h���}�[*/
public class Drummer {

	static double baseBpm = 60.0;	//�x�[�X�t���[��
	static double rateFrame = 1000.0 / 60.0;	//rateFrame
	
	//BPM�̎Z�o�p
	double nowBpm = 0.0;	//���݂�bpm
	double targetBpm = 0.0;	//�^�[�Q�b�g�ƂȂ�bpm
	
	//�h�����f�[�^
	private SoundPool sound = null;
	private int dMax = 10;	//�������p��
	private int id;	//ID
	private double wait = 0;	//�h������炷�|�C���g

	/**������*/
	public void initialize(){
		//������ۑ�����
		sound = new SoundPool(dMax, AudioManager.STREAM_MUSIC, 0);
	//	id = sound.load(AppliData.context, R.raw.a0, 1);
		
		//
		//calcBpm();
	}
	
	/**�I������*/
	public void shutdown(){
		
	}
	
	/**����*/
	public void process(){
		//bpm�����v�Z����
		playSound();
	}
	
	int cnt = 0;

	/**����炷*/
	public void playSound(){
		//BPM���Z�o����
		//calcBpm();
		
		//���Ԃ��Z�o����
		//calcTime();
		
		//bpm��炷
		//playDrum();
		if (++cnt >= 60){
			sound.play(id, 1.0F, 1.0F, 1, 0, 1.0F);
			cnt = 0;
		}
	}

	
	
	/**�h������炷*/
	public void playDrum(){
		long target1 = 0;	
		
		//�^�[�Q�b�g�܂ł̉����o�Ă��Ȃ�
		if (target1 < addMill)
			return;
		
		//����炷
		
		//�^�[�Q�b�g���X�V����
		
		
	}
	
	/**bpm��n��*/
	public void setBpm(double bpm){
		//Bpm
		targetBpm = bpm;
	}
	
	
	/**BPM�Z�o*/
	private void calcBpm(){
		
		//�����
		nowBpm = 60.0;
		
		//�h������炷�~���b���v�Z����
		if (nowBpm <= 0.0){
			wait = 0.0;
		}else{
			//1�b�Ԃɖ炷�^�C�~���O��ݒ肷��
			wait = 1000 / nowBpm;
		}
			
	}
	
	/**���Ԍv��*/
	private void calcTime(){
		//���݂̎��Ԃ��擾����
		long nowMill = System.currentTimeMillis();

		//���݂̎��� -�@1�t���[���O�̎��ԁ@ 
		long sub = nowMill - oldMill;
		
		//�v���X����
		addMill += sub;
		
	}

	private long oldMill = 0; //�̂̃~���T�C�Y
	private long addMill = 0;	//���~��

}
