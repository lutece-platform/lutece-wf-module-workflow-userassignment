/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.userassignment.web.task;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.userassignment.service.task.IAssignUserResourceTaskService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

/**
 * Validation of the task form that assigns a user to a resource
 */
public class AssignUserResourceTaskComponentTest extends LuteceTestCase
{
    private static final String PARAMETER_USER_ID = "user_selection_id";
    private static final String RESOURCE_TYPE = "FORMS_FORM_RESPONSE";

    /**
     * Without any assignable user, the action is refused with a message instead of failing silently in the task
     */
    @Test
    public void testRefusedWithoutAssignableUser( )
    {
        AssignUserResourceTaskComponent component = newComponent( Collections.emptyList( ) );
        MockHttpServletRequest request = new MockHttpServletRequest( );

        assertNotNull( component.doValidateTask( 1, RESOURCE_TYPE, request, Locale.FRENCH, null ) );
    }

    /**
     * The selected user must be one of the proposed users
     */
    @Test
    public void testSelectedUserMustBeProposed( )
    {
        AssignUserResourceTaskComponent component = newComponent( Collections.singletonList( newUser( 5 ) ) );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.addParameter( PARAMETER_USER_ID, "5" );
        assertNull( component.doValidateTask( 1, RESOURCE_TYPE, request, Locale.FRENCH, null ) );

        MockHttpServletRequest requestOther = new MockHttpServletRequest( );
        requestOther.addParameter( PARAMETER_USER_ID, "7" );
        assertNotNull( component.doValidateTask( 1, RESOURCE_TYPE, requestOther, Locale.FRENCH, null ) );
    }

    /**
     * Builds the component with a service proposing the given users
     *
     * @param listUsers
     *            the proposed users
     * @return the component
     */
    private static AssignUserResourceTaskComponent newComponent( List<AdminUser> listUsers )
    {
        IAssignUserResourceTaskService service = (IAssignUserResourceTaskService) Proxy.newProxyInstance(
                AssignUserResourceTaskComponentTest.class.getClassLoader( ), new Class<?> [ ] {
                        IAssignUserResourceTaskService.class
                }, ( proxy, method, args ) -> "createUserList".equals( method.getName( ) ) ? new ArrayList<>( listUsers ) : null );
        return new AssignUserResourceTaskComponent( service, null, null );
    }

    /**
     * Builds an administrator
     *
     * @param nIdUser
     *            the user id
     * @return the administrator
     */
    private static AdminUser newUser( int nIdUser )
    {
        AdminUser user = new AdminUser( );
        user.setUserId( nIdUser );
        return user;
    }
}
