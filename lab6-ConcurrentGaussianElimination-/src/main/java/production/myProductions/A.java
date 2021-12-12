package production.myProductions;

import matrix.DoubleMatrixCell;
import production.AbstractProduction;

public class A extends AbstractProduction<DoubleMatrixCell> {

    public A(DoubleMatrixCell matrixCell1, DoubleMatrixCell matrixCell2) {
        super(matrixCell1, matrixCell2);
    }

    @Override
    public DoubleMatrixCell apply(DoubleMatrixCell matrixCell1, DoubleMatrixCell matrixCell2) {
        return new DoubleMatrixCell(matrixCell1.getValue() / matrixCell2.getValue());
    }
}
