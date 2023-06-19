package schumi178.javaprojects.graphics.zad1.analysis;

import schumi178.javaprojects.graphics.zad1.analysis.util.ColorHSV;
import schumi178.javaprojects.graphics.zad1.analysis.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoPass {
    private final ColorHSV[][] image;
    private final DisjointUnionSets equivalence;
    private Point[][] labelled;
    private int labelCount = 2;

    public TwoPass(ColorHSV[][] image) {
        this.image = image;
        equivalence = new DisjointUnionSets(image.length * image[0].length);
    }

    private void firstPass() {
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                if(labelled[i][j].getLabel() != 0) {
                    if (i == 0 && j == 0) {
                        labelled[i][j].setLabel(1);
                    } else if (i == 0) {
                        labelled[i][j].setLabel(labelled[i][j - 1].getLabel());
                    } else if (j == 0) {
                        labelled[i][j].setLabel(labelled[i - 1][j].getLabel());
                    } else {
                        ColorHSV westValue = image[i][j-1];
                        ColorHSV northValue = image[i-1][j];
                        Point west = labelled[i][j-1];
                        Point north = labelled[i-1][j];
                        if (westValue.getValue() != image[i][j].getValue()) {
                            if (westValue.getValue() == image[i][j].getValue() &&
                            northValue.getValue() == image[i][j].getValue() &&
                            west.getLabel() != north.getLabel()) {
                                labelled[i][j].setLabel(Math.min(west.getLabel(), north.getLabel()));
                                equivalence.union(west.getLabel(), north.getLabel());
                            } else if(westValue.getValue() != image[i][j].getValue() &&
                            northValue.getValue() == image[i][j].getValue()) {
                                labelled[i][j].setLabel(north.getLabel());
                            } else if(westValue.getValue() != image[i][j].getValue() &&
                            northValue.getValue() != image[i][j].getValue()) {
                                labelled[i][j].setLabel(labelCount++);
                            }
                        }
                    }
                }
            }
        }
    }

    private void secondPass() {
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                labelled[i][j].setLabel(equivalence.find(labelled[i][j].getLabel()));
            }
        }
    }

    private List<Integer> countSizes() {
        Map<Integer, Integer> labelMap = new HashMap<>();
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                int label = labelled[i][j].getLabel();
                if(label != 0) {
                    if(!labelMap.containsKey(label)) {
                        labelMap.put(label, 0);
                    }
                    labelMap.put(label, labelMap.get(label) + 1);
                }
            }
        }
        return new ArrayList<>(labelMap.values());
    }

    public List<Integer> getRegionSizes() {
        labelled = new Point[image.length][image[0].length];
        for(int i = 0; i < image.length; i++) {
            for(int j = 0; j < image[i].length; j++) {
                labelled[i][j] = new Point(i, j, (int)image[i][j].getValue() == 0 ? 1 : 0);
            }
        }
        firstPass();
        secondPass();
        return countSizes();
    }
}
