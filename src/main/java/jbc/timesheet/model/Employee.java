package jbc.timesheet.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Entity
public class Employee extends MyUserDetail {

    // INHERIT see: MyUserDetail

    private String firstName;

    private String lastName;


    private LocalDate dob;

    @Pattern(regexp = "^..../../..$", message="must be a valid ISO date")
    private String isoDob;

    private String ssn;


    @Embedded
    @AttributeOverride(name="street", column=@Column(name="addressStreet"))
    @AttributeOverride(name="street2", column=@Column(name="addressStreet2"))
    @AttributeOverride(name="city", column=@Column(name="addressCity"))
    @AttributeOverride(name="state", column=@Column(name="addressState"))
    @AttributeOverride(name="zip", column=@Column(name="addressZip"))
    private Address address = new Address();

    private String phone;

    public Employee() {
    }

    public Employee(long id, String email,String password, Collection<Authority> authorities, String firstName, String lastName, LocalDate dob,  String ssn, Address address, String phone) {
        super(id, email, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.ssn = ssn;
        this.address = address;
        this.phone = phone;
    }

    public Employee(String username) {
        super(username);

        this.firstName = username.substring(0, 1).toUpperCase() + username.replaceAll("@.+", "").substring(1);;
        this.lastName = "Auto-generated";
        this.dob = LocalDate.now();
    }


    @Override
    public String toString() {

        return (firstName + ' ' + lastName).trim();
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

    @Transient
    public String getIsoDob() {
        if (dob == null)
            return LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        isoDob = dob.format(DateTimeFormatter.ISO_DATE);

        return isoDob;
    }

    public void setIsoDob(String isoDob) {
        dob = LocalDate.parse(isoDob,DateTimeFormatter.ISO_DATE);
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

    @Transient
    public boolean hasAuthority(String authority) {
        if (getAuthorities() == null)
            return false;

        for (Authority eachAuthority : getAuthorities()) {
            if (eachAuthority.getAuthority().equals(authority))
                return true;
        }

        return false;
    }
}
