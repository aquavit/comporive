package jp.comporive;

public interface BPMCalculator {
	public double call(Velocity v);
}

class BoundedLinearBPM implements BPMCalculator {

	private final double upperBound;
	private final double proportion;
	
	public BoundedLinearBPM(double proportion, double upperBound) {
		this.proportion = proportion;
		this.upperBound = upperBound;
	}
	
	@Override
	public double call(Velocity v) {
		// 
		return Math.max(1.0, Math.min(proportion * v.magnitude(), upperBound));
	}
	
}