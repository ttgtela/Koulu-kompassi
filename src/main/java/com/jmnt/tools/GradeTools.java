package com.jmnt.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GradeTools {
    public static final List<Character> GRADES = Collections.unmodifiableList(
            Arrays.asList('A', 'B', 'C', 'M', 'E', 'L')
    );

    private GradeTools() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
