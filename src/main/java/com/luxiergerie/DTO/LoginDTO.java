package com.luxiergerie.DTO;

/**
 * This class represents a LoginDto object, which is used for transferring login information.
 */
public class LoginDTO {

    private String serialNumber;

    private String password;

    /**
     * Constructs a new LoginDto object.
     */
    public LoginDTO() {
    }

    /**
     * Returns the serial number associated with the login.
     *
     * @return the serial number
     */
    public String getSerialNumber() {
      return serialNumber;
    }

    /**
     * Sets the serial number associated with the login.
     *
     * @param serialNumber the serial number to set
     */
    public void setSerialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
    }

    /**
     * Returns the password associated with the login.
     *
     * @return the password
     */
    public String getPassword() {
      return password;
    }

    /**
     * Sets the password associated with the login.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
      this.password = password;
    }
}
