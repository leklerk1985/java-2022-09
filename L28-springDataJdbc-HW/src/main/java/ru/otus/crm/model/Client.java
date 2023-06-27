package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.annotation.PersistenceCreator;

@Getter
@Setter
@NoArgsConstructor
@Table("client")
public class Client {
    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "order_column")
    private List<Phone> phones;

    public Client(String name) {
        this(null, name);
    }

    @PersistenceCreator
    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name, Address address) {
        this(null, name, address);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Client(String name, Address address, List<Phone> phones) {
        this(null, name, address, phones);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
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