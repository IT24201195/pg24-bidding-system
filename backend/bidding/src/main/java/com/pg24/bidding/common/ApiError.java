package com.pg24.bidding.common;

import org.springframework.http.HttpStatus;

public record ApiError(String message, HttpStatus status) {}
