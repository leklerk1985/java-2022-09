package homework;

import java.util.LinkedList;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private LinkedList<Customer> customers;

    CustomerReverseOrder() {
        customers = new LinkedList<>();
    }

    public void add(Customer customer) {
        customers.push(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}
