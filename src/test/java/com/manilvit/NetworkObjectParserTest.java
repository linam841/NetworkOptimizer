package com.manilvit;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link NetworkObjectParser}.
 */
public class NetworkObjectParserTest {

    /**
     * Test case to parse a valid file content and check if the connections are parsed correctly.
     */
    @Test
    public void testParseValidFile() throws IOException {
        // Format: first line - number of connections, followed by the connections themselves
        String fileContent = "3\n1 2 3\n2 3 1\n3 4 4\n";
        List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent);

        assertNotNull(connections, "The list of connections should not be null");
        assertEquals(3, connections.size(), "Expected 3 connections");

        // Check the first connection
        NetworkConnection conn1 = connections.get(0);
        assertEquals(1, conn1.getNode1());
        assertEquals(2, conn1.getNode2());
        assertEquals(3, conn1.getCost());

        // Additional checks for other connections can be added here
    }

    /**
     * Test case for an invalid line format in the file.
     * The second line does not conform to the expected format (should contain 3 numbers).
     */
    @Test
    public void testParseInvalidLineFormat() {
        String fileContent = "2\n1 2 3\ninvalid_line";

        Exception exception = assertThrows(IOException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
        assertTrue(exception.getMessage().contains("Invalid line format"), "The error message should mention 'Invalid line format'");
    }

    /**
     * Test case for mismatched connection count.
     * Declared 3 connections, but only 2 were provided.
     */
    @Test
    public void testParseMismatchConnectionCount() {
        String fileContent = "3\n1 2 3\n2 3 1\n";

        Exception exception = assertThrows(IOException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
        assertTrue(exception.getMessage().contains("does not match the number of nodes"), "The error message should mention 'does not match the number of nodes'");
    }

    /**
     * Test case for non-numeric first line in the file.
     * The first line is expected to be a number, so it should throw a NumberFormatException.
     */
    @Test
    public void testParseNonNumericFirstLine() {
        String fileContent = "not_a_number\n1 2 3\n2 3 1\n";

        assertThrows(NumberFormatException.class, () -> {
            NetworkObjectParser.parse(fileContent);
        });
    }

    /**
     * Test case to ensure that extra whitespace is trimmed correctly.
     */
    @Test
    public void testParseWithExtraWhitespace() throws IOException {
        String fileContent = " 3 \n 1   2  3 \n2 3    1\n3 4 4\n";
        List<NetworkConnection> connections = NetworkObjectParser.parse(fileContent);

        assertNotNull(connections, "The list of connections should not be null");
        assertEquals(3, connections.size(), "Expected 3 connections after trimming whitespace");
    }
}