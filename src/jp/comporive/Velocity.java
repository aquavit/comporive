package jp.comporive;

/**
 * ���鎞�� (�A�v���N������̑��Βl) �ɂ�����A���x�x�N�g�� (�����x�Z���T�[����̐����l)
 */
public class Velocity {
	public final double x, y, z;
	public final double t;	// ���� (�b�P��), �A�v���J�n�� = 0.0
	
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
