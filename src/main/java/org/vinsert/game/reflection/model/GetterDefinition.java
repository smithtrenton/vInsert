package org.vinsert.game.reflection.model;

/**
 * @author rvbiljouw
 */
public class GetterDefinition {
    public String name;
    public String signature;
    public String actualSig;
    public String fieldClass;
    public String fieldName;
    public int multiplier;
    public boolean member;

    public boolean isNonPrimitive() {
        return !(signature.contains("java") && signature.contains("/"))
                && !signature.matches("\\[{0,3}(I|Z|F|J|S|B|D|C)(;?)");
    }
}
