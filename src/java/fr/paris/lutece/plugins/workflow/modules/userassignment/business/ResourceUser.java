package fr.paris.lutece.plugins.workflow.modules.userassignment.business;

import java.sql.Timestamp;

import fr.paris.lutece.portal.business.user.AdminUser;

/**
 * This class represents an user assignment to a resource
 */
public class ResourceUser {

	private int _id;
	private int _idResource;
	private String _resourceType;
	private AdminUser _adminUser;
	private Timestamp _dateAssignment;
	private boolean _active;
	
	public ResourceUser( )
	{
		_adminUser = new AdminUser( );
		_adminUser.setUserId( -1 );
	}

	/**
	 * @return the _id
	 */
	public int getId( )
	{
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void setId( int _id )
	{
		this._id = _id;
	}

	/**
	 * @return the _idResource
	 */
	public int getIdResource( )
	{
		return _idResource;
	}

	/**
	 * @param _idResource the _idResource to set
	 */
	public void setIdResource( int _idResource )
	{
		this._idResource = _idResource;
	}

	/**
	 * @return the _resourceType
	 */
	public String getResourceType( )
	{
		return _resourceType;
	}

	/**
	 * @param _resourceType the _resourceType to set
	 */
	public void setResourceType( String _resourceType )
	{
		this._resourceType = _resourceType;
	}

	/**
	 * @return the _adminUser
	 */
	public AdminUser getAdminUser( )
	{
		return _adminUser;
	}

	/**
	 * @param _adminUser the _adminUser to set
	 */
	public void setAdminUser( AdminUser _adminUser )
	{
		this._adminUser = _adminUser;
	}

	/**
	 * @return the _dateAssignmentDate
	 */
	public Timestamp getDateAssignment( )
	{
		return _dateAssignment;
	}

	/**
	 * @param _dateAssignmentDate the _dateAssignmentDate to set
	 */
	public void setDateAssignment( Timestamp _dateAssignmentDate )
	{
		this._dateAssignment = _dateAssignmentDate;
	}

	/**
	 * @return the _bIsActive
	 */
	public boolean isActive( )
	{
		return _active;
	}

	/**
	 * @param _bIsActive the _bIsActive to set
	 */
	public void setActive( boolean _bIsActive )
	{
		this._active = _bIsActive;
	}
}
