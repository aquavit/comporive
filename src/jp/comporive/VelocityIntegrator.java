package jp.comporive;

public class VelocityIntegrator {
	private double vx, vy, vz;
	public VelocityIntegrator(double initialX, double initialY, double initialZ) {
		vx = initialX;
		vy = initialY;
		vz = initialZ;
	}
	
	public void update(double accX, double accY, double accZ, double sec) {
		vx += accX * sec;
		vy += accY * sec;
		vz += accZ * sec;
	}

	public double velocityX() {
		return vx;
	}
	public double velocityY() {
		return vy;
	}
	public double velocityZ() {
		return vz;
	}
	public double magnitude() {
		return Math.sqrt(vx*vx + vy*vy + vz*vz);
	}
}
