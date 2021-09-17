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
package fr.paris.lutece.plugins.workflow.modules.userassignment.service.task;

import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.userassignment.business.IResourceUserDAO;
import fr.paris.lutece.plugins.userassignment.business.ResourceUser;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.AssignUserResourceTaskConfig;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.IAdminUserListProvider;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.SelfAssignAdminUserListProvider;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class AssignUserResourceTaskService implements IAssignUserResourceTaskService
{

    private final Plugin _workflowPlugin;
    private final IResourceUserDAO _resourceUserDAO;
    private final ITaskConfigDAO<AssignUserResourceTaskConfig> _taskConfigDAO;

    /**
     * Constructor
     * 
     */
    @Inject
    public AssignUserResourceTaskService( IResourceUserDAO resourceUserDAO, ITaskConfigDAO<AssignUserResourceTaskConfig> taskConfigDAO )
    {
        this._workflowPlugin = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
        this._resourceUserDAO = resourceUserDAO;
        this._taskConfigDAO = taskConfigDAO;
    }

    @Override
    public void assignUserToResource( AdminUser user, int resourceId, String resourceType )
    {
        List<AdminUser> assignedUsers = _resourceUserDAO.selectUserListByResource( resourceId, resourceType, _workflowPlugin );

        boolean alreadyAssigned = null != assignedUsers.stream( ).map( AdminUser::getUserId ).filter( Predicate.isEqual( user.getUserId( ) ) ).findFirst( )
                .orElse( null );

        if ( !alreadyAssigned )
        {
            ResourceUser resourceUser = new ResourceUser( );
            resourceUser.setIdResource( resourceId );
            resourceUser.setResourceType( resourceType );
            resourceUser.setAdminUser( user );
            resourceUser.setActive( true );

            _resourceUserDAO.insert( resourceUser, _workflowPlugin );
        }

    }

    @Override
    public List<IAdminUserListProvider> getProviderList( )
    {
        return SpringContextService.getBeansOfType( IAdminUserListProvider.class );
    }

    @Override
    public List<AdminUser> createUserList( HttpServletRequest request, ITask task, int resourceKey, String resourceType )
    {
        AssignUserResourceTaskConfig config = _taskConfigDAO.load( task.getId( ) );
        IAdminUserListProvider provider = null;
        if ( config != null )
        {
            for ( IAdminUserListProvider p : getProviderList( ) )
            {
                if ( p.getName( ).equals( config.getProviderName( ) ) )
                {
                    provider = p;
                    break;
                }
            }
        }
        if ( provider == null )
        {
            provider = new SelfAssignAdminUserListProvider( );
        }
        return provider.getUserList( request, resourceKey, resourceType );
    }

    @Override
    public void unassignUserToResource( AdminUser user, int resourceId, String resourceType )
    {
        _resourceUserDAO.deactivateByUserResource( user.getUserId( ), resourceId, resourceType, _workflowPlugin );
    }

    @Override
    public List<AdminUser> listActiveUserByResource( int resourceId, String resourceType )
    {
        return _resourceUserDAO.selectUserListByResource( resourceId, resourceType, _workflowPlugin );
    }
}
