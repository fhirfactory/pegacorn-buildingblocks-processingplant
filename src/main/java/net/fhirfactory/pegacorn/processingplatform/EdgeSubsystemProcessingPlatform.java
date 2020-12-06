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
package net.fhirfactory.pegacorn.processingplatform;

import net.fhirfactory.pegacorn.common.model.FDN;
import net.fhirfactory.pegacorn.common.model.RDN;
import net.fhirfactory.pegacorn.petasos.model.processingplant.DefaultWorkshopSetEnum;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElement;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementFunctionToken;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementIdentifier;
import net.fhirfactory.pegacorn.petasos.model.topology.NodeElementTypeEnum;
import net.fhirfactory.pegacorn.processingplatform.common.StandardProcessingPlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EdgeSubsystemProcessingPlatform extends StandardProcessingPlatform {

    @Override
    protected void buildProcessingPlantWorkshops() {
        getLogger().debug(".buildProcessingPlantWorkshops(): Entry");
        if(getLogger().isTraceEnabled()) {
            getLogger().trace(".buildProcessingPlantWorkshops(): ProcessingPlant Identifier --> {}", this.getProcessingPlantNodeId());
            getLogger().trace(".buildProcessingPlantWorkshops(): ProcessingPlant NodeElement --> {}", this.getProcessingPlantNodeElement());
        }
        getLogger().trace(".buildProcessingPlantWorkshops(): 1st, the Interact Workshop");
        FDN interactFDN = new FDN(this.getProcessingPlantNodeId());
        interactFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.INTERACT_WORKSHOP.getWorkshop()));
        NodeElementIdentifier interactId = new NodeElementIdentifier(interactFDN.getToken());
        NodeElement interact = new NodeElement();
        interact.setVersion(this.getVersion());
        interact.setNodeInstanceID(interactId);
        FDN interactFunctionFDN = new FDN(this.getProcessingPlantNodeElement().getNodeFunctionID());
        interactFunctionFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.INTERACT_WORKSHOP.getWorkshop()));
        interact.setNodeFunctionID(interactFunctionFDN.getToken());
        interact.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        interact.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        interact.setInstanceInPlace(true);
        interact.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(interact);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), interact);
        getLogger().trace(".buildProcessingPlantWorkshops(): 2nd, the Transform");
        FDN transformFDN = new FDN(this.getProcessingPlantNodeId());
        transformFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.TRANSFORM_WORKSHOP.getWorkshop()));
        NodeElementIdentifier transfromId = new NodeElementIdentifier(transformFDN.getToken());
        NodeElement transform = new NodeElement();
        transform.setVersion(this.getVersion());
        transform.setNodeInstanceID(transfromId);
        FDN transformFunctionFDN = new FDN(this.getProcessingPlantNodeElement().getNodeFunctionID());
        transformFunctionFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.TRANSFORM_WORKSHOP.getWorkshop()));
        transform.setNodeFunctionID(transformFunctionFDN.getToken());
        transform.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        transform.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        transform.setInstanceInPlace(true);
        transform.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(transform);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), transform);
        getLogger().trace(".buildProcessingPlantWorkshops(): 3nd, the Edge");
        FDN edgeFDN = new FDN(this.getProcessingPlantNodeId());
        edgeFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.EDGE_WORKSHOP.getWorkshop()));
        NodeElementIdentifier edgeId = new NodeElementIdentifier(edgeFDN.getToken());
        NodeElement edge = new NodeElement();
        edge.setVersion(this.getVersion());
        edge.setNodeInstanceID(edgeId);
        FDN edgeFunctionFDN = new FDN(this.getProcessingPlantNodeElement().getNodeFunctionID());
        edgeFunctionFDN.appendRDN(new RDN(NodeElementTypeEnum.WORKSHOP.getNodeElementType(), DefaultWorkshopSetEnum.EDGE_WORKSHOP.getWorkshop()));
        edge.setNodeFunctionID(edgeFunctionFDN.getToken());
        edge.setConcurrencyMode(this.getProcessingPlantNodeElement().getConcurrencyMode());
        edge.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        edge.setResilienceMode(this.getProcessingPlantNodeElement().getResilienceMode());
        edge.setInstanceInPlace(true);
        edge.setContainingElementID(this.getProcessingPlantNodeId());
        this.getDeploymentIM().registerNode(edge);
        this.getDeploymentIM().addContainedNodeToNode(this.getProcessingPlantNodeId(), edge);
        getLogger().debug(".buildLadonWorkshops(): Exit");
    }
}
