package org.vinsert.api.impl.tabs;

import org.vinsert.api.DepositBox;
import org.vinsert.api.collection.queries.BankQuery;
import org.vinsert.api.wrappers.WidgetItem;

import java.util.List;

/**
 * @author : const_
 */

//TODO: Complete this
public final class DepositBoxImpl implements DepositBox {
    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public List<WidgetItem> getAll() {
        return null;
    }

    @Override
    public boolean contains(int... ids) {
        return false;
    }

    @Override
    public boolean containsAll(int... ids) {
        return false;
    }

    @Override
    public BankQuery find(int... ids) {
        return null;
    }

    @Override
    public BankQuery find(String... names) {
        return null;
    }

    @Override
    public int count(boolean stacks, int... ids) {
        return 0;
    }

    @Override
    public boolean withdrawAll(int id) {
        return false;
    }

    @Override
    public boolean withdrawAll(String name) {
        return false;
    }

    @Override
    public boolean withdrawOne(int id) {
        return false;
    }

    @Override
    public boolean withdrawOne(String name) {
        return false;
    }

    @Override
    public boolean withdrawX(int id, int amount) {
        return false;
    }

    @Override
    public boolean withdrawX(String name, int amount) {
        return false;
    }

    @Override
    public boolean deposit(int amount, int... ids) {
        return false;
    }

    @Override
    public boolean depositExcept(int amount, int... ids) {
        return false;
    }

    @Override
    public boolean depositAll() {
        return false;
    }

    @Override
    public boolean depositAllExcept(int... ids) {
        return false;
    }

    @Override
    public BankQuery find() {
        return null;
    }

    @Override
    public boolean contains(String... names) {
        return false;
    }

    @Override
    public boolean containsAll(String... names) {
        return false;
    }

    @Override
    public int count(boolean stacks, String... names) {
        return 0;
    }

    @Override
    public boolean depositAllOf(String name) {
        return false;
    }

    @Override
    public boolean depositOneOf(String name) {
        return false;
    }

    @Override
    public boolean depositXOf(String name, int amount) {
        return false;
    }

    @Override
    public boolean depositAllOf(int id) {
        return false;
    }

    @Override
    public boolean depositOneOf(int id) {
        return false;
    }

    @Override
    public boolean depositXOf(int id, int amount) {
        return false;
    }

    @Override
    public boolean deposit(int amount, String... names) {
        return false;
    }

    @Override
    public boolean depositExcept(int amount, String... names) {
        return false;
    }

    @Override
    public boolean depositAllExcept(String... names) {
        return false;
    }
}
