package production.myProductions;

import matrix.DoubleMatrixCell;
import production.AbstractProduction;

public class B extends AbstractProduction<DoubleMatrixCell> {

    public B(DoubleMatrixCell matrixCell, DoubleMatrixCell multiplier) {
        super(matrixCell, multiplier);
    }

    @Override
    public DoubleMatrixCell apply(DoubleMatrixCell matrixCell, DoubleMatrixCell multiplier) {
        return new DoubleMatrixCell(matrixCell.getValue() * multiplier.getValue());
    }
}
