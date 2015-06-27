/**
 * 
 */
package com.vishaal.learn.interviews;

import java.util.Date;
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author vkant
 *
 */
public class Employee {
	private int ID;
	private Date dateOfJoining;
	private Set<Employee> reportees;
	
	/**
	 * @param iD
	 * @param dateOfJoining
	 * @param reportees
	 */
	public Employee(int iD, Date dateOfJoining, Set<Employee> reportees) {
		ID = iD;
		this.dateOfJoining = dateOfJoining;
		this.reportees = reportees;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the dateOfJoining
	 */
	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	/**
	 * @return the reportees
	 */
	public Set<Employee> getReportees() {
		return reportees;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).toString();
	}
}
