package jp.comporive;

import android.media.AudioManager;
import android.media.SoundPool;

/**ドラマー*/
public class Drummer {

	static double baseBpm = 60.0;	//ベースフレーム
	static double rateFrame = 1000.0 / 60.0;	//rateFrame
	
	//BPMの算出用
	double nowBpm = 0.0;	//現在のbpm
	double targetBpm = 0.0;	//ターゲットとなるbpm
	
	//ドラムデータ
	private SoundPool sound = null;
	private int dMax = 10;	//同時利用数
	private int id;	//ID
	private double wait = 0;	//ドラムを鳴らすポイント

	/**初期化*/
	public void initialize(){
		//音情報を保存する
		sound = new SoundPool(dMax, AudioManager.STREAM_MUSIC, 0);
	//	id = sound.load(AppliData.context, R.raw.a0, 1);
		
		//
		//calcBpm();
	}
	
	/**終了処理*/
	public void shutdown(){
		
	}
	
	/**処理*/
	public void process(){
		//bpm数を計算する
		playSound();
	}
	
	int cnt = 0;

	/**音を鳴らす*/
	public void playSound(){
		//BPMを算出する
		//calcBpm();
		
		//時間を算出する
		//calcTime();
		
		//bpmを鳴らす
		//playDrum();
		if (++cnt >= 60){
			sound.play(id, 1.0F, 1.0F, 1, 0, 1.0F);
			cnt = 0;
		}
	}

	
	
	/**ドラムを鳴らす*/
	public void playDrum(){
		long target1 = 0;	
		
		//ターゲットまでの音が出ていない
		if (target1 < addMill)
			return;
		
		//音を鳴らす
		
		//ターゲットを更新する
		
		
	}
	
	/**bpmを渡す*/
	public void setBpm(double bpm){
		//Bpm
		targetBpm = bpm;
	}
	
	
	/**BPM算出*/
	private void calcBpm(){
		
		//仮代入
		nowBpm = 60.0;
		
		//ドラムを鳴らすミリ秒を計算する
		if (nowBpm <= 0.0){
			wait = 0.0;
		}else{
			//1秒間に鳴らすタイミングを設定する
			wait = 1000 / nowBpm;
		}
			
	}
	
	/**時間計測*/
	private void calcTime(){
		//現在の時間を取得する
		long nowMill = System.currentTimeMillis();

		//現在の時間 -　1フレーム前の時間　 
		long sub = nowMill - oldMill;
		
		//プラスする
		addMill += sub;
		
	}

	private long oldMill = 0; //昔のミリサイズ
	private long addMill = 0;	//総ミリ

}
