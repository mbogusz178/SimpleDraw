package schumi178.javaprojects.graphics.zad1.analysis.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Point {
    private int x;
    private int y;
    @Setter
    private int label;
}
