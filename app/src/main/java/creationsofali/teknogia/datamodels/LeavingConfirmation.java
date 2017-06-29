package creationsofali.teknogia.datamodels;

/**
 * Created by ali on 6/16/17.
 */

public class LeavingConfirmation {
    private boolean isCheckBoxChecked, confirmationValue;

    public boolean isCheckBoxChecked() {
        return isCheckBoxChecked;
    }

    public void setCheckBoxChecked(boolean checkBoxChecked) {
        isCheckBoxChecked = checkBoxChecked;
    }

    public boolean isConfirmationValue() {
        return confirmationValue;
    }

    public void setConfirmationValue(boolean confirmationValue) {
        this.confirmationValue = confirmationValue;
    }
}
