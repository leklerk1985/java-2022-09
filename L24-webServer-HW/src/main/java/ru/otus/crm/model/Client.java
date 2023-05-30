package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*; // было: import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name, Address address) {
        this.id = null;
        this.name = name;
        this.address = address;
    }

    public Client(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;

        setClientInAddressAndPhones(this, this.address, this.phones);
    }

    public Client(String name, Address address, List<Phone> phones) {
        this.id = null;
        this.name = name;
        this.address = address;
        this.phones = phones;

        setClientInAddressAndPhones(this, this.address, this.phones);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public Client clone() {
        var addressCloned = new Address(
                this.address != null ? this.address.getId() : null,
                this.address != null ? this.address.getStreet() : null);

        var phonesCloned = new ArrayList<Phone>();
        if (this.phones != null) {
            for (var phone: this.phones) {
                var phoneCloned = new Phone(phone.getId(), phone.getNumber());
                phonesCloned.add(phoneCloned);
            }
        }

        var clientCloned = new Client(this.id, this.name, addressCloned, phonesCloned);
        setClientInAddressAndPhones(clientCloned, clientCloned.address, clientCloned.phones);

        return clientCloned;
    }

    private void setClientInAddressAndPhones(Client c, Address a, List<Phone> p) {
        a.setClient(c);
        for (var phone: p) {
            phone.setClient(c);
        }
    }

    public String getPhonesAsString() {
        String result = "";
        for (var phone: phones) {
            result += phone.getNumber() + ", ";
        }
        return result.substring(0, result.length()-2);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}