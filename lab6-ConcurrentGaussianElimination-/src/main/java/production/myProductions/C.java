package production.myProductions;

import matrix.DoubleMatrixCell;
import production.AbstractProduction;

public class C extends AbstractProduction<DoubleMatrixCell> {

    public C(DoubleMatrixCell matrixCell, DoubleMatrixCell nValue) {
        super(matrixCell, nValue);
    }

    @Override
    public DoubleMatrixCell apply(DoubleMatrixCell matrixCell, DoubleMatrixCell nValue) {
        matrixCell.setValue(matrixCell.getValue() - nValue.getValue());
        return matrixCell;
    }
}
