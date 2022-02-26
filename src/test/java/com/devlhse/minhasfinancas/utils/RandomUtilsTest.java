package com.devlhse.minhasfinancas.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandomUtilsTest {

    @Test
    public void deveGerarUmNumeroAleatorioComSeisDigitos(){
        var randomNumber = RandomUtils.getSixDigitsRandomNumberString();
        assertNotNull(randomNumber);
        assertEquals(6, randomNumber.length());
    }
}
