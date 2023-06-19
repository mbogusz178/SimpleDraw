package schumi178.javaprojects.graphics.zad1.filter;

import schumi178.javaprojects.graphics.zad1.filter.util.Color;

public interface SingleParamFilter {
    Color[][] transform(Color[][] image, double value);
}
