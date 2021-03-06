/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 */
package org.glassfish.admin.mejb.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.rmi.PortableRemoteObject;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import javax.management.j2ee.ManagementHome;

import com.sun.enterprise.security.ee.auth.login.ProgrammaticLogin;
import org.glassfish.external.amx.AMXGlassfish;

import javax.management.j2ee.Management;


import org.junit.Ignore;

/**
    Standalone MEJB test -- requires running server and disabling security on MEJB.
    
export V3M=/v3/glassfish/modules
export MAIN=org.glassfish.admin.mejb.test.MEJBTest
java -cp $V3M/gf-client.jar:$V3M/javax.management.j2ee.jar:target/MEJB.jar $MAIN

 */
@Ignore
public class MEJBTest {    
    private final Management mMEJB;
    
    public MEJBTest( final Management mejb )
    {
        mMEJB = mejb;
    }
    
    private void test() {
        try
        {
            _test();
        }
        catch( final Exception e)
        {
            e.printStackTrace();
        }
        println( "DONE with MEJB test." );
    }
    
    
    private void testMBean( final ObjectName objectName)
        throws Exception
    {
        println( "" );
        println( "" + objectName );
        
        final Management mejb = mMEJB;
        final MBeanInfo info = mejb.getMBeanInfo(objectName);
        final String[] attrNames = getAttributeNames( info.getAttributes() );
        
        println( "attributes: " + toString( newListFromArray(attrNames), ", " ) );
        
        final AttributeList list = mejb.getAttributes( objectName, attrNames );
        
        for( final String attrName : attrNames)
        {
            try
            {
                final Object value = mejb.getAttribute( objectName, attrName );
            }
            catch( Exception e )
            {
                println( "Attribute failed: " + attrName );
            }
        }
    }
    
    private void _test()
        throws Exception
    {
        final Management mejb = mMEJB;
        
        final String defaultDomain = mejb.getDefaultDomain();
        println("MEJB default domain = " + defaultDomain + ", MBeanCount = " + mejb.getMBeanCount() );
        
        final String domain = AMXGlassfish.DEFAULT.amxJMXDomain();
        final ObjectName pattern = newObjectName( domain + ":*" );
        final Set<ObjectName> items = mejb.queryNames( pattern, null);
        println("Queried " + pattern + ", got mbeans: " + items.size() );
        for( final ObjectName objectName : items )
        {
            if ( mejb.isRegistered(objectName) )
            {
                testMBean(objectName);
            }
        }
        
        // add listeners to all
        println( "Listener are not supported, skipping." );
        /*
        println( "Adding listeners to every MBean..." );
        
        final ListenerRegistration reg = mejb.getListenerRegistry();
        println( "Got ListenerRegistration: " + reg );
        final NotificationListener listener = new NotifListener();
        for( final ObjectName objectName : items )
        {
            if ( mejb.isRegistered(objectName) )
            {
                final NotificationFilter filter = null;
                final Object handback = null;
                try {
                    reg.addNotificationListener( objectName, listener, filter, handback );
                }
                catch( final Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
        */
    }
    
    
    private static final class NotifListener implements NotificationListener
    {
        public NotifListener()
        {
        }
        
        public void handleNotification( final Notification notif, final Object handback )
        {
            System.out.println( "NotifListener: " + notif);
        }
    }

		public static String
	toString(
		final Collection<?> c,
		final String	 delim )
	{
        final StringBuffer buf = new StringBuffer();
        
        for( final Object item : c )
        {
            buf.append( "" + item );
            buf.append( delim );
        }
        if( c.size() != 0)
        {
            buf.setLength( buf.length() - delim.length() );
        }
        
        return buf.toString();
    }
            
		public static <T> List<T>
	newListFromArray( final T []  items )
	{
		final List<T>	list	= new ArrayList<T>();
		
		for( int i = 0; i < items.length; ++i )
		{
			list.add( items[ i ] );
		}

		return( list );
	}


	public static String []
	getAttributeNames( final MBeanAttributeInfo[]	infos  )
	{
		final String[]	names	= new String[ infos.length ];
		
		for( int i = 0; i < infos.length; ++i )
		{
			names[ i ]	= infos[ i ].getName();
		}
		
		return( names );
	}

    static ObjectName
	newObjectName( final String name )
	{
		try
		{
			return( new ObjectName( name ) ); 
		}
		catch( Exception e )
		{
			throw new RuntimeException( e.getMessage(), e );
		}
	}



public static void main(String[] args) {
    try {
        final String mejbName = "java:global/mejb/MEJBBean";
        final String username = "admin";
        final String password = "";
        final String realm = "admin-realm";
        System.out.println( "Authenticating with \"" + username + "\", \"" + password + "\"");
        
        final ProgrammaticLogin pm = new ProgrammaticLogin();
        pm.login( username, password, realm, true);

        println("Looking up: " + mejbName);
        final InitialContext initial = new InitialContext();
        final Object objref = initial.lookup(mejbName);

        final ManagementHome home = (ManagementHome) PortableRemoteObject.narrow(objref, ManagementHome.class);
        try
        {
            final ManagementHome home2 = (ManagementHome)objref;
        }
        catch( final Exception e )
        {
            println("WARNING: (ManagementHome)PortableRemoteObject.narrow(objref, ManagementHome.class) works, but (ManagementHome)objref does not!" );
        }
        
        //println("ManagementHome: " + home + " for " + mejbName);
        final Management mejb = (Management)home.create();
        println("Got the MEJB");

        new MEJBTest( mejb ).test();

        println( "Calling mejb.remove()" );
        mejb.remove();

    } catch (Exception ex) {
        System.err.println("Caught an unexpected exception!");
        ex.printStackTrace();
    }
    println( "Exiting main() forcibly" );
    System.exit( -1 );
}

    private static final void println(final Object o) {
        System.out.println("" + o);
    }
}
