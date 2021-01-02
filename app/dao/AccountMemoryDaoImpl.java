package dao;

public class AccountMemoryDaoImpl implements  AccountDao{

    public AccountMemoryDaoImpl() {
        System.out.println("**AccountMemoryDaoImpl created");
    }

    @Override
    public String getAccount(Integer id) {
        return "Memory Account id " + id;
    }
}
