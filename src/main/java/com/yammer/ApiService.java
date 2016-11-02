package com.yammer;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * Created by eduardol on 30/10/2016.
 */
public class ApiService extends Service<ServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new ApiService().run(args);
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {

    }

    @Override
    public void run(ServiceConfiguration serviceConfiguration, Environment environment) throws Exception {
        environment.addResource(new ApiResource());
        environment.addResource(new DefaultResource());
    }
}
