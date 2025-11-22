package UI.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase de utilidad para aplicar estilos modernos a componentes Swing
 */
public class ModernStyles {
    
    // Colores del diseño web (rosados/morados elegantes)
    public static final Color PRIMARY_COLOR = new Color(212, 165, 199); // #d4a5c7 - Rosa principal
    public static final Color PRIMARY_DARK = new Color(201, 139, 184); // #c98bb8 - Rosa oscuro
    public static final Color PRIMARY_LIGHT = new Color(255, 107, 157); // #ff6b9d - Rosa brillante
    public static final Color PRIMARY_DEEP = new Color(184, 112, 160); // #b870a0 - Rosa profundo
    public static final Color SECONDARY_COLOR = new Color(107, 45, 77); // #6b2d4d - Morado oscuro
    public static final Color SECONDARY_MID = new Color(139, 74, 107); // #8b4a6b - Morado medio
    public static final Color SECONDARY_LIGHT = new Color(168, 85, 122); // #a8557a - Morado claro
    
    // Colores de fondo (gradientes rosados suaves)
    public static final Color BG_LIGHT = new Color(232, 213, 227); // #e8d5e3 - Rosa muy claro
    public static final Color BG_MID = new Color(240, 224, 235); // #f0e0eb - Rosa medio claro
    public static final Color BG_PALE = new Color(245, 230, 240); // #f5e6f0 - Rosa pálido
    public static final Color BG_WHITE = new Color(255, 255, 255);
    public static final Color BG_DARK = new Color(10, 10, 10); // #0a0a0a - Negro para footer
    
    // Colores de texto
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51); // #333
    public static final Color TEXT_SECONDARY = new Color(102, 102, 102); // #666
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    
    // Colores adicionales
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color INFO_COLOR = new Color(102, 126, 234); // #667eea - Morado azulado para auth
    
    // Fuentes modernas con jerarquía tipográfica mejorada (similar a web: Playfair Display + Poppins)
    private static final String[] FUENTES_TITULOS = {
        "Playfair Display",
        "Georgia",
        "Times New Roman",
        "Serif"
    };
    
    private static final String[] FUENTES_CUERPO = {
        "Poppins",
        "Segoe UI",
        "Roboto",
        "Open Sans",
        "Inter",
        "SF Pro Display",
        "Helvetica Neue",
        "Arial"
    };
    
    /**
     * Obtiene la mejor fuente disponible del sistema para títulos (serif)
     */
    private static Font getBestTitleFont(int style, int size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();
        
        // Buscar fuente serif para títulos (como Playfair Display)
        for (String preferredFont : FUENTES_TITULOS) {
            for (String availableFont : availableFonts) {
                if (preferredFont.equalsIgnoreCase(availableFont)) {
                    Font font = new Font(preferredFont, style, size);
                    return font.deriveFont(font.getStyle(), (float) size);
                }
            }
        }
        
        // Fallback a fuente serif del sistema
        return new Font(Font.SERIF, style, size).deriveFont((float) size);
    }
    
    /**
     * Obtiene la mejor fuente disponible del sistema para cuerpo (sans-serif)
     */
    private static Font getBestBodyFont(int style, int size) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();
        
        // Buscar fuente sans-serif para cuerpo (como Poppins)
        for (String preferredFont : FUENTES_CUERPO) {
            for (String availableFont : availableFonts) {
                if (preferredFont.equalsIgnoreCase(availableFont)) {
                    Font font = new Font(preferredFont, style, size);
                    return font.deriveFont(font.getStyle(), (float) size);
                }
            }
        }
        
        // Fallback a Arial si no se encuentra ninguna
        return new Font("Arial", style, size).deriveFont((float) size);
    }
    
    /**
     * Obtiene la mejor fuente disponible del sistema (método genérico)
     */
    private static Font getBestAvailableFont(int style, int size) {
        return getBestBodyFont(style, size);
    }
    
    /**
     * Obtiene una fuente con renderizado mejorado
     */
    public static Font getFont(int style, int size) {
        Font font = getBestAvailableFont(style, size);
        
        // Aplicar mejoras de renderizado
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        attributes.put(TextAttribute.LIGATURES, TextAttribute.LIGATURES_ON);
        if (style == Font.PLAIN) {
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_REGULAR);
        } else if (style == Font.BOLD) {
            attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        }
        
        return font.deriveFont(attributes);
    }
    
    /**
     * Fuente regular (normal)
     */
    public static Font getRegularFont(int size) {
        return getFont(Font.PLAIN, size);
    }
    
    /**
     * Fuente negrita
     */
    public static Font getBoldFont(int size) {
        return getFont(Font.BOLD, size);
    }
    
    /**
     * Fuente semibold
     */
    public static Font getSemiboldFont(int size) {
        Font font = getBestAvailableFont(Font.PLAIN, size);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_SEMIBOLD);
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    /**
     * Fuente light
     */
    public static Font getLightFont(int size) {
        Font font = getBestAvailableFont(Font.PLAIN, size);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_LIGHT);
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    // Jerarquía tipográfica (títulos con serif, cuerpo con sans-serif)
    public static Font getHeading1Font() {
        Font font = getBestTitleFont(Font.BOLD, 32);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    public static Font getHeading2Font() {
        Font font = getBestTitleFont(Font.BOLD, 28);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    public static Font getHeading3Font() {
        Font font = getBestTitleFont(Font.BOLD, 24);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    public static Font getHeading4Font() {
        Font font = getBestTitleFont(Font.BOLD, 20);
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        return font.deriveFont(attributes);
    }
    
    public static Font getBodyLargeFont() {
        return getRegularFont(16);
    }
    
    public static Font getBodyFont() {
        return getRegularFont(14);
    }
    
    public static Font getBodySmallFont() {
        return getRegularFont(12);
    }
    
    public static Font getCaptionFont() {
        return getRegularFont(11);
    }
    
    /**
     * Estiliza un botón con diseño moderno (estilo web con gradiente)
     */
    public static void styleButton(JButton button, Color backgroundColor) {
        button.setFont(getSemiboldFont(14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkenColor(originalColor, 0.1f));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }
    
    /**
     * Estiliza un botón primario (con gradiente rosado como web)
     */
    public static void stylePrimaryButton(JButton button) {
        styleButton(button, PRIMARY_COLOR);
        // Agregar efecto de sombra similar a web
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(12, 24, 12, 24),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
    }
    
    /**
     * Estiliza un botón secundario (con gradiente morado para auth)
     */
    public static void styleSecondaryButton(JButton button) {
        styleButton(button, INFO_COLOR);
    }
    
    /**
     * Estiliza un botón de autenticación (gradiente morado como web)
     */
    public static void styleAuthButton(JButton button) {
        styleButton(button, INFO_COLOR);
    }
    
    /**
     * Estiliza un campo de texto moderno (estilo web)
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(getBodyFont());
        textField.setForeground(TEXT_PRIMARY);
        textField.setBackground(new Color(248, 249, 250)); // #f8f9fa como web
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 229, 233), 2), // #e1e5e9 como web
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        textField.setOpaque(true);
    }
    
    /**
     * Estiliza un campo de contraseña moderno (estilo web)
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(getBodyFont());
        passwordField.setForeground(TEXT_PRIMARY);
        passwordField.setBackground(new Color(248, 249, 250)); // #f8f9fa como web
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 229, 233), 2), // #e1e5e9 como web
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        passwordField.setOpaque(true);
    }
    
    /**
     * Estiliza un panel con sombra y bordes redondeados
     */
    public static void stylePanel(JPanel panel, Color backgroundColor) {
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
    }
    
    /**
     * Estiliza un panel de tarjeta (card)
     */
    public static void styleCardPanel(JPanel panel) {
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }
    
    /**
     * Estiliza un label con tipografía moderna
     */
    public static void styleLabel(JLabel label, int fontSize, Color textColor) {
        label.setFont(getRegularFont(fontSize));
        label.setForeground(textColor);
    }
    
    /**
     * Estiliza un label de título principal
     */
    public static void styleTitleLabel(JLabel label) {
        label.setFont(getHeading2Font());
        label.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Estiliza un label de título grande
     */
    public static void styleLargeTitleLabel(JLabel label) {
        label.setFont(getHeading1Font());
        label.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Estiliza un label de subtítulo
     */
    public static void styleSubtitleLabel(JLabel label) {
        label.setFont(getBodyLargeFont());
        label.setForeground(TEXT_SECONDARY);
    }
    
    /**
     * Estiliza un label de cuerpo de texto
     */
    public static void styleBodyLabel(JLabel label) {
        label.setFont(getBodyFont());
        label.setForeground(TEXT_PRIMARY);
    }
    
    /**
     * Estiliza un label de texto pequeño
     */
    public static void styleSmallLabel(JLabel label) {
        label.setFont(getBodySmallFont());
        label.setForeground(TEXT_SECONDARY);
    }
    
    /**
     * Oscurece un color
     */
    private static Color darkenColor(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Aclara un color
     */
    private static Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b);
    }
    
    /**
     * Crea un panel con gradiente (estilo web)
     */
    public static JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();
                // Gradiente diagonal como en web (135deg)
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
    }
    
    /**
     * Crea un panel con gradiente de fondo rosado (como el body de la web)
     */
    public static JPanel createPinkGradientPanel() {
        return createGradientPanel(BG_LIGHT, BG_PALE);
    }
    
    /**
     * Crea un panel con gradiente de header rosado (como header-top de la web)
     */
    public static JPanel createHeaderGradientPanel() {
        return createGradientPanel(PRIMARY_COLOR, PRIMARY_DARK);
    }
    
    /**
     * Aplica Look and Feel moderno con tipografía mejorada
     */
    public static void applyModernLookAndFeel() {
        try {
            // Intentar usar Nimbus
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            // Personalizar colores de Nimbus
            UIManager.put("nimbusBase", PRIMARY_COLOR);
            UIManager.put("nimbusBlueGrey", new Color(173, 216, 230));
            UIManager.put("control", BG_LIGHT);
            
            // Configurar fuentes globales para todos los componentes
            Font defaultFont = getBodyFont();
            UIManager.put("Button.font", getSemiboldFont(14));
            UIManager.put("Label.font", getBodyFont());
            UIManager.put("TextField.font", getBodyFont());
            UIManager.put("PasswordField.font", getBodyFont());
            UIManager.put("TextArea.font", getBodyFont());
            UIManager.put("TextPane.font", getBodyFont());
            UIManager.put("EditorPane.font", getBodyFont());
            UIManager.put("ComboBox.font", getBodyFont());
            UIManager.put("List.font", getBodyFont());
            UIManager.put("Table.font", getBodyFont());
            UIManager.put("TableHeader.font", getSemiboldFont(13));
            UIManager.put("Menu.font", getBodyFont());
            UIManager.put("MenuItem.font", getBodyFont());
            UIManager.put("CheckBox.font", getBodyFont());
            UIManager.put("RadioButton.font", getBodyFont());
            UIManager.put("ToggleButton.font", getSemiboldFont(13));
            UIManager.put("TabbedPane.font", getBodyFont());
            UIManager.put("ToolTip.font", getCaptionFont());
            UIManager.put("TitledBorder.font", getSemiboldFont(13));
            UIManager.put("OptionPane.messageFont", getBodyFont());
            UIManager.put("OptionPane.buttonFont", getSemiboldFont(13));
            
            // Configurar renderizado mejorado
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Aplicar fuentes incluso si falla Nimbus
                Font defaultFont = getBodyFont();
                UIManager.put("Button.font", getSemiboldFont(14));
                UIManager.put("Label.font", getBodyFont());
                UIManager.put("TextField.font", getBodyFont());
            } catch (Exception ex) {
                // Usar look and feel por defecto
            }
        }
    }
    
    /**
     * Configura el renderizado de texto mejorado para un componente
     */
    public static void enableTextAntialiasing(JComponent component) {
        component.putClientProperty("awt.font.desktophints", 
            Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints"));
    }
}

