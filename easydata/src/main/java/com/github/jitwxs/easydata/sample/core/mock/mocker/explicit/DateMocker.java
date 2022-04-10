package com.github.jitwxs.easydata.sample.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.common.exception.EasyDataMockException;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 16:48
 */
public class DateMocker implements IMocker<Date> {
    @Override
    public Date mock(MockConfig mockConfig) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try {
            final String[] dateRange = mockConfig.getDateRange();

            final long startTime = format.parse(dateRange[0]).getTime();
            final long endTime = format.parse(dateRange[1]).getTime();

            return new Date(RandomUtils.nextLong(startTime, endTime));
        } catch (ParseException e) {
            throw new EasyDataMockException(e);
        }
    }
}
