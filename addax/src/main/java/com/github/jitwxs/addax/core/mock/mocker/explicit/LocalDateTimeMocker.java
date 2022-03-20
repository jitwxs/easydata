package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.util.TimeUtils;
import com.github.jitwxs.addax.core.mock.Mock;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 16:48
 */
public class LocalDateTimeMocker implements IMocker<LocalDateTime> {
    @Override
    public LocalDateTime mock(MockConfig mockConfig) {
        final Date date = Mock.run(Date.class);

        return date == null ? null : TimeUtils.dateToLdt(date);
    }
}
