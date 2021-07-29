package reflection.generic;

import reflection.Customer;

public class DemoGeneric {
    public static void main(String[] args) {
        MasterModel<Customer> modelObj = new MasterModel<>();
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setIdentityNumber("A001");
        customer.setBalance(200);
        customer.setEmail("khach@gmail.com");
        modelObj.save(customer);
    }
}
