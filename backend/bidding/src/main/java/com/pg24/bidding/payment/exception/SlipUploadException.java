package com.pg24.bidding.payment.exception;

public class SlipUploadException extends RuntimeException {
    public SlipUploadException(String message) { super(message); }
    public SlipUploadException(String message, Throwable cause) { super(message, cause); }
}
