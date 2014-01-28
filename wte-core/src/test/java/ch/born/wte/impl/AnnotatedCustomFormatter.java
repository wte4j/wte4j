package ch.born.wte.impl;

import ch.born.wte.DefaultFormatter;
import ch.born.wte.FormatterName;
import ch.born.wte.impl.format.ToStringFormatter;

@FormatterName("custom")
@DefaultFormatter({ Integer.class })
public class AnnotatedCustomFormatter extends ToStringFormatter {

}