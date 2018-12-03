package ru.other;

import javax.xml.bind.ValidationEvent;

public class libFormat {

    public static String format(String str, int len)
    {
        int space = len - str.length();
        for (int i=1; i<=space; i++)
        {
            str = str + " ";
        }

        return  str;
    }

    public static String format(int num, int len)
    {
        String str = String.valueOf(num);
        int space = len - str.length();
        for (int i=1; i<=space; i++)
        {
            str = str + " ";
        }
        return  str;
    }
}
