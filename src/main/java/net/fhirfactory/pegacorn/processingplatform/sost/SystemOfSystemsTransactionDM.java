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
package net.fhirfactory.pegacorn.processingplatform.sost;

import net.fhirfactory.pegacorn.common.model.FDNToken;
import net.fhirfactory.pegacorn.petasos.model.sost.SystemOfSystemsTransaction;
import net.fhirfactory.pegacorn.petasos.model.sost.SystemOfSystemsTransactionIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@ApplicationScoped
public class SystemOfSystemsTransactionDM {
    private static final Logger LOG = LoggerFactory.getLogger(SystemOfSystemsTransactionDM.class);

    private ConcurrentHashMap<SystemOfSystemsTransactionIdentifier, SystemOfSystemsTransaction> transactionSet;

    /**
     *
     * @param newTransaction
     */
    public void addTransaction(SystemOfSystemsTransaction newTransaction){
        LOG.debug(".addTransaction(): Entry, newTransaction (SystemOfSystemsTransaction) --> {}", newTransaction);

        LOG.debug(".addTransaction(): Exit");
    }

    /**
     *
     * @param transactionId
     * @return
     */
    public SystemOfSystemsTransaction getTransaction(SystemOfSystemsTransactionIdentifier transactionId){
        LOG.debug(".getTransaction(): Entry, transactionId (SystemOfSystemsTransactionIdentifier) --> {}", transactionId);
        SystemOfSystemsTransaction retrievedTransaction = null;

        LOG.debug(".getTransaction(): Exit, returning (SystemOfSystemsTransactionIdentifier) --> {}", retrievedTransaction);
        return(retrievedTransaction);
    }

    /**
     *
     * @param endpointId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<SystemOfSystemsTransaction> getTransactionSetOriginatingFromEndpoint(FDNToken endpointId, LocalDateTime startDate, LocalDateTime endDate){
        LOG.debug(".getTransactionSetOriginatingFromEndpoint(): Entry, endpointId(FDNToken) --> {}, startDate (LocalDateTime) --> {}, endDate (LocalDateTime) --> {}", endpointId, startDate, endDate);
        ArrayList<SystemOfSystemsTransaction> matchingTransactionSet = new ArrayList<SystemOfSystemsTransaction>();

        LOG.debug(".getTransactionSetOriginatingFromEndpoint(): Exit, number of Elements retrieved --> {}", matchingTransactionSet.size());
        return(matchingTransactionSet);
    }

    /**
     *
     * @param updatedTransaction
     */
    public void updateTransaction(SystemOfSystemsTransaction updatedTransaction){

    }

    /**
     *
     * @param transactionId
     */
    public void deleteTransaction(SystemOfSystemsTransactionIdentifier transactionId){

    }

}
