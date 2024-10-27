package com.jmnt.utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GradeUtils {
    public static final List<Character> GRADES = Collections.unmodifiableList(
            Arrays.asList('A', 'B', 'C', 'M', 'E', 'L')
    );

    private GradeUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
