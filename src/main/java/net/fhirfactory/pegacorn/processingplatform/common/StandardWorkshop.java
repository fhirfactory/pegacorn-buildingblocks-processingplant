/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.pegacorn.processingplatform.common;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.deployment.topology.manager.DeploymentTopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.map.model.DeploymentTopologyInitialisationInterface;
import net.fhirfactory.pegacorn.petasos.model.processingplant.ProcessingPlantServicesInterface;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementFunctionToken;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementIdentifier;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class StandardWorkshop extends RouteBuilder {

    private NodeElement node;
    private NodeElementIdentifier nodeId;
    private NodeElementFunctionToken nodeFunctionToken;
    private boolean isInitialised;
    private String version;

    @Inject
    private DeploymentTopologyIM deploymentIM;

    @Inject
    private DeploymentTopologyInitialisationInterface topologyBuilder;

    @Inject
    ProcessingPlantServicesInterface processingPlant;

    public StandardWorkshop() {
        super();
        this.isInitialised = false;
    }

    abstract protected Logger getLogger();

    abstract protected String specifyWorkshopName();

    abstract protected String specifyWorkshopVersion();

    abstract protected void invokePostConstructInitialisation();

    @PostConstruct
    private void initialise() {
        if (!isInitialised) {
            getLogger().debug("StandardWorkshop::initialise(): Invoked!");
            topologyBuilder.initialiseDeploymentTopology();
            processingPlant.initialisePlant();
            getLogger().trace("StandardWorkshop::initialise(): Topology initialised, specifying NodeFunction");
            setNodeFunctionToken(buildFunctionToken());
            getLogger().trace("StandardWorkshop::initialise(): NodeFunctionToken --> {}", getNodeFunctionToken());
            setVersion(specifyWorkshopVersion());
            getLogger().trace("StandardWorkshop::initialise(): ProcessingPlant Version --> {}", getVersion());
            buildWorkshop();

            invokePostConstructInitialisation();
            getLogger().trace("StandardWorkshop::initialise(): Node --> {}", getNode());
            isInitialised = true;
        }
    }

    private void buildWorkshop() {
        getLogger().debug(".buildWorkshop(): Entry, adding Workshop --> {}, version --> {}", specifyWorkshopName(), specifyWorkshopVersion());
        FDN workshopFDN = new FDN(processingPlant.getProcessingPlantNodeId());
        workshopFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), specifyWorkshopName()));
        NodeElementIdentifier workshopIdentifier = new NodeElementIdentifier(workshopFDN.getToken());
        NodeElement workshopNode = new NodeElement();
        workshopNode.setVersion(specifyWorkshopVersion());
        workshopNode.setNodeInstanceID(workshopIdentifier);
        workshopNode.setNodeFunctionID(getNodeFunctionToken().getFunctionID());
        workshopNode.setConcurrencyMode(processingPlant.getProcessingPlantNodeElement().getConcurrencyMode());
        workshopNode.setResilienceMode(processingPlant.getProcessingPlantNodeElement().getResilienceMode());
        workshopNode.setInstanceInPlace(true);
        workshopNode.setContainingElementID(processingPlant.getProcessingPlantNodeId());
        deploymentIM.registerNode(workshopNode);
        deploymentIM.addContainedNodeToNode(processingPlant.getProcessingPlantNodeId(), workshopNode);
        setNode(workshopNode);
    }

    private NodeElementFunctionToken buildFunctionToken(){
        NodeElementFunctionToken processingPlantFunctionToken = processingPlant.getProcessingPlantNodeElement().getNodeFunctionToken();
        FDN workshopFDN = new FDN(processingPlantFunctionToken.getFunctionID());
        workshopFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), specifyWorkshopName() ));
        NodeElementFunctionToken workshopFunctionToken = new NodeElementFunctionToken();
        workshopFunctionToken.setFunctionID(workshopFDN.getToken());
        workshopFunctionToken.setVersion(specifyWorkshopVersion());
        return(workshopFunctionToken);
    }

    public NodeElement getNode() {
        return node;
    }

    public void setNode(NodeElement node) {
        this.node = node;
    }

    public NodeElementIdentifier getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeElementIdentifier nodeId) {
        this.nodeId = nodeId;
    }

    public NodeElementFunctionToken getNodeFunctionToken() {
        return nodeFunctionToken;
    }

    public void setNodeFunctionToken(NodeElementFunctionToken nodeFunctionToken) {
        this.nodeFunctionToken = nodeFunctionToken;
    }

    public boolean isInitialised() {
        return isInitialised;
    }

    public void setInitialised(boolean initialised) {
        isInitialised = initialised;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void configure() throws Exception {
        String fromString = "timer://" +specifyWorkshopName() + "-ingres" + "?repeatCount=1";

        from(fromString)
        .log(LoggingLevel.DEBUG, "Response Content --> ${body}");
    }
}
