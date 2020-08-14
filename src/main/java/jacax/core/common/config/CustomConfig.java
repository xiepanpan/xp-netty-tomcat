package jacax.core.common.config;

import org.apache.log4j.Logger;
import sun.dc.pr.PRError;

import java.io.IOException;
import java.util.*;

/**
 * @author: xiepanpan
 * @Date: 2020/8/14 0014
 * @Description:  加载自定义配置
 */
public class CustomConfig {

    private Logger logger = Logger.getLogger(CustomConfig.class);

    private final static String PLACEHOLDER_START = "${";

    private static Map<String,String> ctx;

    /**
     * 加载配置文件
     * @param props
     */
    public static void load(String... props) {
        new CustomConfig(props);
    }

    private CustomConfig(String... props) {
        for (String path:props) {
            Properties properties = new Properties();
            try {
                properties.load(CustomConfig.class.getClassLoader().getResourceAsStream(path));
                resolvePlaceHolders(properties);
                ctx = new HashMap<String, String>();
                for (Object key:properties.keySet()) {
                    String keyStr = key.toString();
                    String value = properties.getProperty(keyStr);
                    logger.info(keyStr+":"+value);
                    ctx.put(keyStr,value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    /**
     * 获取所有的key值
     * @return
     */
    public static Set<String> getKeys() {
        return ctx.keySet();
    }

    public static String getString(String key) {
        return ctx.get(key);
    }

    private void resolvePlaceHolders(Properties properties) {
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            Object value = entry.getValue();
            if (value!=null && String.class.isInstance(value)) {
                String resolved = resolvePlaceHolder(properties, (String) value);
                if (!value.equals(resolved)) {
                    if (resolved == null) {
                        iterator.remove();
                    } else {
                        entry.setValue(resolved);
                    }
                }

            }
        }
    }

    /**
     * 解析占位符
     * @param properties
     * @param value
     * @return
     */
    private String resolvePlaceHolder(Properties properties, String value) {
        if (value.indexOf(PLACEHOLDER_START)<0) {
            return value;
        }
        StringBuffer stringBuffer = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int pos = 0;pos<chars.length;pos++) {
            if (chars[pos] == '$') {
                if (chars[pos+1]=='{') {
                    String key = "";
                    int x = pos+2;
                    for (;x<chars.length&& chars[x]!='}';x++) {
                        key += chars[x];
                        if (x== chars.length-1) {
                            throw new IllegalArgumentException("unmatched placeholder start ["+value+"]");
                        }
                    }
                    String val = extractFromSystem(properties, key);
                    stringBuffer.append(val == null ?"":val);
                    pos = x+1;
                    if (pos>=chars.length) {
                        break;
                    }
                }
            }
            stringBuffer.append(chars[pos]);
        }
        String string = stringBuffer.toString();
        return isEmpty(string)?null:string;
    }

    /**
     * 判断字符串是否为空 (null或者.length=0)
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string == null ||string.length()==0;
    }

    private String extractFromSystem(Properties properties, String key) {
        return properties.getProperty(key);
    }

}