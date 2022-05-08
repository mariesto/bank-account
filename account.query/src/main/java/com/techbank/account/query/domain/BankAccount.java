package com.techbank.account.query.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.techbank.account.common.dto.AccountType;
import com.techbank.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BankAccount extends BaseEntity {

    @Id
    private String id;

    private String accountHolder;

    private Date createdDate;

    private AccountType accountType;

    private double balance;
}
