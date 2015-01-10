package tokenizer;

import org.junit.Before;
import org.junit.Test;
import tokenizer.TokenizerImpl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static org.junit.Assert.*;

public class TokenizerImplTest {

    TokenizerImpl _tokenizer;
    Socket client;
    InputStreamReader in;

    @Before
    public void setUp() throws Exception {
        _tokenizer = new TokenizerImpl();
        client = new Socket();
        in = new InputStreamReader(client.getInputStream(),"UTF-8");
        _tokenizer.addInputStream(in);
    }

    @Test
    public void testNextMessage() throws Exception {
        OutputStreamWriter osw = new OutputStreamWriter(client.getOutputStream(),"UTF-8");
        osw.write("strat");
        assertEquals("start",_tokenizer.nextMessage());
    }

    @Test
    public void testIsAlive() throws Exception {

    }

    @Test
    public void testAddInputStream() throws Exception {

    }
}