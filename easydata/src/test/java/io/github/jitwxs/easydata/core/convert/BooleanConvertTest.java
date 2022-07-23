package io.github.jitwxs.easydata.core.convert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jitwxs
 * @see BooleanConvert
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BooleanConvertTest {
    @InjectMocks
    private BooleanConvert convert;

    @Test
    public void stringToBoolean() {
        assertTrue(convert.convert("true"));
        assertTrue(convert.convert("T"));
        assertTrue(convert.convert("y"));
        assertTrue(convert.convert("yes"));

        assertFalse(convert.convert("false"));
        assertFalse(convert.convert("F"));
        assertFalse(convert.convert("N"));
        assertFalse(convert.convert("no"));
    }

    @Test
    public void intToBoolean() {
        assertTrue(convert.convert(1));
        assertTrue(convert.convert(1L));

        assertFalse(convert.convert(0));
        assertFalse(convert.convert(0L));
    }
}