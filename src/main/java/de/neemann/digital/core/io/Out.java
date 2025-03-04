/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.core.io;

import de.neemann.digital.core.*;
import de.neemann.digital.core.element.*;
import de.neemann.digital.lang.Lang;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * The different outputs
 */
public class Out implements Element {

    /**
     * The Input description
     */
    public static final ElementTypeDescription DESCRIPTION
            = new ElementTypeDescription("Out", attributes -> new Out(attributes).enforceName(), input("in")) {
        @Override
        public String getDescription(ElementAttributes elementAttributes) {
            String d = Lang.evalMultilingualContent(elementAttributes.get(Keys.DESCRIPTION));
            if (d.length() > 0)
                return d;
            else
                return super.getDescription(elementAttributes);
        }
    }
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.DESCRIPTION)
            .addAttribute(Keys.INT_FORMAT)
            .addAttribute(Keys.PINNUMBER);

    /**
     * The LED description
     */
    public static final ElementTypeDescription LEDDESCRIPTION
            = new ElementTypeDescription("LED", Out.class, input("in"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.LED_SIZE)
            .addAttribute(Keys.COLOR);

    /**
     * The polarity aware LED description
     */
    public static final ElementTypeDescription POLARITYAWARELEDDESCRIPTION
            = new ElementTypeDescription("PolarityAwareLED",
            attributes -> new Out(1, 1), input("A"), input("C"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.COLOR);

    /**
     * The seven segment display description
     */
    public static final ElementTypeDescription SEVENDESCRIPTION = new SevenSegTypeDescription();

    /**
     * The seven segment hex display description
     */
    public static final ElementTypeDescription SEVENHEXDESCRIPTION
            = new ElementTypeDescription("Seven-Seg-Hex",
            attributes -> new Out(4, 1), input("d"), input("dp"))
            .addAttribute(Keys.COLOR)
            .addAttribute(Keys.SEVEN_SEG_SIZE);

    /**
     * Sixteen Segment Display
     */
    public static final ElementTypeDescription SIXTEENDESCRIPTION
            = new ElementTypeDescription("SixteenSeg",
            attributes -> new Out(16, 1), input("led"), input("dp"))
            .addAttribute(Keys.COLOR)
            .addAttribute(Keys.SEVEN_SEG_SIZE);

    private final int[] bits;
    private final String label;
    private final String pinNumber;
    private final IntFormat format;
    private boolean enforceSignal = false;
    private ObservableValue value;

    /**
     * Creates a new instance
     *
     * @param attributes the attributes
     */
    public Out(ElementAttributes attributes) {
        bits = new int[]{attributes.getBits()};
        label = attributes.getLabel();
        pinNumber = attributes.get(Keys.PINNUMBER);
        format = attributes.get(Keys.INT_FORMAT);
    }

    /**
     * Creates a new instance
     *
     * @param bits the bitcount of the different inputs
     */
    public Out(int... bits) {
        this.bits = bits;
        label = null;
        pinNumber = "";
        format = null;
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        if (inputs.size() != bits.length)
            throw new NodeException("wrong input count");
        value = inputs.get(0).checkBits(bits[0], null);
        for (int i = 1; i < bits.length; i++)
            inputs.get(i).checkBits(bits[i], null);
    }

    @Override
    public ObservableValues getOutputs() {
        return ObservableValues.EMPTY_LIST;
    }

    @Override
    public void registerNodes(Model model) {
        final Signal signal = new Signal(label, value)
                .setPinNumber(pinNumber)
                .setFormat(format);
        if (enforceSignal || signal.isValid())
            model.addOutput(signal);
    }

    private Element enforceName() {
        enforceSignal = true;
        return this;
    }

    private final static class SevenSegTypeDescription extends ElementTypeDescription {
        private SevenSegTypeDescription() {
            super("Seven-Seg", attributes -> {
                if (attributes.get(Keys.COMMON_CATHODE))
                    return new Out(1, 1, 1, 1, 1, 1, 1, 1, 1);
                else
                    return new Out(1, 1, 1, 1, 1, 1, 1, 1);
            });
            addAttribute(Keys.COLOR);
            addAttribute(Keys.COMMON_CATHODE);
            addAttribute(Keys.LED_PERSISTENCE);
        }

        @Override
        public PinDescriptions getInputDescription(ElementAttributes attributes) throws NodeException {
            if (attributes.get(Keys.COMMON_CATHODE)) {
                return new PinDescriptions(
                        input("a"), input("b"), input("c"),
                        input("d"), input("e"), input("f"),
                        input("g"), input("dp"), input("cc")).setLangKey(getPinLangKey());
            } else
                return new PinDescriptions(
                        input("a"), input("b"), input("c"),
                        input("d"), input("e"), input("f"),
                        input("g"), input("dp")).setLangKey(getPinLangKey());
        }
    }
}
