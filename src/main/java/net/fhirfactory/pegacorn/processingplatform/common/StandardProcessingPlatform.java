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
import net.fhirfactory.pegacorn.deployment.names.PegacornLadonComponentNames;
import net.fhirfactory.pegacorn.deployment.topology.manager.DeploymentTopologyIM;
import net.fhirfactory.pegacorn.deployment.topology.map.model.DeploymentTopologyInitialisationInterface;
import net.fhirfactory.pegacorn.petasos.model.processingplant.ProcessingPlantServicesInterface;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementFunctionToken;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementIdentifier;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class StandardProcessingPlatform extends RouteBuilder implements ProcessingPlantServicesInterface {

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
    private PegacornLadonComponentNames subsystemComponentNames;

    public StandardProcessingPlatform() {
        super();
        this.isInitialised = false;
    }

    @PostConstruct
    private void initialise() {
        if (!isInitialised) {
            getLogger().debug("StandardProcessingPlatform::initialise(): Invoked!");
            topologyBuilder.initialiseDeploymentTopology();
            getLogger().trace("StandardProcessingPlatform::initialise(): Topology initialised, specifying node");
            specifyNode();
            getLogger().trace("StandardProcessingPlatform::initialise(): Node --> {}", this.getProcessingPlantNodeElement());
            specifyNodeElementIdentifier();
            getLogger().trace("StandardProcessingPlatform::initialise(): NodeIdentifier --> {}", this.getProcessingPlantNodeId());
            specifyNodeElementFunctionToken();
            getLogger().trace("StandardProcessingPlatform::initialise(): NodeFunctionToken --> {}", this.getNodeToken());
            this.version = specifyProcessingPlantVersion();
            getLogger().trace("StandardProcessingPlatform::initialise(): ProcessingPlant Version --> {}", this.getVersion());
            buildProcessingPlantWorkshops();
            isInitialised = true;
        }
    }

    @Override
    public void initialisePlant() {
        initialise();
    }

    abstract protected Logger getLogger();

    abstract protected String specifyProcessingPlantName();

    abstract protected String specifyProcessingPlantVersion();

    abstract protected String specifySite();

    abstract protected String specifyPlatform();

    abstract protected void buildProcessingPlantWorkshops();

    private void specifyNode() {
        getLogger().debug(".specifyNode(): Entry");
        this.node = deploymentIM.getNode(specifyProcessingPlantName(), NodeElementTypeEnum.PROCESSING_PLANT, specifyProcessingPlantVersion());
        getLogger().debug(".specifyNode(): Exit, node (NodeElement) --> {}", this.getProcessingPlantNodeElement());
    }

    private void specifyNodeElementFunctionToken() {
        getLogger().debug(".getNodeElementFunctionToken(): Entry");
        this.nodeToken = this.getProcessingPlantNodeElement().getNodeFunctionToken();
        getLogger().debug(".getNodeElementFunctionToken(): Exit, nodeFunction (NodeElementFunctionToken) --> {}", this.getNodeToken());
    }

    private void specifyNodeElementIdentifier() {
        getLogger().debug("getNodeElementIdentifier(): Entry");
        this.nodeId = this.getProcessingPlantNodeElement().getNodeInstanceID();
        getLogger().debug("getNodeElementIdentifier(): Exit, nodeId (NodeElementIdentifier) --> {}", this.getProcessingPlantNodeId());
    }

    public DeploymentTopologyIM getDeploymentIM() {
        return (deploymentIM);
    }

    public DeploymentTopologyInitialisationInterface getTopologyBuilder() {
        return (this.topologyBuilder);
    }

    public PegacornLadonComponentNames getSubsystemComponentNames() {
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
        getLogger().info(".getWorkshop(): Entry, workshopNmae --> {}", workshopName);
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
