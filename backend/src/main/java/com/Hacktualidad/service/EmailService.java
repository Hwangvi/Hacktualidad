package com.Hacktualidad.service;

import com.Hacktualidad.dto.CartDTO;
import com.Hacktualidad.dto.CartElementDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(UserResponseDTO user){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("2juanvicenterodriguez@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject(">> SYSTEM: ACCESO CONCEDIDO // HACKTUALIDAD");

            String colorAcento = "#00ff41";
            String html = String.format(
                    "<div style='background-color:#0d0d0d; color:#e0e0e0; padding:20px; font-family:monospace;'>" +
                            "<h2 style='color:%s;'>[+] NUEVO USUARIO DETECTADO EN EL SISTEMA</h2>" +
                            "<p>Bienvenido/a, <b>%s</b>.</p>" +
                            "<p>Tus credenciales han sido validadas correctamente.</p>" +
                            "<p>ID de Acceso: %d</p>" +
                            "<p style='color:%s;'>// Happy Hacking, Hacktualidad Team.</p>" +
                            "</div>",
                    colorAcento, user.getName(), user.getUserId(), colorAcento
            );

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendOrderConfirmation(String to, CartDTO cart) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("2juanvicenterodriguez@gmail.com");
            helper.setTo(to);
            helper.setSubject(">> SYSTEM: CONFIRMACIÓN DE PEDIDO // HACKTUALIDAD");

            String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String colorFondo = "#0d0d0d";
            String colorTexto = "#e0e0e0";
            String colorAcento = "#00ff41";
            String colorBorde = "#333333";

            StringBuilder html = new StringBuilder();

            html.append("<div style='font-family: \"Courier New\", Courier, monospace; background-color: ").append(colorFondo).append("; color: ").append(colorTexto).append("; padding: 20px; width: 100%; max-width: 600px; margin: 0 auto;'>");

            html.append("<div style='border-bottom: 2px solid ").append(colorAcento).append("; padding-bottom: 10px; margin-bottom: 20px;'>");
            html.append("<h2 style='color: ").append(colorAcento).append("; margin: 0; letter-spacing: 2px;'>// PEDIDO DE HACKTUALIDAD</h2>");
            html.append("<p style='font-size: 12px; margin: 5px 0 0 0; color: #888;'>PEDIDO REALIZADO CON EXITO</p>");
            html.append("</div>");

            html.append("<p style='color: ").append(colorAcento).append(";'><strong>>> ESTADO DEL PEDIDO:</strong> <span style='color: #fff;'>CONFIRMADO</span></p>");
            html.append("<p style='font-size: 12px;'>FECHA Y HORA DEL PEDIDO: ").append(fechaActual).append("</p>");
            html.append("<p style='font-size: 12px;'>PRODUCTOS COMPRADOS POR: ").append(to).append("</p>");
            html.append("<br>");

            html.append("<p>Muchas gracias por comprar en Hacktualidad. Esperamos que los productos satisfagan tu hambre de conocimiento en informática. Abajo la lista de productos comprados :</p>");

            html.append("<table style='width: 100%; border-collapse: collapse; margin-top: 20px; font-size: 14px;'>");

            html.append("<tr style='border-bottom: 1px dashed ").append(colorAcento).append("; color: ").append(colorAcento).append(";'>");
            html.append("<th style='text-align: left; padding: 10px;'>NOMBRE DEL PRODUCTO</th>");
            html.append("<th style='text-align: center; padding: 10px;'>CANTIDAD</th>");
            html.append("<th style='text-align: right; padding: 10px;'>PRECIO</th>");
            html.append("</tr>");

            for (CartElementDTO item : cart.getItems()) {
                html.append("<tr style='border-bottom: 1px solid ").append(colorBorde).append("; color: #fff;'>");
                html.append("<td style='padding: 10px;'>").append(item.getProduct().getName()).append("</td>");
                html.append("<td style='text-align: center; padding: 10px;'>").append(item.getQuantity()).append("</td>");
                html.append("<td style='text-align: right; padding: 10px;'>").append(String.format("%.2f", item.getSubtotal())).append(" €</td>");
                html.append("</tr>");
            }
            html.append("</table>");

            html.append("<div style='text-align: right; margin-top: 20px; padding-top: 10px; border-top: 2px solid ").append(colorAcento).append("; display: inline-block; width: 100%;'>");
            html.append("<span style='font-size: 14px; color: #888; margin-right: 10px;'>PRECIO TOTAL:</span>");
            html.append("<span style='font-size: 20px; color: ").append(colorAcento).append("; font-weight: bold;'>").append(String.format("%.2f", cart.getTotalAmount())).append(" €</span>");
            html.append("</div>");

            html.append("<br><br>");
            html.append("<div style='border-top: 1px dashed #444; padding-top: 10px; font-size: 10px; color: #666; text-align: center;'>");
            html.append("<p>Recibo de Hacktualidad S.L.</p>");
            html.append("<p>Si ha recibido este correo por error, por favor contacte al admin de Hacktualidad.</p>");
            html.append("<p style='color: ").append(colorAcento).append(";'> // Hacktualidad Inc. &copy; 2025 </p>");
            html.append("</div>");
            html.append("</div>");

            helper.setText(html.toString(), true);

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error enviando email: " + e.getMessage());
        }
    }
}