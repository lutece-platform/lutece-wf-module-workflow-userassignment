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
package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

/**
 * This class provides Data Access methods for {@link TaskUserAssignmentNotificationConfigDAO } objects
 */
@ApplicationScoped
@Named( "workflow-userassignment.taskUserAssignmentNotificationConfigDAO" )
public final class TaskUserAssignmentNotificationConfigDAO implements ITaskConfigDAO<TaskUserAssignmentNotificationConfig>
{

    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_task, message, subject, sender_name, sender_email, recipients_cc, recipients_bcc FROM workflow_task_userassignment_assignment_notification_cf WHERE id_task = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO workflow_task_userassignment_assignment_notification_cf ( id_task, message, subject, sender_name, sender_email, recipients_cc, recipients_bcc ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM workflow_task_userassignment_assignment_notification_cf WHERE id_task = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE workflow_task_userassignment_assignment_notification_cf SET message = ?, subject = ?, sender_name = ?, sender_email = ?, recipients_cc = ?, recipients_bcc = ? WHERE id_task = ?";


    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TaskUserAssignmentNotificationConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            daoUtil.setString( nIndex++, config.getMessage( ) );
            daoUtil.setString( nIndex++, config.getSubject( ) );
            daoUtil.setString( nIndex++, config.getSenderName( ) );
            daoUtil.setString( nIndex++, config.getSenderEmail( ) );
            daoUtil.setString( nIndex++, config.getRecipientsCc( ) );
            daoUtil.setString( nIndex++, config.getRecipientsBcc( ) );
            
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TaskUserAssignmentNotificationConfig load( int nId )
    {
    	TaskUserAssignmentNotificationConfig config = null;
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                config = new TaskUserAssignmentNotificationConfig( );

                int nIndex = 1;
                config.setIdTask( daoUtil.getInt( nIndex++ ) );
                config.setMessage( daoUtil.getString( nIndex++ ) );
                config.setSubject( daoUtil.getString( nIndex++ ) );
                config.setSenderName( daoUtil.getString( nIndex++ ) );
                config.setSenderEmail( daoUtil.getString( nIndex++ ) );
                config.setRecipientsCc( daoUtil.getString( nIndex++ ) );
                config.setRecipientsBcc( daoUtil.getString( nIndex++ ) );
            }
        }

        return config;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nConfigId )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nConfigId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( TaskUserAssignmentNotificationConfig config )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, config.getMessage( ) );
            daoUtil.setString( nIndex++, config.getSubject( ) );
            daoUtil.setString( nIndex++, config.getSenderName( ) );
            daoUtil.setString( nIndex++, config.getSenderEmail( ) );
            daoUtil.setString( nIndex++, config.getRecipientsCc( ) );
            daoUtil.setString( nIndex++, config.getRecipientsBcc( ) );
            daoUtil.setInt( nIndex++, config.getIdTask( ) );
            
            daoUtil.executeUpdate( );
        }
    }

}
