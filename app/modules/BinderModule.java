package modules;

import com.google.inject.AbstractModule;
import dao.AccountDao;
import dao.AccountMemoryDaoImpl;

import com.google.inject.name.Names;
import dao.AccountMySqlImpl;
import dao.AccountPGImpl;

// for DI, associating interface to implementation
// used to initialize the application startup/configuration/connections
public class BinderModule extends  AbstractModule {
    public BinderModule() {
        System.out.println("**BinderModule created");
    }

    protected  void configure() {
        System.out.println("**Configuring DI");
        // binding interface to class implementation, lazy creation/binding only on need basic
        // someone need to inject
      //  bind(AccountDao.class).to(AccountMemoryDaoImpl.class);

        // created early at bootstrap time
        // when it will be useful?
        // Data connectors, DB is not there, why to start the application
        bind(AccountDao.class).to(AccountMemoryDaoImpl.class).asEagerSingleton();

        // named injections
        // based on multiple implementations
        // very specific names
        // can be injected with
        // @Inject
        // @Named("memory") or // @Named("mysql") or // @Named("pg")
        bind(AccountDao.class).annotatedWith(Names.named("memory")).to(AccountMemoryDaoImpl.class);
        bind(AccountDao.class).annotatedWith(Names.named("mysql")).to(AccountMySqlImpl.class);
        bind(AccountDao.class).annotatedWith(Names.named("pg")).to(AccountPGImpl.class);
    }
}
