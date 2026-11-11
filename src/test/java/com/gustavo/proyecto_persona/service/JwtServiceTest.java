package com.gustavo.proyecto_persona.service;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Nested
    class ExtractUsernameTests {

        @Test
        public void testExtractUsernameTokenNull() {
            String token = null;

           IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,()->jwtService.extractUsername(token));

           assertEquals("El token no puede ser null.", resultado.getMessage());
        }

        @Test
        public void testExtractUsernameTokenNoTienePuntos() {
            String token = "jsdhsodifdfdspfdsoifodsfds90f90f909f0wfwjfwjfpwj";

            MalformedJwtException resultado = assertThrows(MalformedJwtException.class,()->jwtService.extractUsername(token));

            assertEquals("El token JWT no tiene un formato válido.", resultado.getMessage());
        }

        @Test
        public void testExtractUsernameTokenTieneMasDeDosPuntos() {
            String token = "jsdhsodifdfd.spfdsoifodsfds90f90f90.9f0wfwjfw.jfpwj";

            MalformedJwtException resultado = assertThrows(MalformedJwtException.class,()->jwtService.extractUsername(token));

            assertEquals("El token JWT no tiene un formato válido.", resultado.getMessage());
        }

        @Test
        public void testExtractUsernameTokenVacio() {
            String token = "";

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,()->jwtService.extractUsername(token));

            assertEquals("El token no puede estar vacio.", resultado.getMessage());
        }

        @Test
        public void testExtractUsernameCadenaCorta() {
            String token = "eyJhbGciOiJIUzI";

            MalformedJwtException resultado = assertThrows(MalformedJwtException.class, ()->jwtService.extractUsername(token));

            assertEquals("El token JWT no tiene un formato válido.", resultado.getMessage());
        }

        @Test
        public void testExtractUsername() {
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.iX6NNHGhc7EoPhzY-_kv_cedSD3QP2b1woJmVr5RwR4";

            String resultado = jwtService.extractUsername(token);

            assertEquals("1234567890", resultado);


        }
    }

    @Nested
    class GenerateTokenTests {
        @Test
        public void testGenerateTokenUserDetailsNull() {
            UserDetails userDetails = null;
            assertThrows(IllegalArgumentException.class, () -> jwtService.generateToken(userDetails));
        }

        @Test
        public void testGeneratorToken() {

            UserDetails userDetails = User.withUsername("Gus123")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            String resultado = jwtService.generateToken(userDetails);
            String username = jwtService.extractUsername(resultado);

            assertEquals("Gus123", username);

            System.out.println(resultado);

        }
    }

    @Nested
    class IsTokenValidTest {

        @Test
        public void testIsTokenValidUserDetailsNull(){
            String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6MTc5NDAwNzQ5NH0.4vlzRd-gVqCPvDfm6T-w3lLknSMj1OnVrDLJ-FSpNb6cMjydBN-1RJzaAO6BXDQB";
            UserDetails userDetails = null;

            IllegalArgumentException resultado = assertThrows(IllegalArgumentException.class,()-> jwtService.isTokenValid(token, userDetails));

            assertEquals("UserDetails no puede ser nulo.", resultado.getMessage());
        }

        @Test
        public void testIsTokenValidTokenSinPuntos(){
            String token = "eyJhbGciOiJIUzM4NCJ9eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6MTc5NDAwNzQ5NH04vlzRd-gVqCPvDfm6T-w3lLknSMj1OnVrDLJ-FSpNb6cMjydBN-1RJzaAO6BXDQB";
            UserDetails userDetails = User.withUsername("Gus123")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            MalformedJwtException resultado = assertThrows(MalformedJwtException.class,()-> jwtService.isTokenValid(token, userDetails));

            assertEquals("El token JWT no tiene un formato válido.", resultado.getMessage());
        }

        @Test
        public void testIsTokenValidTokenDiferenteDeDosPuntos(){
            String token = "eyJhbGciOiJIUzM4NC.J9eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6MTc5NDAwNzQ5NH04vlzRd-gVqCPvDfm6T-w3lLknSMj1OnVrDLJ-FSpNb6cMjydBN-1RJzaAO6BXDQB";
            UserDetails userDetails = User.withUsername("Gus123")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            MalformedJwtException resultado = assertThrows(MalformedJwtException.class,()-> jwtService.isTokenValid(token, userDetails));

            assertEquals("El token JWT no tiene un formato válido.", resultado.getMessage());
        }

        @Test
        public void testIsTokenValid(){
            String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6NDEwMjQ0NDgwMH0.QqWPsBXbIemxKAAknhBaVzaWrHHtt9kXvnWv0fgoQSCudqEmMF1GLPw1shdO-nqJ";
            UserDetails userDetails = User.withUsername("Gus123")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            boolean resultado = jwtService.isTokenValid(token, userDetails);

            assertTrue(resultado);
        }

        @Test
        public void testIsTokenValidNoValido(){
            String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6MTc5NDAwNzQ5NH0.4vlzRd-gVqCPvDfm6T-w3lLknSMj1OnVrDLJ-FSpNb6cMjydBN-1RJzaAO6BXDQB";
            UserDetails userDetails = User.withUsername("Gus1234")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            boolean resultado = jwtService.isTokenValid(token, userDetails);

            assertFalse(resultado);
        }

        @Test
        public void testIsTokenValidExpirado(){
            String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJHdXMxMjMiLCJpYXQiOjE3OTM5NzE0OTQsImV4cCI6MTc5Mzk3MTAwMH0.3uZLIl0-J0QTzJtA0QJ15t_jKY55RKML4MCt9xHP2EgXVPjA1LKS7PYVZ7CHFRKX";
            UserDetails userDetails = User.withUsername("Gus123")
                    .roles("ADMIN")
                    .password("123456789")
                    .build();

            boolean resultado = jwtService.isTokenValid(token, userDetails);

            assertFalse(resultado);
        }
    }


}
