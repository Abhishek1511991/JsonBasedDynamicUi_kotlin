package com.demo.dynamicjsonui.dynamicui.utils;

/**
 * Created by Abhishek at 03/10/2022
 */
public class ValidationStatus {
    boolean isValid;
    String errorMessage;

    public ValidationStatus(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
