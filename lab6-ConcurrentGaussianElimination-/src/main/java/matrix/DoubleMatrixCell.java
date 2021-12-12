package matrix;

public class DoubleMatrixCell {

    private double value;

    public DoubleMatrixCell() {
        this.value = 0.0;
    }

    public DoubleMatrixCell(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void subtract(double value) {
        this.value -= value;
    }

    public void divide(double value) {
        this.value /= value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
