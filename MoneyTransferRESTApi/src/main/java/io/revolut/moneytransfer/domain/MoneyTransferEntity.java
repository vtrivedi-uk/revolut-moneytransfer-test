package io.revolut.moneytransfer.domain;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "money_transfer ")
@Table(name = "money_transfer ")
public class MoneyTransferEntity {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull(message = "Originator Account Number cannot be blank")
	@Column(nullable = false)
	private Long originatorAccountNumber;

	@NotNull(message = "Beneficiary Account Number cannot be blank")
	@Column(nullable = false)
	private Long beneficiaryAccountNumber;

	@NotNull(message = "Transfer Amount cannot be blank")
	@Column(nullable = false)
	private BigDecimal transferAmount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MoneyTransferStatus transferStatus;

	@Column
	private Date transferDate;
	/**
	 * reason for transaction failure if transaction failed.
	 */
	@Column
	private String reason;

	public MoneyTransferEntity(
			@NotNull(message = "Originator Account Number cannot be blank") Long originatorAccountNumber,
			@NotNull(message = " Beneficiary Account Number cannot be blank") Long beneficiaryAccountNumber,
			@NotNull(message = "Transaction Amount cannot be blank") BigDecimal transferAmount,
			MoneyTransferStatus transferStatus, 
			Date transferDate) {
		this.originatorAccountNumber = originatorAccountNumber;
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
		this.transferAmount = transferAmount;
		this.transferStatus = transferStatus;
		this.transferDate = transferDate;
	}

	public Long getId() {
		return id;
	}

	public MoneyTransferEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getOriginatorAccountNumber() {
		return originatorAccountNumber;
	}

	public MoneyTransferEntity setOriginatorAccountNumber(Long originatorAccountNumber) {
		this.originatorAccountNumber = originatorAccountNumber;
		return this;
	}

	public Long getBeneficiaryAccountNumber() {
		return beneficiaryAccountNumber;
	}

	public MoneyTransferEntity setBeneficiaryAccountNumber(Long beneficiaryAccountNumber) {
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
		return this;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public MoneyTransferEntity setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
		return this;
	}

	public MoneyTransferStatus getTransferStatus() {
		return transferStatus;
	}

	public MoneyTransferEntity setTransferStatus(MoneyTransferStatus transferStatus) {
		this.transferStatus = transferStatus;
		return this;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public MoneyTransferEntity setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public MoneyTransferEntity setReason(String reason) {
		this.reason = reason;
		return this;
	}

	@Override
	public String toString() {
		return "MoneyTransferEntity [id=" + id + ", originatorAccountNumber=" + originatorAccountNumber
				+ ", beneficiaryAccountNumber=" + beneficiaryAccountNumber + ", transferAmount=" + transferAmount
				+ ", transferStatus=" + transferStatus + ", transferDate=" + transferDate + ", reason=" + reason + "]";
	}
}
