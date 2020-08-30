package net.fhirfactory.pegacorn.processingplatform.common;

import net.fhirfactory.pegacorn.deployment.topology.manager.DeploymentTopologyIM;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import org.apache.camel.builder.RouteBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class StandardProcessingPlatform extends RouteBuilder {

    private NodeElement myNodeElement;

    @Inject
    private DeploymentTopologyIM deploymentIM;


    @PostConstruct
    public void initialise(){

    }

    public abstract String specifyProcessingPlantName();
    public abstract String specifyProcessingPlantVersion();
    public abstract String specifySite();
    public abstract String specifyPlatform();

    public DeploymentTopologyIM getDeploymentIM(){
        return(deploymentIM);
    }




}
