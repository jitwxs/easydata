package com.github.jitwxs.addax.core.mock.mocker.explicit;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.core.mock.mocker.IMocker;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

public class BigDecimalMocker implements IMocker<BigDecimal> {

  @Override
  public BigDecimal mock(MockConfig mockConfig) {
    return BigDecimal.valueOf(RandomUtils.nextDouble(mockConfig.getFloatRange()[0], mockConfig.getFloatRange()[1]));
  }
}