package main.java.com.amanda.quotesbr.exceptions;

public class DAOException extends RuntimeException {

    private int code;

    public DAOException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
