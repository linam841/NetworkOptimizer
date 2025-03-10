package com.manilvit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkObjectParserTest {

    @Test
    public void testParseValidFile() throws IOException {
        // Формат: первая строка - количество соединений, затем сами соединения
        String fileContent = "3\n1 2 3\n2 3 1\n3 4 4\n";
        List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent);

        assertNotNull(connections, "Список соединений не должен быть null");
        assertEquals(3, connections.size(), "Ожидается 3 соединения");

        // Проверяем первое соединение
        NetworkConnection conn1 = connections.get(0);
        assertEquals(1, conn1.getNode1());
        assertEquals(2, conn1.getNode2());
        assertEquals(3, conn1.getCost());

        // Можно дополнительно проверить оставшиеся соединения
    }

    @Test
    public void testParseInvalidLineFormat() {
        // Вторая строка не соответствует ожидаемому формату (должно быть 3 числа)
        String fileContent = "2\n1 2 3\ninvalid_line";

        Exception exception = assertThrows(IOException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
        assertTrue(exception.getMessage().contains("Invalid line format"), "Должно быть сообщение об ошибке формата строки");
    }

    @Test
    public void testParseMismatchConnectionCount() {
        // Объявлено 3 соединения, а передано только 2
        String fileContent = "3\n1 2 3\n2 3 1\n";

        Exception exception = assertThrows(IOException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
        assertTrue(exception.getMessage().contains("does not match the number of nodes"), "Должно быть сообщение о несоответствии количества соединений");
    }

    @Test
    public void testParseNonNumericFirstLine() {
        // Первая строка не является числом
        String fileContent = "not_a_number\n1 2 3\n2 3 1\n";

        // При попытке парсинга первой строки ожидается NumberFormatException
        assertThrows(NumberFormatException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
    }

    @Test
    public void testParseWithExtraWhitespace() throws IOException {
        // Проверяем, что лишние пробелы обрезаются корректно
        String fileContent = " 3 \n 1   2  3 \n2 3    1\n3 4 4\n";
        List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent);

        assertNotNull(connections);
        assertEquals(3, connections.size());
    }
}