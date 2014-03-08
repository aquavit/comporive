package jp.comporive;

/**
 * ある時刻 (アプリ起動からの相対値) における、速度ベクトル (加速度センサーからの推測値)
 */
public class Velocity {
	public final double x, y, z;
	public final double t;	// 時刻 (秒単位), アプリ開始時 = 0.0
	
	public Velocity(double t, double x, double y, double z) {
		this.t = t;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public String toString() {
		return "at: " + t + ", velocity = (" + x + ", " + y + ", " + z + ")";
	}
}
