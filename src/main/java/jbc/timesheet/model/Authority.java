package jbc.timesheet.model;

import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Authority implements GrantedAuthority {

    @Id
    @SequenceGenerator(name = "Authority", sequenceName = "AuthorityId", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(generator = "Authority")
    private long id;

    @NaturalId
    private String authority;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "authorities"
            // Reverse end
    )
    private Collection<MyUserDetail> myUserDetailCollection;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Collection<MyUserDetail> getMyUserDetailCollection() {
        return myUserDetailCollection;
    }

    public void setMyUserDetailCollection(Collection<MyUserDetail> myUserDetailCollection) {
        this.myUserDetailCollection = myUserDetailCollection;
    }

    @Override
    public String toString() {
        return this.authority;
    }
}
