package com.example.ordermanagement.Entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AppUser implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    private String image;

    private String password;

    private String address;

    private String mobile;

    @Column(columnDefinition = "boolean default true")
    private boolean enable;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles= new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Payment> payments = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    public void addOrder(Order order){
        orders.add(order);
        order.setUser(this);
    }

    public void addPayment(Payment payment){
        payments.add(payment);
        payment.setUser(this);
    }

}
