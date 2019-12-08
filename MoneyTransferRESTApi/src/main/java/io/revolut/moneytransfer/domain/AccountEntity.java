package io.revolut.moneytransfer.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity(name = "account ")
@Table(name = "account ")
public class AccountEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty(message = "Account Name cannot be blank")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Email address cannot be blank")
    @Email(message = "Email address is not valid.")
    @Column(name = "email_address", unique = true)
    private String emailAddress;

    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    public AccountEntity() {

    }

    public AccountEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountEntity setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public AccountEntity setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public AccountEntity setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
