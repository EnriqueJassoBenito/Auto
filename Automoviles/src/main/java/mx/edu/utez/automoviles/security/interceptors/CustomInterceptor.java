package mx.edu.utez.automoviles.security.interceptors;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class CustomInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = convertIPv6toIPv4(request.getRemoteAddr());
        System.out.println("Revisando dirección IP antes de pasar al controller...");
        ip = convertIPv6toIPv4(ip);
        System.out.println(ip);

        if (ip.startsWith("192.168")) {
            System.out.println("La dirección IP está bloqueada");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "La dirección IP está bloqueada");
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("CERRANDO INTERCEPTOR");
    }


    private String convertIPv6toIPv4(String ipv6) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipv6);
            byte[] addressBytes = inetAddress.getAddress();

            if (addressBytes.length == 4) {
                return inetAddress.getHostAddress();
            }

            if ("0:0:0:0:0:0:0:1".equals(ipv6) || "::1".equals(ipv6)) {
                return "127.0.0.1";
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipv6;
    }


}
