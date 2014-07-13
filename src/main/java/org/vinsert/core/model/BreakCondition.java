package org.vinsert.core.model;


import org.vinsert.api.MethodContext;
import org.vinsert.api.util.Utilities;
import org.vinsert.api.wrappers.Skill;
import org.vinsert.core.Session;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author const_
 */
public final class BreakCondition {
    public static final int MILLIS_IN_MINUTE = 60000;

    private int time;
    private BreakPredicate predicate;
    private String desc;
    private boolean executed = false;
    private BreakConditionType type;
    private Skill skill;
    private int min;
    private int max;

    public BreakCondition() {

    }

    private BreakCondition(int time, BreakPredicate predicate, String desc, BreakConditionType type) {
        this.type = type;
        this.time = time * MILLIS_IN_MINUTE;
        this.predicate = predicate;
        this.desc = desc;
    }

    public boolean validate(MethodContext ctx) {
        return !(type == BreakConditionType.SKILL && executed) && predicate.apply(ctx);
    }

    public void execute(MethodContext ctx) {
        ctx.tabs.logout();
        ctx.session.setState(Session.State.PAUSED);
        Utilities.sleep(time);
        ctx.session.setState(Session.State.ACTIVE);
        executed = true;
    }

    public static BreakCondition createTime(final int sleep, final int min, final int max) {
        BreakCondition condition = new BreakCondition(sleep, new BreakPredicate() {
            @Override
            public boolean apply(MethodContext ctx) {
                SimpleDateFormat timingFormat = new SimpleDateFormat("hhmm");
                String curr = timingFormat.format(new Date(System.currentTimeMillis()));
                return min <= Integer.parseInt(curr) && max >= Integer.parseInt(curr);
            }
        }, "When the time is between " + min + " & " + max + " (" + sleep + " minutes)", BreakConditionType.TIME);
        condition.min = max;
        condition.max = min;
        return condition;
    }

    public static BreakCondition createSkill(final int sleep, final Skill skill, final int min, final int max) {
        BreakCondition condition = new BreakCondition(sleep, new BreakPredicate() {
            @Override
            public boolean apply(MethodContext ctx) {
                return ctx.isLoggedIn() && ctx.skills.getBaseLevel(skill) >= min &&
                        ctx.skills.getBaseLevel(skill) <= max;
            }
        }, "When " + skill.toString() + " level is between " + min + " & " + max + " (" + sleep + " minutes)", BreakConditionType.SKILL);
        condition.min = min;
        condition.max = max;
        condition.skill = skill;
        return condition;
    }

    public static BreakCondition createRuntime(final int sleep, final int min, final int max) {
        BreakCondition condition = new BreakCondition(sleep, new BreakPredicate() {
            @Override
            public boolean apply(MethodContext ctx) {
                int curr = ctx.session.getAliveTime();
                return min * MILLIS_IN_MINUTE <= curr && max * MILLIS_IN_MINUTE >= curr;
            }
        }, "When runtime is between " + min + " &  " + max + " minutes (" + sleep + " minutes)", BreakConditionType.RUNTIME);
        condition.min = min;
        condition.max = max;
        return condition;
    }

    public static BreakCondition fromString(String string) {
        BreakConditionType type = null;
        int min = 0;
        int max = 0;
        int time = 0;
        Skill skill = null;
        String[] data = string.split("@");
        for (int i = 0; i < data.length; i++) {
            switch (i) {
                case 0:
                    type = BreakConditionType.valueOf(data[i].toUpperCase());
                    break;
                case 1:
                    min = Integer.parseInt(data[i]);
                    break;
                case 2:
                    max = Integer.parseInt(data[i]);
                    break;
                case 3:
                    time = Integer.parseInt(data[i]);
                    break;
                case 4:
                    skill = (data[i].equals("null") ? null :
                            Skill.valueOf(data[i].toUpperCase()));
                    break;
            }
        }
        switch (type) {
            case SKILL:
                return createSkill(time, skill, min, max);
            case RUNTIME:
                return createRuntime(time, min, max);
            case TIME:
                return createTime(time, min, max);
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isExecuted() {
        return executed;
    }

    public BreakConditionType getType() {
        return type;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getTime() {
        return time;
    }

    public BreakPredicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return desc;
    }

    private interface BreakPredicate {

        boolean apply(MethodContext ctx);
    }
}
