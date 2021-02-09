
package cn.leo.aplugin.tools


import org.objectweb.asm.Opcodes
import java.lang.reflect.Array
import java.lang.reflect.Field
import java.util.concurrent.ConcurrentHashMap

class Logger {

    enum LogUI {
        //color
        C_ERROR("\033[40;31m"),
        C_WARN("\033[40;33m"),
        C_BLACK_GREEN("\033[7m"),
        //end
        E_NORMAL("\033[0m");

        private final String value

        LogUI(String value) {
            this.value = value
        }

        String getValue() {
            return this.value
        }
    }

    private static boolean debug = false
    public static ConcurrentHashMap<Integer, String> accCodeMap = new ConcurrentHashMap<>()
    public static ConcurrentHashMap<Integer, String> opCodeMap = new ConcurrentHashMap<>()

    /**
     * 打印提示信息
     */
    static void printCopyright() {
        println()
        println("${LogUI.C_BLACK_GREEN.value}" + "####################################################################" + "${LogUI.E_NORMAL.value}")
        println("${LogUI.C_BLACK_GREEN.value}" + "##                                                                ##" + "${LogUI.E_NORMAL.value}")
        println("${LogUI.C_BLACK_GREEN.value}" + "##                   Welcome to use APlugin                       ##"+ "${LogUI.E_NORMAL.value}")
        println("${LogUI.C_BLACK_GREEN.value}" + "##         Contact Me : https://github.com/fanrunqi/APlugin       ##" + "${LogUI.E_NORMAL.value}")
        println("${LogUI.C_BLACK_GREEN.value}" + "##                                                                ##" + "${LogUI.E_NORMAL.value}")
        println("${LogUI.C_BLACK_GREEN.value}" + "####################################################################" + "${LogUI.E_NORMAL.value}")
        println()
    }

    /**
     * 设置是否打印日志
     */
    static void setDebug(boolean isDebug) {
        debug = isDebug
    }

    static boolean isDebug() {
        return debug
    }

    def static error(Object msg) {
        try {
            println("${LogUI.C_ERROR.value}[SensorsAnalytics]: ${msg}${LogUI.E_NORMAL.value}")
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def static warn(Object msg) {
        try {
            println("${LogUI.C_WARN.value}[SensorsAnalytics]: ${msg}${LogUI.E_NORMAL.value}")
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    /**
     * 打印日志
     */
    def static info(Object msg) {
        if (debug)
            try {
                println "[SensorsAnalytics]: ${msg}"
            } catch (Exception e) {
                e.printStackTrace()
            }
    }

    def static logForEach(Object... msg) {
        if (!debug) {
            return
        }
        msg.each {
            Object m ->
                try {
                    if (m != null) {
                        if (m.class.isArray()) {
                            print "["
                            def length = Array.getLength(m);
                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    def get = Array.get(m, i);
                                    if (get != null) {
                                        print "${get}\t"
                                    } else {
                                        print "null\t"
                                    }
                                }
                            }
                            print "]\t"
                        } else {
                            print "${m}\t"
                        }
                    } else {
                        print "null\t"
                    }
                } catch (Exception e) {
                    e.printStackTrace()
                }
        }
        println ""
    }

    static String accCode2String(int access) {
        def builder = new StringBuilder()
        def map = getAccCodeMap()
        map.each { key, value ->
            if ((key.intValue() & access) > 0) {
                builder.append(value + ' ')
            }
        }
        return builder.toString()
    }

    private static Map<Integer, String> getAccCodeMap() {
        if (accCodeMap.isEmpty()) {
            Field[] fields = Opcodes.class.getDeclaredFields()
            HashMap<Integer, String> tmpMap = [:]
            fields.each {
                if (it.name.startsWith("ACC_")) {
                    if (it.type == Integer.class) {
                        tmpMap[it.get(null) as Integer] = it.name
                    } else {
                        tmpMap[it.getInt(null)] = it.name
                    }
                }
            }
            accCodeMap.putAll(tmpMap)
        }
        return accCodeMap
    }

    static Map<Integer, String> getOpMap() {
        if (opCodeMap.size() == 0) {
            HashMap<String, Integer> map = [:]
            Field[] fields = Opcodes.class.getDeclaredFields()
            fields.each {
                if (it.type == Integer.class) {
                    map[it.get(null) as Integer] = it.name
                } else {
                    map[it.getInt(null)] = it.name
                }
            }
            opCodeMap.putAll(map)
        }
        return opCodeMap
    }
}