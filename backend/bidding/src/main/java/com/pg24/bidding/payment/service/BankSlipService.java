package com.pg24.bidding.payment.service;

import com.pg24.bidding.payment.config.StorageProperties;
import com.pg24.bidding.payment.exception.InvalidPaymentStateException;
import com.pg24.bidding.payment.exception.PaymentNotFoundException;
import com.pg24.bidding.payment.exception.SlipUploadException;
import com.pg24.bidding.payment.model.Payment;
import com.pg24.bidding.payment.model.PaymentSlip;
import com.pg24.bidding.payment.repository.PaymentRepository;
import com.pg24.bidding.payment.repository.PaymentSlipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BankSlipService {
    private static final Set<String> ALLOWED = Set.of("image/png","image/jpeg","application/pdf");
    private static final long MAX_SIZE = 10L * 1024 * 1024; // enforce with Spring too

    private final PaymentRepository payments;
    private final PaymentSlipRepository slips;
    private final PaymentService paymentService;
    private final StorageProperties storage;

    public BankSlipService(PaymentRepository payments, PaymentSlipRepository slips,
                           PaymentService paymentService, StorageProperties storage) {
        this.payments = payments; this.slips = slips; this.paymentService = paymentService; this.storage = storage;
    }

    public PaymentSlip upload(Long paymentId, MultipartFile file, Long uploaderId) {
        Payment payment = payments.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        if (file == null || file.isEmpty()) throw new SlipUploadException("Empty file");
        if (file.getSize() > MAX_SIZE) throw new SlipUploadException("File too large");
        String mime = file.getContentType() == null ? "" : file.getContentType();
        if (!ALLOWED.contains(mime)) throw new SlipUploadException("Unsupported file type: " + mime);

        // Store file
        try {
            Path base = Paths.get(storage.getBaseDir()).toAbsolutePath().normalize();
            Files.createDirectories(base);
            String safeName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Path dest = base.resolve(safeName);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            PaymentSlip slip = new PaymentSlip();
            slip.setPayment(payment);
            slip.setFilename(file.getOriginalFilename());
            slip.setMimeType(mime);
            slip.setSizeBytes(file.getSize());
            slip.setStoragePath(dest.toString());
            slip.setUploadedBy(uploaderId);

            PaymentSlip saved = slips.save(slip);

            // move payment state to UNDER_REVIEW
            paymentService.moveToUnderReview(payment);

            return saved;
        } catch (IOException e) {
            throw new SlipUploadException("Failed to store file", e);
        } catch (InvalidPaymentStateException ise) {
            throw ise;
        }
    }

    @Transactional(readOnly = true)
    public List<PaymentSlip> listForPayment(Long paymentId) {
        return slips.findByPaymentId(paymentId);
    }
}
