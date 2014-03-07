package jp.comporive;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**ドラマー*/
public class Drummer {

	static double baseBpm = 60.0;	//ベースフレーム
	static double rateFrame = 1000.0 / 60.0;	//rateFrame
	
	//BPMの算出用
	double nowBpm = 0.0;	//現在のbpm
	double targetBpm = 0.0;	//ターゲットとなるbpm
	
	//private static int BUF_SIZE = 30;
	
	//ドラムデータ
	private SoundPool sound = null;
	private int dMax = 10;	//同時利用数
	private int id;	//ID
	private long wait = 0;	//ドラムを鳴らすポイント
	private long target = 0;	
	//private long list[] = new long[BUF_SIZE];
	//private int focus = 0;
	
	
	/**初期化*/
	public void initialize(){
		//音情報を保存する
		sound = new SoundPool(dMax, AudioManager.STREAM_MUSIC, 0);
		id = sound.load(AppliData.context, R.raw.a0, 1);
		
		//
		long now = System.currentTimeMillis();
		target =  1000;
		samMill = 0;
		
		/*
		//鳴らすタイミングを計算する
		for (int i=0; i<BUF_SIZE; i++){
			list[i] = now+1000 + (1000*i);
		}
		*/
		//
		//calcBpm();
	}
	
	/**終了処理*/
	public void shutdown(){
		
	}
	
	/**処理*/
	public void process(){
		//calcBpm();
		
		//時間を算出する
		//calcTime();
		
		//bpm数を計算する
		playSound();
	}
	
	int cnt = 0;

	/**音を鳴らす*/
	private void playSound(){
		//BPMを算出する
		calcBpm();
		
		//時間を算出する
		calcTime();
		
		//bpmを鳴らす
		playDrum();
		
		/*
		if (++cnt >= 60){
		
			cnt = 0;
		}
		*/
	}
	
	/**ドラムを鳴らす*/
	private void playDrum(){
		Log.e("Time", "target = "+target);
		Log.e("Time", "sam = "+samMill);
		
		//ターゲットまでの音が出ていない
		if (target > samMill)
			return;

		//
		target = samMill + wait;
		
		//音を鳴らす
		sound.play(id, 1.0F, 1.0F, 1, 0, 1.0F);

		Log.e("Sound", "PLAY SOUND--------------------");
	}
	
	/**bpmを渡す*/
	public void setBpm(double bpm){
		//Bpm
		targetBpm = bpm;
	}
	
	
	/**BPM算出*/
	private void calcBpm(){
		
		//仮代入
		nowBpm = 180.0;
		
		//ドラムを鳴らすミリ秒を計算する
		if (nowBpm <= 0.0){
			wait = 0;
		}else{
			//60BPM は 1000ミリ(1秒)に一回なので、割合を算出する
			double rate = 60.0 / nowBpm;
			wait = (long)(1000.0 * rate);
		}
	}
	
	/**時間計測*/
	private void calcTime(){
		//初期時の処理
		if (oldMill == -1){
			oldMill = System.currentTimeMillis();
			return;
		}
			
		//現在の時間を取得する
		long nowMill = System.currentTimeMillis();

		//現在の時間 -　1フレーム前の時間　 
		long sub = nowMill - oldMill;
		
		//プラスする
		samMill += sub;
		
		//
		oldMill = nowMill;
	}

	private long oldMill = -1; //昔のミリサイズ
	private long samMill = 0;	//総ミリ

}
