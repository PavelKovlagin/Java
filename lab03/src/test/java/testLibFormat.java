import ru.jdbc.Auto;
import ru.jdbc.classAuto;
import ru.jdbc.connection;
import ru.other.libFormat;

public class testLibFormat {

    @org.junit.Test
    public void testFormatText() {
        System.out.println("\nТестирование метода форматирования текста");
        System.out.println(libFormat.format("space10", 10) + libFormat.format("space15", 15) + "end");
    }

    @org.junit.Test
    public void testFormatInt() {
        System.out.println("\nТестирование метода форматирования чисел");
        System.out.println(libFormat.format(12345, 10) + libFormat.format(1234,10));
        System.out.println(libFormat.format(123, 10) + libFormat.format(1234567,10));
    }
}
