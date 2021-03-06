package io.github.jitwxs.easydata.core.mock.mocker.explicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

public class BigDecimalMocker implements IMocker<BigDecimal> {

  @Override
  public BigDecimal mock(MockConfig mockConfig) {
    return BigDecimal.valueOf(RandomUtils.nextDouble(mockConfig.getFloatRange()[0], mockConfig.getFloatRange()[1]));
  }
}