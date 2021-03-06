/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.tools.verifier.tests.ejb.session;

import com.sun.enterprise.deployment.MethodDescriptor;
import com.sun.enterprise.tools.verifier.Result;
import com.sun.enterprise.tools.verifier.Verifier;
import com.sun.enterprise.tools.verifier.VerifierTestContext;
import com.sun.enterprise.tools.verifier.tests.ComponentNameConstructor;
import com.sun.enterprise.tools.verifier.tests.ejb.EjbCheck;
import com.sun.enterprise.tools.verifier.tests.ejb.EjbTest;
import org.glassfish.ejb.deployment.descriptor.ContainerTransaction;
import org.glassfish.ejb.deployment.descriptor.EjbDescriptor;
import org.glassfish.ejb.deployment.descriptor.EjbSessionDescriptor;

import java.util.Enumeration;

/**
 * Optionally implemented SessionSynchronization interface transaction 
 * demarcation test.
 * If an enterprise bean implements the javax.ejb.SessionSynchronization
 * interface, the Application Assembler can specify only the following values
 * for the transaction attributes of the bean's methods:
 *   Required
 *   RequiresNew
 *   Mandatory
 */
public class TransactionDemarcationSessionSynchronizationInterface extends EjbTest implements EjbCheck {


    /**
     * Optionally implemented SessionSynchronization interface transaction 
     * demarcation test.
     * If an enterprise bean implements the javax.ejb.SessionSynchronization
     * interface, the Application Assembler can specify only the following values
     * for the transaction attributes of the bean's methods:
     *   Required
     *   RequiresNew
     *   Mandatory
     *
     * @param descriptor the Enterprise Java Bean deployment descriptor
     *   
     * @return <code>Result</code> the results for this assertion
     */
    public Result check(EjbDescriptor descriptor) {

        Result result = getInitializedResult();
        ComponentNameConstructor compName = getVerifierContext().getComponentNameConstructor();
        boolean oneFound = false;

        if (descriptor instanceof EjbSessionDescriptor) {
            try {
                VerifierTestContext context = getVerifierContext();
                ClassLoader jcl = context.getClassLoader();
                Class c = Class.forName(descriptor.getEjbClassName(), false, getVerifierContext().getClassLoader());
                // walk up the class tree
                do {
                    Class[] interfaces = c.getInterfaces();

                    for (int i = 0; i < interfaces.length; i++) {
                        if (interfaces[i].getName().equals("javax.ejb.SessionSynchronization")) {
                            oneFound = true;
                            break;
                        }
                    }
                } while ((c=c.getSuperclass()) != null);

            } catch (ClassNotFoundException e) {
                Verifier.debug(e);
                addErrorDetails(result, compName);
                result.failed(smh.getLocalString
                        (getClass().getName() + ".failedException1",
                                "Error: [ {0} ] class not found.",
                                new Object[] {descriptor.getEjbClassName()}));
                return result;
            }

            // If an enterprise bean implements the javax.ejb.SessionSynchronization
            // interface, the Application Assembler can specify only the following
            // values for the transaction attributes of the bean's methods:
            //   Required, RequiresNew, Mandatory
            if (oneFound) {
                String transactionAttribute = "";
                ContainerTransaction containerTransaction = null;
                boolean oneFailed = false;
                if (!descriptor.getMethodContainerTransactions().isEmpty()) {
                    for (Enumeration ee = descriptor.getMethodContainerTransactions().keys(); ee.hasMoreElements();) {
                        MethodDescriptor methodDescriptor = (MethodDescriptor) ee.nextElement();
                        containerTransaction =
                                (ContainerTransaction) descriptor.getMethodContainerTransactions().get(methodDescriptor);

                        if (!(containerTransaction != null && properAttribDefined(containerTransaction))) {
                            transactionAttribute  =
                                    containerTransaction.getTransactionAttribute();
                            addErrorDetails(result, compName);
                            result.failed(smh.getLocalString
                                    (getClass().getName() + ".failed",
                                            "Error: TransactionAttribute [ {0} ] for method [ {1} ] is not valid.",
                                            new Object[] {transactionAttribute, methodDescriptor.getName()}));

                        }
                    }
                }
            }
        }

        if(result.getStatus()!=Result.FAILED) {
            addGoodDetails(result, compName);
            result.passed(smh.getLocalString
                    (getClass().getName() + ".passed",
                            "TransactionAttributes are defined properly for the bean"));
        }
        return result;
    }

    private boolean properAttribDefined(ContainerTransaction containerTransaction) {
        String transactionAttribute  = containerTransaction.getTransactionAttribute();
        return (ContainerTransaction.REQUIRED.equals(transactionAttribute)
                || ContainerTransaction.REQUIRES_NEW.equals(transactionAttribute)
                || ContainerTransaction.MANDATORY.equals(transactionAttribute));

    }
}
