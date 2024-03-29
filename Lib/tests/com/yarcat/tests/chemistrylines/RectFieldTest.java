package com.yarcat.tests.chemistrylines;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.field.Field.CellVisitor;
import com.yarcat.chemistrylines.field.RectField;

public class RectFieldTest {

    private final static int[] getVisited(int cols, int rows, int n) {
        RectField field = new RectField(cols, rows);
        final int[] visited = new int[field.getLength()];
        field.visitSiblings(n, new CellVisitor() {
            @Override
            public void visit(int n, Field field) {
                visited[n] = 1;
            }
        });
        return visited;
    }

    @Test
    public void visitCenter() throws Exception {
        int[] visited = getVisited(3, 3, 4);
        assertArrayEquals(new int[] { 0, 1, 0,
                                      1, 0, 1,
                                      0, 1, 0 }, visited);
    }

    @Test
    public void visitLeftTop() throws Exception {
        int[] visited = getVisited(3, 3, 0);
        assertArrayEquals(new int[] { 0, 1, 0,
                                      1, 0, 0,
                                      0, 0, 0 }, visited);
    }

    @Test
    public void visitRightTop() throws Exception {
        int[] visited = getVisited(3, 3, 2);
        assertArrayEquals(new int[] { 0, 1, 0,
                                      0, 0, 1,
                                      0, 0, 0 }, visited);
    }

    @Test
    public void visitRightBottom() throws Exception {
        int[] visited = getVisited(3, 3, 8);
        assertArrayEquals(new int[] { 0, 0, 0,
                                      0, 0, 1,
                                      0, 1, 0 }, visited);
    }
    @Test
    public void visitLeftBottom() throws Exception {
        int[] visited = getVisited(3, 3, 6);
        assertArrayEquals(new int[] { 0, 0, 0,
                                      1, 0, 0,
                                      0, 1, 0 }, visited);
    }
}
