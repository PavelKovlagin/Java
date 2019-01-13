package processplan;

public class libFormat {

    public static String format(String str, int len)
    {
        return re(str, len);
    }

    public static String format(int num, int len)
    {
        return re(String.valueOf(num), len);
    }
    
    public static String re(String str, int len) {
        int space = len - str.length();
        for (int i=1; i<=space; i++)
            str = str + " ";
        return str;
    }
}
