package common;

import model.Checker;

public class FieldMockup {
    private CheckerMockup checker;
    private boolean is_selected;

    public CheckerMockup getCheckerMockup() {
        return checker;
    }

    public boolean isSelected() {
        return is_selected;
    }

    public FieldMockup(CheckerMockup checker, boolean is_selected) {
        this.checker = checker;
        this.is_selected = is_selected;
    }
}
