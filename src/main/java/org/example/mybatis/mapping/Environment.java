package org.example.mybatis.mapping;

import lombok.Builder;
import lombok.Data;
import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

@Builder
@Data
public class Environment {
    private String id;

    private TransactionFactory transactionFactory;

    private DataSource dataSource;

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }
}
