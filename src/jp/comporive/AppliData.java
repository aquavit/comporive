package jp.comporive;

import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**コンテキスト管理*/
public class AppliData {
	public static final short DEBUG_MODE = 0;	//1-デバッグ　0-リリース
	public static final short WRITE_OPTION = 0;
	
	//開発時の画面サイズ
	public static final int bWidth = 960;	//製作時の基にした画面幅
	public static final int bHeight = 540;	//製作時の基にした画面縦
	
	//プログラム全般で使う重要データ
	public static final int FRAME = 30;
	public static GL11 gl = null;
	public static Context context = null;
	public static int sWidth;		//ゲーム画面幅
	public static int sHeight;		//ゲーム画面高さ
	public static int wWidth;	//ウィンドウの幅
	public static int wHeight; 	//ウィンドウの高さ
	
	public static int sBaseX;	//スクリーン座標のAndroid画面内の起点位置
	public static int sBaseY;	//スクリーン座標のAndroid画面内の起点位置
	public static GLSurfaceView render = null;
	
	/**GLキューイベント*/
	public static void queGlEvent(Runnable proc){
		if (null == render) return;
		render.queueEvent(proc);
	}
	
	/**現在設定しているフレーム数に変換したフレーム数を返す
	 * @param FPS60時のフレーム数
	 * @return 現在設定しているFPS用に変換したフレーム数
	 * */
	public static int getRevisedFrame(int fps60Frame){
		//FPSが60だったときは、そのまま返す。
		if (FRAME == 60) 
			return fps60Frame;
				
		//30フレームの場合は、2で割って返す。
		return fps60Frame/2;
	}
	
	/**渡されたスケール値と現在の画面サイズXをかけたものを返す
	 * */
	public static float getResizeX2Scale(float scaleX){
		return sWidth*scaleX;
	}
	
	/**渡されたスケール値と現在の画面サイズYをかけたものを返す
	 * */
	public static float getResizeY2Scale(float scaleY){
		return sHeight*scaleY;
	}
	
	/**ウィンドウ座標に変換する*/
	public static float getResizeWindowX(float scaleX){
		//スクリーン画面内の位置を算出
		float x = sWidth*scaleX;
		
		//Android画面の中心地を算出
		float cx = wWidth*0.5f;
		
		//基点位置を算出
		float bx = cx - sWidth*0.5f;
		
		return bx+x;
	}
	
	/**ウィンドウ座標に変換する*/
	public static float getResizeWindowY(float scaleY){
		//スクリーン画面内の位置を算出
		float y = sHeight*scaleY;
		
		//Android画面の中心地を算出
		float cy = wHeight*0.5f;
		
		//基点位置を算出
		float by = cy - sHeight*0.5f;
		
		return by+y;
	}
	
	/**作成時のサイズをもとにデータを補正する
	 * @return 現在のスクリーン座標X*(渡したデータ/制作時の画面サイズX)
	 * */
	public static float getResizeWidthSize(float width){
		//渡された数値を制作サイズで割り、スケール値を求める
		//それを現在のスクリーン値にかける
		return sWidth * (width/bWidth);
	}
	
	/**作成時のサイズをもとにデータを補正する
	 * @return 現在のスクリーン座標Y*(渡したデータ/制作時の画面サイズY)
	 * */
	public static float getResizeHeightSize(float height){
		return sHeight * (height/bHeight);
	}
	
	/**幅*/
	public static float getWidthToScreenSize(int width){
		return sWidth * getBaseWidthRate(width);
	}
	
	/**高さ*/
	public static float getHeightToScreenSize(int width){
		return sHeight * getBaseWidthRate(width);
	}
	
	/**製作時の幅サイズを基にした割合を返す*/
	public static float getBaseWidthRate(int width){
		return width/bWidth;
	}
	
	/**制作時の縦サイズを基にした割合を返す*/
	public static float getBaseHeightRate(int height){
		return height/bHeight;
	}
}


