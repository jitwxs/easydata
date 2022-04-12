package com.github.jitwxs.easydata.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.common.bean.MockConfig;
import com.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 16:48
 */
public class DateMocker implements IMocker<Date> {
    @Override
    public Date mock(MockConfig mockConfig) {
        final long[] dateRange = mockConfig.getDateRange();

        return new Date(RandomUtils.nextLong(dateRange[0], dateRange[1]));
    }
}
