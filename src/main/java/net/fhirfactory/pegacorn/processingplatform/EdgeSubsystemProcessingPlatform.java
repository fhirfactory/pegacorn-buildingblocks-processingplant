package net.fhirfactory.pegacorn.processingplatform;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementIdentifier;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import net.fhirfactory.pegacorn.processingplatform.common.StandardProcessingPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EdgeSubsystemProcessingPlatform extends StandardProcessingPlatform {
    private static final Logger LOG = LoggerFactory.getLogger(EdgeSubsystemProcessingPlatform.class);

    @Override
    protected void buildProcessingPlantWorkshops() {
        LOG.debug(".buildProcessingPlantWorkshops(): Entry");
        LOG.trace(".buildProcessingPlantWorkshops(): 1st, the Interact Workshop");
        FDN interactFDN = new FDN(this.getProcessingPlantNodeId());
        interactFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), "Interact"));
        NodeElementIdentifier interactId = new NodeElementIdentifier(interactFDN.getToken());
        NodeElement interact = new NodeElement();
        interact.setVersion(this.getVersion());
        interact.setNodeInstanceID(interactId);
        interact.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        interact.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        interact.setInstanceInPlace(true);
        interact.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(interact);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), interact);
        LOG.trace(".buildProcessingPlantWorkshops(): 2nd, the Transform");
        FDN transformFDN = new FDN(this.getProcessingPlantNodeId());
        transformFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), "Transform"));
        NodeElementIdentifier transfromId = new NodeElementIdentifier(transformFDN.getToken());
        NodeElement transform = new NodeElement();
        transform.setVersion(this.getVersion());
        transform.setNodeInstanceID(transfromId);
        transform.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        transform.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        transform.setInstanceInPlace(true);
        transform.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(transform);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), transform);
        LOG.trace(".buildProcessingPlantWorkshops(): 3nd, the Edge");
        FDN edgeFDN = new FDN(this.getProcessingPlantNodeId());
        edgeFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), "Edge"));
        NodeElementIdentifier edgeId = new NodeElementIdentifier(edgeFDN.getToken());
        NodeElement edge = new NodeElement();
        edge.setVersion(this.getVersion());
        edge.setNodeInstanceID(edgeId);
        edge.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        edge.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        edge.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        edge.setInstanceInPlace(true);
        edge.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(edge);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), edge);
        LOG.debug(".buildLadonWorkshops(): Exit");
    }

}
