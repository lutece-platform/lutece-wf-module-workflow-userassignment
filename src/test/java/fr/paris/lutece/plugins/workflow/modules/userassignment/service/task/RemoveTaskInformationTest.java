/*
 * Copyright (c) 2002-2026, City of Paris
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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformation;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.information.UserTaskInformationHome;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.test.LuteceTestCase;
import jakarta.inject.Inject;

/**
 * The information of the user assignment tasks is removed with the history it belongs to
 */
public class RemoveTaskInformationTest extends LuteceTestCase
{
    private static final int ID_TASK = 987600;
    private static final int ID_HISTORY = 987601;
    private static final int ID_OTHER_HISTORY = 987602;

    @Inject
    private AssignUserResourceTask _assignUserResourceTask;
    @Inject
    private AssignCurrentUserResourceTask _assignCurrentUserResourceTask;
    @Inject
    private UnassignUserResourceTask _unassignUserResourceTask;
    @Inject
    private UnassignUserResourceAutomaticTask _unassignUserResourceAutomaticTask;

    @Test
    public void testRemoveTaskInformationOfOneHistory( )
    {
        for ( ITask task : List.of( _assignUserResourceTask, _assignCurrentUserResourceTask, _unassignUserResourceTask, _unassignUserResourceAutomaticTask ) )
        {
            task.setId( ID_TASK );
            createInformation( ID_HISTORY );
            createInformation( ID_OTHER_HISTORY );

            try
            {
                task.doRemoveTaskInformation( ID_HISTORY );

                assertNull( UserTaskInformationHome.find( ID_HISTORY, ID_TASK ), task.getClass( ).getName( ) );
                assertNotNull( UserTaskInformationHome.find( ID_OTHER_HISTORY, ID_TASK ), task.getClass( ).getName( ) );
            }
            finally
            {
                UserTaskInformationHome.remove( ID_HISTORY, ID_TASK );
                UserTaskInformationHome.remove( ID_OTHER_HISTORY, ID_TASK );
            }
        }
    }

    private void createInformation( int nIdHistory )
    {
        UserTaskInformation information = new UserTaskInformation( nIdHistory, ID_TASK );
        information.add( UserTaskInformation.TASK_USER_ID, "1" );
        UserTaskInformationHome.create( information );
    }
}
