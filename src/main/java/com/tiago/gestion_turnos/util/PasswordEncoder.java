package com.tiago.gestion_turnos.util;

import org.springframework.stereotype.Component;

import com.password4j.BcryptFunction;
import com.password4j.Password;
import com.password4j.types.Bcrypt;

@Component
public class PasswordEncoder {
    private static final String SHARED_SECRET = "mi_sistema_de_turnos_shared_secret";
    private static final int LOG_ROUNDS = 15;

    /**
     * Codifica una contraseña en texto plano usando BCrypt.
     *
     * rawPassword Contraseña sin codificar
     * retorna un Hash seguro de la contraseña
     */
    public String encode(String rawPassword) {
        // Creamos una función BCrypt configurada con el tipo y número de rondas
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, LOG_ROUNDS);

        // Generamos el hash:
        //  - Password.hash() crea el objeto hash
        //  - addPepper() agrega la "pepper" secreta
        //  - with(bcrypt) aplica el algoritmo configurado
        //  - getResult() devuelve el hash final como String
        return Password.hash(rawPassword)
                .addPepper(SHARED_SECRET)
                .with(bcrypt)
                .getResult();
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash guardado.
     *
     * rawPassword Contraseña ingresada por el usuario
     * encodedPassword Hash almacenado en la base de datos
     * retorna true si coinciden, false en caso contrario
     */
    public boolean verify(String rawPassword, String encodedPassword) {
        // Se usa la misma configuración de BCrypt que en encode()
        BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, LOG_ROUNDS);

        // Password.check() compara la contraseña original con el hash,
        // agregando la misma "pepper" y usando la misma función.
        return Password.check(rawPassword, encodedPassword)
                .addPepper(SHARED_SECRET)
                .with(bcrypt);
    }
}
