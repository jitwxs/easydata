package com.github.jitwxs.addax.core.mock.strings;

import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.core.mock.Mock;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * 中国身份证号生成器
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:05
 */
public class CNIdCardGenerator implements IStringGenerator {

    final Map<String, String> areaCodeMap = new HashMap<String, String>() {{
        put("11", "北京");
        put("12", "天津");
        put("13", "河北");
        put("14", "山西");
        put("15", "内蒙古");
        put("21", "辽宁");
        put("22", "吉林");
        put("23", "黑龙江");
        put("31", "上海");
        put("32", "江苏");
        put("33", "浙江");
        put("34", "安徽");
        put("35", "福建");
        put("36", "江西");
        put("37", "山东");
        put("41", "河南");
        put("42", "湖北");
        put("43", "湖南");
        put("44", "广东");
        put("45", "广西");
        put("46", "海南");
        put("50", "重庆");
        put("51", "四川");
        put("52", "贵州");
        put("53", "云南");
        put("54", "西藏");
        put("61", "陕西");
        put("62", "甘肃");
        put("63", "青海");
        put("64", "宁夏");
        put("65", "新疆");
        put("71", "台湾");
        put("81", "香港");
        put("82", "澳门");
        put("91", "国外");
    }};

    @Override
    public String generator(MockConfig mockConfig) {
        String areaCode = areaCodeMap.keySet().toArray(new String[0])[nextInt(0, areaCodeMap.size())] +
                StringUtils.leftPad((nextInt(0, 9998) + 1) + "", 4, "0");

        String birthday = new SimpleDateFormat("yyyyMMdd").format(Mock.run(Date.class, mockConfig));
        String randomCode = String.valueOf(1000 + nextInt(0, 999)).substring(1);
        String pre = areaCode + birthday + randomCode;
        String verifyCode = getVerifyCode(pre);
        String result = pre + verifyCode;

        return result;
    }

    @Override
    public MockStringEnum type() {
        return MockStringEnum.CN_ID_CARD;
    }

    private static String getVerifyCode(String cardId) {
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int tmp = 0;
        for (int i = 0; i < Wi.length; i++) {
            tmp += Integer.parseInt(String.valueOf(cardId.charAt(i))) * Integer.parseInt(Wi[i]);
        }

        int modValue = tmp % 11;
        return ValCodeArr[modValue];
    }
}
