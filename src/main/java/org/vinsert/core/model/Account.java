package org.vinsert.core.model;

import com.google.gson.annotations.Expose;
import org.vinsert.api.wrappers.Skill;

import java.util.List;

/**
 * Represents an Account in the database.
 * WARNING: it's important that this class is properly secured
 * by the SecurityManager, to prevent accounts from being hijacked.
 */
public final class Account {
    private static final Container<Account> ACCOUNT_CONTAINER =
            new Container<>("accounts");

    @Expose
    private String username = "";
    @Expose
    private String password = "";
    @Expose
    private String bankPin = "0000";
    @Expose
    private Skill lampSkill;
    @Expose
    private boolean purgeMarker = false;

    public Account(String username, String password, String bankPin) {
        setUsername(username);
        setPassword(password);
        setBankPin(bankPin);
    }

    public Account() {

    }

    public static void defaults() {

    }

    public static List<Account> getAll() {
        return ACCOUNT_CONTAINER.getAll();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password in encrypted form
     *
     * @return The encrypted password
     */
    public String getPassword() {
        return password;
    }

    public Skill getLampSkill() {
        return lampSkill;
    }

    /**
     * Encrypts and sets the password for this account
     *
     * @param password The password to encrypt
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankPin() {
        return bankPin;
    }

    public void setBankPin(String bankPin) {
        this.bankPin = bankPin;
    }

    public void setLampSkill(Skill lampSkill) {
        this.lampSkill = lampSkill;
    }

    public boolean isPurgeMarker() {
        return purgeMarker;
    }

    public void setPurgeMarker(boolean purgeMarker) {
        this.purgeMarker = purgeMarker;
    }

    @Override
    public String toString() {
        return username;
    }

    public static Container<Account> getContainer() {
        return ACCOUNT_CONTAINER;
    }

    public void save() {
        if (ACCOUNT_CONTAINER.contains(this)) {
            ACCOUNT_CONTAINER.remove(this);
        }
        ACCOUNT_CONTAINER.add(this);
        ACCOUNT_CONTAINER.save();
    }

    public void remove() {
        ACCOUNT_CONTAINER.remove(this);
        ACCOUNT_CONTAINER.save();
    }
}
