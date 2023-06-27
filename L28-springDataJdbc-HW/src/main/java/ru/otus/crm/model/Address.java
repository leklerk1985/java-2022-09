package ru.otus.crm.model;

import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

@NoArgsConstructor
@Table("address")
public class Address {
    @Id
    private Long id;

    private String street;

    @PersistenceCreator
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this(null, street);
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return street;
    }
}
