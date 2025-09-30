package com.pg24.bidding.payment.dto.response;

import java.time.Instant;

public class BankSlipInfoResponse {
    private Long id;
    private String filename;
    private String mimeType;
    private long sizeBytes;
    private String storagePath;
    private Instant uploadedAt;

    public BankSlipInfoResponse(Long id, String filename, String mimeType, long sizeBytes, String storagePath, Instant uploadedAt) {
        this.id = id; this.filename = filename; this.mimeType = mimeType; this.sizeBytes = sizeBytes; this.storagePath = storagePath; this.uploadedAt = uploadedAt;
    }
    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getMimeType() { return mimeType; }
    public long getSizeBytes() { return sizeBytes; }
    public String getStoragePath() { return storagePath; }
    public Instant getUploadedAt() { return uploadedAt; }
}
