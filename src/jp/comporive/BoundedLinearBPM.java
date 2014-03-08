package jp.comporive;

class BoundedLinearBPM implements BPMCalculator {

	private final double upperBound;
	private final double proportion;
	public final double base;
	
	public BoundedLinearBPM(double proportion, double upperBound, double base) {
		this.proportion = proportion;
		this.upperBound = upperBound;
		this.base = base;
	}
	
	@Override
	public double call(double v) {
		// 
		return Math.max(0.0, Math.min(proportion * v, upperBound)) + base;
	}
	
}