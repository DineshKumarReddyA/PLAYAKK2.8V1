package dao;

public class AccountMySqlImpl implements  AccountDao{
    @Override
    public String getAccount(Integer id) {
        return "MySql Account id " + id;
    }
}
