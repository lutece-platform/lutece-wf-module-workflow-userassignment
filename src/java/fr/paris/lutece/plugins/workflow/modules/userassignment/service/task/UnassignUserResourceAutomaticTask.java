/*
 * Copyright (c) 2002-2022, City of Paris
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
package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 * 
 * UnassignUserResourceAutomaticTask
 *
 */
public class UnassignUserResourceAutomaticTask extends SimpleTask
{

    private static final String MESSAGE_TASK_TITLE = "module.workflow.userassignment.task_unassign_user_resource_automatic.title";

    private final IAssignUserResourceTaskService _assignUserResourceTaskService;
    private final IResourceHistoryService        _resourceHistoryService;

    @Inject
    public UnassignUserResourceAutomaticTask( IAssignUserResourceTaskService assignUserResourceTaskService, IResourceHistoryService resourceHistoryService )
    {
        super( );
        _assignUserResourceTaskService = assignUserResourceTaskService;
        _resourceHistoryService = resourceHistoryService;
    }

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );

    	List<AdminUser> listAdminUser = _assignUserResourceTaskService.listActiveUserByResource( resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
        
    	for ( AdminUser user : listAdminUser )
    	{
        	_assignUserResourceTaskService.unassignUserToResource( user, resourceHistory.getIdResource( ), resourceHistory.getResourceType( ) );
            saveUserTaskInformation( nIdResourceHistory, user );
    	}
    }

    private void saveUserTaskInformation( int nIdResourceHistory, AdminUser userUnassigned )
    {
        UserTaskInformation taskInformation = new UserTaskInformation( nIdResourceHistory, getId( ) );
        taskInformation.add( UserTaskInformation.TASK_INFORMATION_UNASSIGNOR_AUTOMATIC, userUnassigned.getFirstName( ) + " " + userUnassigned.getLastName( ) );

        UserTaskInformationHome.create( taskInformation );
    }

    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_TASK_TITLE, locale );
    }

    @Override
    public void doRemoveTaskInformation( int nIdHistory )
    {
        ResourceHistory history = _resourceHistoryService.findByPrimaryKey( nIdHistory );
        UserTaskInformationHome.remove( history.getIdResource( ), getId( ) );
    }
}
