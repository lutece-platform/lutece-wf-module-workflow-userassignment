package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import fr.paris.lutece.plugins.workflowcore.business.config.ITaskConfigDAO;
import fr.paris.lutece.util.sql.DAOUtil;

public class AssignUserResourceTaskConfigDao implements ITaskConfigDAO<AssignUserResourceTaskConfig> {

	// Queries
	private static final String SQL_INSERT = "INSERT INTO workflow_task_assign_user_config (id_task, provider_name) VALUES (?, ?)";
	private static final String SQL_UPDATE = "UPDATE workflow_task_assign_user_config SET provider_name = ? WHERE id_task = ?";
	private static final String SQL_DELETE = "DELETE FROM workflow_task_assign_user_config WHERE id_task = ?";
	private static final String SQL_SELECT = "SELECT id_task, provider_name FROM workflow_task_assign_user_config WHERE id_task = ?";

	@Override
	public void insert( AssignUserResourceTaskConfig config )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_INSERT ) )
		{
			int nPos = 1;
			daoUtil.setInt( nPos++, config.getIdTask( ) );
			daoUtil.setString( nPos++, config.getProviderName( ) );

			daoUtil.executeUpdate( );
		}

	}

	@Override
	public void store( AssignUserResourceTaskConfig config )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_UPDATE ) ) {
			int nPos = 1;
			daoUtil.setString (nPos++, config.getProviderName( ) );
			daoUtil.setInt( nPos++, config.getIdTask( ) );

			daoUtil.executeUpdate( );
		}

	}

	@Override
	public AssignUserResourceTaskConfig load( int nIdTask )
	{
		AssignUserResourceTaskConfig config = null;

		try ( DAOUtil daoUtil = new DAOUtil( SQL_SELECT ) )
		{
			int nPos = 1;
			daoUtil.setInt( nPos++, nIdTask );
			daoUtil.executeQuery( );

			if ( daoUtil.next( ) )
			{
				int nIndex = 1;
				config = new AssignUserResourceTaskConfig( );
				config.setIdTask( daoUtil.getInt( nIndex++ ) );
				config.setProviderName( daoUtil.getString( nIndex++ ) );
			}
		}
		return config;
	}

	@Override
	public void delete( int nIdTask )
	{
		try ( DAOUtil daoUtil = new DAOUtil( SQL_DELETE ) )
		{
			int nPos = 1;
			daoUtil.setInt( nPos++, nIdTask );

			daoUtil.executeUpdate( );
		}
	}
}
