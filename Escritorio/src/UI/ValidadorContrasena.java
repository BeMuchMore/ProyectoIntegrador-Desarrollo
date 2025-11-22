package UI;

import java.util.regex.Pattern;

public class ValidadorContrasena {
    
    public enum NivelSeguridad {
        FACIL("Fácil", "red"),
        MEDIO("Medio", "orange"),
        DIFICIL("Difícil", "yellow"),
        AVANZADO("Avanzado", "green");
        
        private final String texto;
        private final String color;
        
        NivelSeguridad(String texto, String color) {
            this.texto = texto;
            this.color = color;
        }
        
        public String getTexto() {
            return texto;
        }
        
        public String getColor() {
            return color;
        }
    }
    
    // Evaluar el nivel de seguridad de la contraseña
    public static NivelSeguridad evaluarContrasena(String contrasena) {
        int puntuacion = 0;
        
        // Verificar longitud
        if (contrasena.length() >= 8) {
            puntuacion++;
        }
        if (contrasena.length() >= 12) {
            puntuacion++;
        }
        if (contrasena.length() >= 16) {
            puntuacion++;
        }
        
        // Verificar si tiene letras mayúsculas
        if (Pattern.compile("[A-Z]").matcher(contrasena).find()) {
            puntuacion++;
        }
        
        // Verificar si tiene letras minúsculas
        if (Pattern.compile("[a-z]").matcher(contrasena).find()) {
            puntuacion++;
        }
        
        // Verificar si tiene números
        if (Pattern.compile("[0-9]").matcher(contrasena).find()) {
            puntuacion++;
        }
        
        // Verificar si tiene caracteres especiales
        if (Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(contrasena).find()) {
            puntuacion += 2;
        }
        
        // Penalizar patrones comunes
        if (contienePatronesComunes(contrasena)) {
            puntuacion -= 2;
        }
        
        // Determinar nivel según puntuación
        if (puntuacion <= 2) {
            return NivelSeguridad.FACIL;
        } else if (puntuacion <= 4) {
            return NivelSeguridad.MEDIO;
        } else if (puntuacion <= 6) {
            return NivelSeguridad.DIFICIL;
        } else {
            return NivelSeguridad.AVANZADO;
        }
    }
    
    // Verificar patrones comunes
    private static boolean contienePatronesComunes(String contrasena) {
        String contrasenaMinus = contrasena.toLowerCase();
        String[] patronesComunes = {
            "123456", "password", "12345678", "qwerty", "abc123",
            "123456789", "12345", "1234", "111111", "1234567",
            "admin", "123123", "letmein", "welcome", "monkey"
        };
        
        for (String patron : patronesComunes) {
            if (contrasenaMinus.contains(patron)) {
                return true;
            }
        }
        
        // Verificar secuencias consecutivas
        if (tieneSecuenciasConsecutivas(contrasena)) {
            return true;
        }
        
        return false;
    }
    
    // Verificar secuencias consecutivas
    private static boolean tieneSecuenciasConsecutivas(String contrasena) {
        int consecutivos = 1;
        char prevChar = contrasena.charAt(0);
        
        for (int i = 1; i < contrasena.length(); i++) {
            char currentChar = contrasena.charAt(i);
            if (currentChar == prevChar + 1 || currentChar == prevChar - 1) {
                consecutivos++;
                if (consecutivos >= 3) {
                    return true;
                }
            } else {
                consecutivos = 1;
            }
            prevChar = currentChar;
        }
        
        return false;
    }
    
    // Generar sugerencias para mejorar la contraseña
    public static String obtenerSugerencias(String contrasena) {
        StringBuilder sugerencias = new StringBuilder("Sugerencias para mejorar tu contraseña:\n");
        
        if (contrasena.length() < 8) {
            sugerencias.append("• La contraseña debe tener al menos 8 caracteres\n");
        }
        
        if (!Pattern.compile("[A-Z]").matcher(contrasena).find()) {
            sugerencias.append("• Añade al menos una letra mayúscula\n");
        }
        
        if (!Pattern.compile("[a-z]").matcher(contrasena).find()) {
            sugerencias.append("• Añade al menos una letra minúscula\n");
        }
        
        if (!Pattern.compile("[0-9]").matcher(contrasena).find()) {
            sugerencias.append("• Añade al menos un número\n");
        }
        
        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(contrasena).find()) {
            sugerencias.append("• Añade al menos un carácter especial (!@#$%^&*)\n");
        }
        
        return sugerencias.toString();
    }
}