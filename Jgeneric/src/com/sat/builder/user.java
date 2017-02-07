package com.sat.builder;

public class user {

	private String firstname;
	private String lastname;
	private String seno;

	private user(userBuilder ub) {
		this.firstname = ub.firstname;
		this.lastname = ub.lastname;
		this.seno = ub.sno;
	}
	public String getName(){
		
		return "S.No: " + seno + "\n first Name:"+ firstname+ "\nLastName:"+lastname;
		
	}

	public static class  userBuilder {
		private String sno;
		private String firstname;
		private String lastname;
        public userBuilder(String sno){
        	this.sno=sno;
        	
        }
		public userBuilder withfname(String firstname) {
			this.firstname = firstname;

			return this;
		}

		public userBuilder withlname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public user build() {
			user user1 = new user(this);
			return user1;
		}

	}
}
