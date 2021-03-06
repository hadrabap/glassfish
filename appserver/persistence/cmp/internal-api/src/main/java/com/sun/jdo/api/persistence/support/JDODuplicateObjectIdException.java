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

/*
 * JDODuplicateObjectIdException.java
 *
 * Created on May 06, 2002
 */

package com.sun.jdo.api.persistence.support;

/** JDODuplicateObjectIdException is thrown in case this PersistenceManager
 * has another instance with the same Object Id in its cache.
 *
 * @author  Marina Vatkina
 * @version 0.1
 */
public class JDODuplicateObjectIdException extends JDOUserException
{
    /**
     * Creates a new <code>JDODuplicateObjectIdException</code> without detail message.
     */
    public JDODuplicateObjectIdException() 
    {
    }

    /**
     * Constructs a new <code>JDODuplicateObjectIdException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JDODuplicateObjectIdException(String msg) 
    {
        super(msg);
    }
    
    /**
      * Constructs a new <code>JDODuplicateObjectIdException</code> with the specified detail message
      * and nested Exception.
      * @param msg the detail message.
      * @param nested the nested <code>Exception</code>.
      */
    public JDODuplicateObjectIdException(String msg, Exception nested) 
    {
        super(msg, nested);
    }

    /** Constructs a new <code>JDODuplicateObjectIdException</code> with the specified detail message
     * and failed object array.
     * @param msg the detail message.
     * @param failed the failed object array.
     */
    public JDODuplicateObjectIdException(String msg, Object[] failed) {
        super(msg, failed);
    }
 
    /** Constructs a new <code>JDODuplicateObjectIdException</code> with the specified detail message,
     * nested exception, and failed object array.
     * @param msg the detail message.
     * @param nested the nested <code>Exception</code>.
     * @param failed the failed object array.
     */
    public JDODuplicateObjectIdException(String msg, Exception nested, Object[] failed) {
        super(msg, nested, failed);
    }
}
