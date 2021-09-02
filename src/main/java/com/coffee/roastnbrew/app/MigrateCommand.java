package com.coffee.roastnbrew.app;

import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.mysql.cj.jdbc.MysqlDataSource;
import io.dropwizard.Application;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.flywaydb.core.Flyway;

@Slf4j
public class MigrateCommand extends ConfiguredCommand<CoffeeConfig> {
    
    private final Class<CoffeeConfig> configClass;
    
    MigrateCommand(Application<CoffeeConfig> application) {
        super("migrate", "Runs flyway db migration");
        this.configClass = application.getConfigurationClass();
    }
    
    @Override
    protected Class<CoffeeConfig> getConfigurationClass() {
        return configClass;
    }
    
    @Override
    protected void run(Bootstrap<CoffeeConfig> bootstrap, Namespace namespace, CoffeeConfig config) throws Exception {
        try {
            Flyway flyway = new Flyway();
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setURL(config.getDbURL());
            flyway.setDataSource(mysqlDataSource);
            
            String action = namespace.getString("action");
            switch (action) {
                case "migrate":
                    flyway.migrate();
                    break;
                case "info":
                    flyway.info();
                    break;
                case "repair":
                    flyway.repair();
                    break;
                default:
                    throw new CoffeeException("Invalid command: " + action);
            }
    
            log.info("ran " + action + " successfully!");
            System.exit(0);
        } catch (Exception e) {
            log.error("Failed to run migrate command", e);
            System.exit(1);
        }
    }
    
    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        subparser.addArgument("-a", "--action")
            .dest("action")
            .type(String.class)
            .required(true)
            .help("action to perform migrate, info or repair");
    }
}
