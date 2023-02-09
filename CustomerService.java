package homework;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private TreeMap<Customer, String> customerData;

    CustomerService() {
        customerData = new TreeMap<>((customer1, customer2) -> customer1.getScores().compareTo(customer2.getScores()));
    }

    public void add(Customer customer, String data) {
        customerData.put(new Customer(customer), data);
    }

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        //return null; // это "заглушка, чтобы скомпилировать"
        return calculateCopyEntry(null);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return calculateCopyEntry(customer);
    }

    private Map.Entry<Customer, String> calculateCopyEntry(Customer customer) {
        Map.Entry<Customer, String> entry = getEntry(customer);
        Map.Entry<Customer, String> copyEntry = getCopyEntry(entry);
        replaceEntryInMap(entry, copyEntry);
        return copyEntry;
    }

    private Map.Entry<Customer, String> getEntry(Customer customer) {
        return (customer != null ? customerData.higherEntry(customer) : customerData.firstEntry());
    }

    private Map.Entry<Customer, String> getCopyEntry(Map.Entry<Customer, String> entry) {
        return (entry != null ? Map.Entry.copyOf(entry) : null);
    }

    private void replaceEntryInMap(Map.Entry<Customer, String> entry, Map.Entry<Customer, String> copyEntry) {
        if (entry != null) {
            customerData.remove(entry.getKey());
            customerData.put(new Customer(copyEntry.getKey()), copyEntry.getValue());
        }
    }
}