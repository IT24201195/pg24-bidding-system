package com.pg24.bidding.common;

import java.util.List;

public record PageResponse<T>(List<T> items, long total) {}
