package com.vishaal.learn.interviews;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HashCodeReview {

	public static void main(String[] args) {
		
		Set<Employee> empSet = new HashSet<Employee>();
		Date doj1 = new Date();
		for (int i = 0 ; i <= 10 ; i++) {
			Date doj = new Date(Calendar.getInstance().getTime().getTime() + i);
			
			//empSet.add(new Employee(i, "Emp Name " + i , doj, null));
			empSet.add(new Employee(1, "Emp Name", doj1, null));
		}
		
		System.out.println("Emp Set Size : " + empSet.size());

	}
	
	public static class Employee {
		
		private int id;
		private String name;
		private Date doj;
		private Set<Employee> reportees;
		
		/**
		 * @param id
		 * @param name
		 * @param doj
		 * @param reportees
		 */
		public Employee(int id, String name, Date doj, Set<Employee> reportees) {
			this.id = id;
			this.name = name;
			this.doj = doj;
			this.reportees = reportees;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the doj
		 */
		public Date getDoj() {
			return doj;
		}

		/**
		 * @return the reportees
		 */
		public Set<Employee> getReportees() {
			return reportees;
		}
		
		/*@Override
		public int hashCode() {
			
			
			final int prime = 31;
			int result = 1;
			result = prime * result + ((doj == null) ? 0 : doj.hashCode());
			result = prime * result + id;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((reportees == null) ? 0 : reportees.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			
			
			
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Employee)) {
				return false;
			}
			Employee other = (Employee) obj;
			if (doj == null) {
				if (other.doj != null) {
					return false;
				}
			} else if (!doj.equals(other.doj)) {
				return false;
			}
			if (id != other.id) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (reportees == null) {
				if (other.reportees != null) {
					return false;
				}
			} else if (!reportees.equals(other.reportees)) {
				return false;
			}
			return true;
		}*/
	}
}