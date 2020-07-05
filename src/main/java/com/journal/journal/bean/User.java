/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author anoir
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String pseudo;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    @JsonIgnore
    private String password;
    private String degree;
    private String adress;
    private String country;
    private String region;
    private String city;
    private String postalCode;
    private String phone;
    private String fax;
    private String institution;
    private String departement;
    private String instAdress;
    private String instPhone;
    private String availability;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date registrationDate;
    private boolean verified;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRoleDetail> userRoleDetails;

    @OneToMany(mappedBy = "user")
    private List<UserArticleDetail> userArticleDetails;
    @OneToMany(mappedBy = "user")
    private List<UserSpecialtyDetail> userSpecialtyDetails;

    public User() {
    }

    public User(String pseudo, String firstName, String lastName, String middleName, String email, String degree, String adress, String country, String region, String city, String postalCode, String phone, String fax, String institution, String departement, String instAdress, String instPhone) {
        this.pseudo = pseudo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.degree = degree;
        this.adress = adress;
        this.country = country;
        this.region = region;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.fax = fax;
        this.institution = institution;
        this.departement = departement;
        this.instAdress = instAdress;
        this.instPhone = instPhone;
        this.verified = false;
        this.registrationDate = new Date();
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserArticleDetail> getUserArticleDetails() {
        return userArticleDetails;
    }

    public void setUserArticleDetails(List<UserArticleDetail> userArticleDetails) {
        this.userArticleDetails = userArticleDetails;
    }

    public List<UserSpecialtyDetail> getUserSpecialtyDetails() {
        return userSpecialtyDetails;
    }

    public void setUserSpecialtyDetails(List<UserSpecialtyDetail> userSpecialtyDetails) {
        this.userSpecialtyDetails = userSpecialtyDetails;
    }

    public List<UserRoleDetail> getUserRoleDetails() {
        return userRoleDetails;
    }

    public void setUserRoleDetails(List<UserRoleDetail> userRoleDetails) {
        this.userRoleDetails = userRoleDetails;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String Degree) {
        this.degree = degree;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getInstAdress() {
        return instAdress;
    }

    public void setInstAdress(String instAdress) {
        this.instAdress = instAdress;
    }

    public String getInstPhone() {
        return instPhone;
    }

    public void setInstPhone(String instPhone) {
        this.instPhone = instPhone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", institution=" + institution + '}';
    }

}
