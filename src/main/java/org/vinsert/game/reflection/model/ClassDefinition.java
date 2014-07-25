package org.vinsert.game.reflection.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rvbiljouw
 */
public class ClassDefinition {
    public List<GetterDefinition> getters = new ArrayList<>();
    public List<MethodDefinition> methods = new ArrayList<>();
    public String identifiedAs;
    public String originalName;
}
