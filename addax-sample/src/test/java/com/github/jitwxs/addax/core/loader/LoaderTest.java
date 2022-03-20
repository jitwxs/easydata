package com.github.jitwxs.addax.core.loader;

import com.github.jitwxs.addax.conn.FileConnection;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import com.github.jitwxs.addax.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {
    @Test
    public void loading() {
        final Loader loader = new Loader(new FileConnection());

        final List<OrderEvaluate> evaluates = loader.loading(OrderEvaluate.class);

        assertEquals(1, evaluates.size());
    }
}