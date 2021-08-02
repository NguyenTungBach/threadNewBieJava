package reflection.generic;

import reflection.Customer;

public class DemoGeneric {
    public static void main(String[] args) {
        MasterModel<Customer> modelObj = new MasterModel<Customer>(Customer.class);
        // tạo bảng
//        modelObj.migrateData(Customer.class);
        // chèn bảng
        Customer customer = new Customer();
        customer.setName("Customer để xóa tiếp");
        customer.setIdentityNumber("A005");
        customer.setBalance(400);
        customer.setEmail("khach@gmail5.com");
//        modelObj.save(customer);
        // tìm đến tất cả
        modelObj.findAll(Customer.class);
        // update
//        Customer updateCustomer = new Customer();
//        updateCustomer.setName("Update A001");
//        updateCustomer.setIdentityNumber("A001");
//        updateCustomer.setBalance(500);
//        updateCustomer.setEmail("Updatekhach@gmail.com");
//        modelObj.update(updateCustomer);
        Customer deleteCustomer = new Customer();
        deleteCustomer.setIdentityNumber("A004");
//        modelObj.delete(deleteCustomer);
    }
}
