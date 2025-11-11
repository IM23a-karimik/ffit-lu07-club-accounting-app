package ch.bzz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "booking_number", nullable = false, unique = true, length = 30)
    private String bookingNumber;

    @Column(name = "booking_date", nullable = false)
    private LocalDate date;

    @Column(name = "booking_text", nullable = false, length = 250)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id", nullable = false)
    private Account debitAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id", nullable = false)
    private Account creditAccount;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_name", referencedColumnName = "project_name", nullable = false)
    private Project project;

    public Booking(String bookingNumber, LocalDate date, String text, Account debitAccount,
                   Account creditAccount, BigDecimal amount, Project project) {
        this.bookingNumber = bookingNumber;
        this.date = date;
        this.text = text;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.amount = amount;
        this.project = project;
    }
}
