package dao;

public class AccountPGImpl implements  AccountDao{
    @Override
    public String getAccount(Integer id) {
        return "PG Account id " + id;
    }
}
