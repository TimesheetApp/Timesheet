package jbc.timesheet.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Collection;

@Entity
public class Employee extends MyUserDetail {

    // INHERIT see: MyUserDetail

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String email;

    private String ssn;


    @Embedded
    @AttributeOverride(name="street", column=@Column(name="addressStreet"))
    @AttributeOverride(name="street2", column=@Column(name="addressStreet2"))
    @AttributeOverride(name="city", column=@Column(name="addressCity"))
    @AttributeOverride(name="state", column=@Column(name="addressState"))
    @AttributeOverride(name="zip", column=@Column(name="addressZip"))
    private Address address;

    private String phone;

    public Employee() {
    }

    public Employee(long id, String username, String password, Collection<Authority> authorities, String firstName, String lastName, LocalDate dob, String email, String ssn, Address address, String phone) {
        super(id, username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.ssn = ssn;
        this.address = address;
        this.phone = phone;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstName).append(' ').append(lastName);

        return sb.toString().trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
