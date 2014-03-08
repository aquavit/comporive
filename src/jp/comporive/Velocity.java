package jp.comporive;

/**
 * ���鎞�� (�A�v���N������̑��Βl) �ɂ�����A���x�x�N�g�� (�����x�Z���T�[����̐����l)
 */
public class Velocity {
	public final double x, y, z;
	public final double t;	// ���� (�b�P��), �A�v���J�n�� = 0.0
	public final double dt; // ���O�̃C�x���g�Ƃ̎��ԍ��� (�b�P��)
	
	public Velocity(double t, double dt, double x, double y, double z) {
		this.t = t;
		this.dt = dt;
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
