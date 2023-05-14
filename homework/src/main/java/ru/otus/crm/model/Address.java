package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "street")
    private String street;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    public Address(String street, Client client) {
        this.id = null;
        this.street = street;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
