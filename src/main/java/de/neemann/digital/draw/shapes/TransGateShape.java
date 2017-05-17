package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.Observer;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.PinDescriptions;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pin;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.Graphic;
import de.neemann.digital.draw.graphics.Polygon;
import de.neemann.digital.draw.graphics.Style;
import de.neemann.digital.draw.graphics.Vector;

import static de.neemann.digital.draw.shapes.GenericShape.SIZE;
import static de.neemann.digital.draw.shapes.GenericShape.SIZE2;

/**
 * Shape of a transmission gate.
 * Created by hneemann on 17.05.17.
 */
public class TransGateShape implements Shape {
    private static final int RAD = 4;
    private static final int P = SIZE - 5;
    private static final Polygon TOP = new Polygon(true)
            .add(0, 0)
            .add(0, -SIZE)
            .add(SIZE * 2, 0)
            .add(SIZE * 2, -SIZE)
            .add(0, 0);
    private static final Polygon BOTTOM = new Polygon(true)
            .add(0, 0)
            .add(0, SIZE)
            .add(SIZE * 2, 0)
            .add(SIZE * 2, SIZE)
            .add(0, 0);

    private final PinDescriptions input;
    private final PinDescriptions output;

    /**
     * Creates a trantmission gate
     *
     * @param attr   the attrobutes
     * @param input  inputs
     * @param output outputs
     */
    public TransGateShape(ElementAttributes attr, PinDescriptions input, PinDescriptions output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public Pins getPins() {
        return new Pins()
                .add(new Pin(new Vector(SIZE, -SIZE), input.get(0)))
                .add(new Pin(new Vector(SIZE, SIZE), input.get(1)))
                .add(new Pin(new Vector(0, 0), output.get(0)))
                .add(new Pin(new Vector(SIZE * 2, 0), output.get(1)));
    }

    @Override
    public InteractorInterface applyStateMonitor(IOState ioState, Observer guiObserver) {
        return null;
    }

    @Override
    public void drawTo(Graphic graphic, boolean highLight) {
        graphic.drawPolygon(TOP, Style.NORMAL);
        graphic.drawPolygon(BOTTOM, Style.NORMAL);
        graphic.drawLine(new Vector(SIZE, -SIZE), new Vector(SIZE, -SIZE2), Style.NORMAL);
        graphic.drawCircle(new Vector(SIZE - RAD, P - RAD), new Vector(SIZE + RAD, P + RAD), Style.NORMAL);
    }

}
