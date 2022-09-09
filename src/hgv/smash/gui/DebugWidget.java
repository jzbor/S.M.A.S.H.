package hgv.smash.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DebugWidget {
    private int depth;
    private double lastRecord;
    private List<DebugRecord> records;

    public DebugWidget(int depth) {
        this.depth = depth;
        this.lastRecord = millis();
        this.records = new ArrayList<>();
    }

    public void resetClock() {
        this.lastRecord = millis();
    }

    public void recordPoint(String identifier) {
        double time = millis();
        addRecord(identifier, time - lastRecord);
        lastRecord = time;
    }

    private void addRecord(String identifier, double value) {
        // find identifier
        for (DebugRecord dr : records) {
            if (dr.identifier.equals(identifier)) {
                dr.records[dr.nextIndex] = value;
                dr.advanceIndex();
                return;
            }
        }

        // identifier not registered yet
        records.add(new DebugRecord(identifier, depth));

    }

    public void draw(Graphics graphics, int x, int y, int lineOffset) {
        // measure average duration of one fram
        double avgTotal = 0;
        for (int i = 0; i < depth; i++) {
            double current = 0;
            for (DebugRecord dr : records) {
                current += dr.records[i];
            }
            avgTotal += current;
        }
        avgTotal /= depth;

        // print average frame time
        String string = String.format("Frame: %.2fms per Frame (%d FPS)", avgTotal, (int) (1000 / avgTotal));
        graphics.drawString(string, x, y);
        y += lineOffset;

        for (DebugRecord dr : records) {
            double share = dr.avg() / avgTotal;
            string = String.format("%s: %.2fms (%.1f%%)", dr.identifier, dr.avg(), (share * 100));
            graphics.drawString(string, x, y);
            y += lineOffset;
        }

    }

    public static double millis() {
        return ((double) System.nanoTime()) / 1000000;
    }
}

class DebugRecord {
    final int depth;
    int nextIndex;
    double[] records;
    String identifier;

    DebugRecord(String identifier, int depth) {
        this.depth = depth;
        this.nextIndex = 0;
        this.records = new double[depth];
        this.identifier = identifier;
    }

    public void advanceIndex() {
        nextIndex = (nextIndex + 1) % depth;
    }

    public double avg() {
        double sum = 0;
        for (int i = 0; i < records.length; i++) {
            sum += records[i];
        }
        return sum / depth;
    }

}