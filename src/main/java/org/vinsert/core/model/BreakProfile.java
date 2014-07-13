package org.vinsert.core.model;

/**
 * @author const_
 */
public final class BreakProfile {

    private BreakCondition[] conditions = new BreakCondition[0];
    private String name;
    private String conditionsString = "";

    public BreakProfile() {

    }

    public BreakProfile(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addConditions(BreakCondition... conditions) {
        int curr = this.conditions.length;
        BreakCondition next = (curr > 0 ? this.conditions[0] : conditions[0]);
        BreakCondition[] newArray = new BreakCondition[curr + conditions.length];
        StringBuilder builder = new StringBuilder(next.getType().toString());
        builder.append("@").append(next.getMin()).append("@").append(next.getMax()).append("@")
                .append(next.getTime()).append("@").append((next.getSkill() != null ? next.getSkill().toString() : "null"));
        if (curr > 0) {
            for (int x = 1; x < curr; x++) {
                next = conditions[x];
                newArray[x] = this.conditions[x];
                builder.append("!").append(next.getType().name()).append("@").append(next.getMin()).append("@").append(next.getMax()).append("@")
                        .append(next.getTime()).append("@").append((next.getSkill() != null ? next.getSkill().toString() : "null"));
            }
        }
        for (int i = curr; i < conditions.length + curr; i++) {
            next = conditions[i - curr];
            newArray[i] = next;
            builder.append("!").append(next.getType().name()).append("@").append(next.getMin()).append("@").append(next.getMax()).append("@")
                    .append(next.getTime()).append("@").append((next.getSkill() != null ? next.getSkill().toString() : "null"));
        }
        this.conditions = newArray;
        conditionsString = builder.toString();
    }

    public BreakCondition[] getConditions() {
        return conditions.clone();
    }

    public void setConditions(BreakCondition[] conditions) {
        this.conditions = conditions.clone();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getConditionsString() {
        return conditionsString;
    }

    public void setConditionsString(String conditionsString) {
        this.conditionsString = conditionsString;
    }
}
