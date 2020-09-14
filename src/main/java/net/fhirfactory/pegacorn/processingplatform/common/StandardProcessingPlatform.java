package net.fhirfactory.pegacorn.processingplatform.common;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.deployment.properties.PegacornCoreSubsystemComponentNames;
import net.fhirfactory.pegacorn.deployment.topology.manager.DeploymentTopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.map.model.DeploymentTopologyInitialisationInterface;
import net.fhirfactory.pegacorn.petasos.model.processingplant.DefaultWorkshopSetEnum;
import net.fhirfactory.pegacorn.petasos.model.processingplant.ProcessingPlantServicesInterface;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementFunctionToken;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementIdentifier;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class StandardProcessingPlatform extends RouteBuilder implements ProcessingPlantServicesInterface {
    private static final Logger LOG = LoggerFactory.getLogger(StandardProcessingPlatform.class);

    private NodeElement node;
    private NodeElementIdentifier nodeId;
    private NodeElementFunctionToken nodeToken;
    private boolean isInitialised;
    private String version;

    @Inject
    private DeploymentTopologyIM deploymentIM;

    @Inject
    private DeploymentTopologyInitialisationInterface topologyBuilder;

    @Inject
    private PegacornCoreSubsystemComponentNames subsystemComponentNames;

    public StandardProcessingPlatform() {
        super();
        this.isInitialised = false;
    }

    @PostConstruct
    private void initialise() {
        if (!isInitialised) {
            LOG.debug("StandardProcessingPlatform::initialise(): Invoked!");
            topologyBuilder.initialiseDeploymentTopology();
            LOG.trace("StandardProcessingPlatform::initialise(): Topology initialised, specifying node");
            specifyNode();
            LOG.trace("StandardProcessingPlatform::initialise(): Node --> {}", this.getProcessingPlantNodeElement());
            specifyNodeElementIdentifier();
            LOG.trace("StandardProcessingPlatform::initialise(): NodeIdentifier --> {}", this.getProcessingPlantNodeId());
            specifyNodeElementFunctionToken();
            LOG.trace("StandardProcessingPlatform::initialise(): NodeFunctionToken --> {}", this.getNodeToken());
            this.version = specifyProcessingPlantVersion();
            LOG.trace("StandardProcessingPlatform::initialise(): ProcessingPlant Version --> {}", this.getVersion());
            buildProcessingPlantWorkshops();
            isInitialised = true;
        }
    }

    @Override
    public void initialisePlant() {
        initialise();
    }

    abstract protected String specifyProcessingPlantName();

    abstract protected String specifyProcessingPlantVersion();

    abstract protected String specifySite();

    abstract protected String specifyPlatform();

    abstract protected void buildProcessingPlantWorkshops();

    private void specifyNode() {
        LOG.debug(".specifyNode(): Entry");
        this.node = deploymentIM.getNode(specifyProcessingPlantName(), NodeElementTypeEnum.PROCESSING_PLANT, specifyProcessingPlantVersion());
        LOG.debug(".specifyNode(): Exit, node (NodeElement) --> {}", this.getProcessingPlantNodeElement());
    }

    private void specifyNodeElementFunctionToken() {
        LOG.debug(".getNodeElementFunctionToken(): Entry");
        this.nodeToken = this.getProcessingPlantNodeElement().getNodeFunctionToken();
        LOG.debug(".getNodeElementFunctionToken(): Exit, nodeFunction (NodeElementFunctionToken) --> {}", this.getNodeToken());
    }

    private void specifyNodeElementIdentifier() {
        LOG.debug("getNodeElementIdentifier(): Entry");
        this.nodeId = this.getProcessingPlantNodeElement().getNodeInstanceID();
        LOG.debug("getNodeElementIdentifier(): Exit, nodeId (NodeElementIdentifier) --> {}", this.getProcessingPlantNodeId());
    }

    public DeploymentTopologyIM getDeploymentIM() {
        return (deploymentIM);
    }

    public DeploymentTopologyInitialisationInterface getTopologyBuilder() {
        return (this.topologyBuilder);
    }

    public PegacornCoreSubsystemComponentNames getSubsystemComponentNames() {
        return (this.subsystemComponentNames);
    }

    public NodeElementFunctionToken getNodeToken() {
        return (this.nodeToken);
    }

    public String getVersion() {
        return (this.version);
    }

    @Override
    public void configure() throws Exception {

    }

    @Override
    public NodeElement getProcessingPlantNodeElement() {
        return (this.node);
    }

    @Override
    public NodeElementIdentifier getProcessingPlantNodeId() {
        return (this.nodeId);
    }

    @Override
    public NodeElement getWorkshop(String workshopName) {
        boolean found = false;
        NodeElementIdentifier foundNodeId = null;
        for (NodeElementIdentifier containedNode : this.getProcessingPlantNodeElement().getContainedElements()) {
            FDN nodeFDN = new FDN(containedNode);
            if (nodeFDN.getUnqualifiedRDN().getValue().contentEquals(workshopName)) {
                found = true;
                foundNodeId = containedNode;
                break;
            }
        }
        if (found) {
            NodeElement foundNode = deploymentIM.getNode(foundNodeId);
            return (foundNode);
        }
        return (null);
    }
}
