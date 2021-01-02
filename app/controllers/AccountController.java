package controllers;

import dao.AccountDao;
import play.mvc.*;
import javax.inject.Inject;
import javax.inject.Named;

public class AccountController extends  Controller {
    // should not inject impl, that will become hardcoding
    //mysql

    AccountDao accountDao; // default binding, linked to AccountMemoryDaoImpl/refer BinderModule

    @Inject
    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Inject
    @Named("memory")
    AccountDao memoryAccountDao;

    @Inject
    @Named("pg")
    AccountDao pgAccountDao;

    @Inject
    @Named("mysql")
    AccountDao mysqlAccountDao;

    // default memory
    public Result getAccount(Integer id) {
        return  ok(accountDao.getAccount(id));
    }

    public Result getAccountMemory(Integer id) {
        return  ok(memoryAccountDao.getAccount(id));
    }

    public Result getAccountMySql(Integer id) {
        return  ok(mysqlAccountDao.getAccount(id));
    }

    public Result getAccountPG(Integer id) {
        return  ok(pgAccountDao.getAccount(id));
    }


}
