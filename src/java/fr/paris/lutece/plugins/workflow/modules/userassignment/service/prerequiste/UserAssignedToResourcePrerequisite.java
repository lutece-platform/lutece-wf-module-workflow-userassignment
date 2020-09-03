/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.workflow.modules.userassignment.service.prerequiste;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.plugins.userassignment.business.ResourceUserHome;
import fr.paris.lutece.plugins.workflow.service.prerequisite.IManualActionPrerequisiteService;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.DefaultPrerequisiteConfig;
import fr.paris.lutece.plugins.workflowcore.business.prerequisite.IPrerequisiteConfig;
import fr.paris.lutece.portal.business.user.AdminUser;

public class UserAssignedToResourcePrerequisite implements IManualActionPrerequisiteService
{

    private static final String PREREQUISITE_TITLE_I18N = "module.workflow.userassignment.prerequisite.user.resource.title";
    private static final String PREREQUISITE_TYPE = "module.workflow.userassignment.prerequisite.user.resource";

    public String getTitleI18nKey( )
    {
        return PREREQUISITE_TITLE_I18N;
    }

    public String getPrerequisiteType( )
    {
        return PREREQUISITE_TYPE;
    }

    @Override
    public boolean hasConfiguration( )
    {
        return false;
    }

    @Override
    public IPrerequisiteConfig getEmptyConfiguration( )
    {
        return new DefaultPrerequisiteConfig( );
    }

    @Override
    public String getConfigurationDaoBeanName( )
    {
        return null;
    }

    @Override
    public String getConfigHtml( IPrerequisiteConfig config, HttpServletRequest request, Locale locale )
    {
        return null;
    }

    @Override
    public boolean canActionBePerformed( int nIdResource, String strResourceType, IPrerequisiteConfig config, int nIdAction )
    {
        return false;
    }

    @Override
    public boolean canManualActionBePerformed( User user, int nIdResource, String strResourceType, IPrerequisiteConfig config, int nIdAction )
    {
        List<AdminUser> userList = ResourceUserHome.findUserByResource( nIdResource, strResourceType );
        for ( AdminUser assignedUser : userList )
        {
            if ( assignedUser.getUserId( ) == ( (AdminUser) user ).getUserId( ) )
            {
                return true;
            }
        }
        return false;
    }
}
