package net.fhirfactory.pegacorn.platform.edge.ask;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import net.fhirfactory.pegacorn.deployment.topology.manager.ServiceModuleTopologyProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class LadonProxy {
    private static final Logger LOG = LoggerFactory.getLogger(LadonProxy.class);

    FhirContext r4Context;
    String ladonAnswerEndpoint;
    IGenericClient ladonClient;

    @Inject
    ServiceModuleTopologyProxy topologyProxy;

    @PostConstruct
    public void initialise(){
        this.r4Context = FhirContext.forR4();
        this.ladonAnswerEndpoint = buildLadonAnswerEndpoint();
        this.ladonClient = r4Context.newRestfulGenericClient(getLadonAnswerEndpoint());
    }


    protected String buildLadonAnswerEndpoint(){
        String endpointString = new String();

        return(endpointString);
    }

    public String getLadonAnswerEndpoint(){
        return(this.ladonAnswerEndpoint);
    }

    public IGenericClient getLadonClient(){
        return(this.ladonClient);
    }

}
