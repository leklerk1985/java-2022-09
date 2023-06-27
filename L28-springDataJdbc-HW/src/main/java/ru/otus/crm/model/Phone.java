package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

@NoArgsConstructor
@Table("phone")
public class Phone {
    @Id
    private Long id;

    private String number;

    @Nonnull
    private Integer orderColumn;

    public Phone(String number, int orderColumn) {
        this(null, number, orderColumn);
    }

    @PersistenceCreator
    public Phone(Long id, String number, int orderColumn) {
        this.id = id;
        this.number = number;
        this.orderColumn = orderColumn;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
