package org.example;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.*;
public class Main {
    static Scanner teclado = new Scanner(System.in);
    static boolean[][][][] asientos = new boolean[3][3][12][17];
    static final double PRECIO_GENERAL = 14.50;
    static final double PRECIO_NIÑOS = 12.50;
    static final double PRECIO_ADULTO_MAYOR = 12.50;
    static final double PRECIO_CONADIS = 7.00;
    static final double PRECIO_PROMO_SOCIO = 20.00;
    static final double PRECIO_SOCIO = 5.00;
    static final double TASA_IGV = 0.18;
    static String detalleDulceria = "";
    static int contadorBoleta = 0;
    static final long TIEMPO_BLOQUEO = 5 * 60 * 1000L;
    static long[][][][] bloqueoHasta = new long[3][3][12][17];
    static int[][][][] bloqueoCliente = new int[3][3][12][17];
    static int clienteActual = 0;
    static String[] insumos = {"Popcorn gigante Salado", "Popcorn gigante Dulce",
            "Popcorn gigante Mixto", "Gaseosa 32 oz", "Hot Dog"};
    static int[] stockInsumos = {20, 20, 20, 50, 20};
    static int[] stockMinimo = {4, 4, 4, 10, 4};
    static int[] vendidosInsumos = new int[5];
    static int[] consumoCompra = new int[5]; // reservado en la compra actual
    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n==============================================");
            System.out.println("       CINEPLANET - REAL PLAZA JULIACA");
            System.out.println("==============================================");
            System.out.println(" [1]. Peliculas y compra de entradas");
            System.out.println(" [2]. Dulceria");
            System.out.println(" [3]. Socio Cineplanet");
            System.out.println(" [4]. Salir");
            System.out.println("----------------------------------------------");
            opcion = leerEntero("Elija una opcion: ");
            switch (opcion) {
                case 1:
                    comprarEntradas();
                    break;
                case 2:
                    menuDulceria();
                    break;
                case 3:
                    menuSocio();
                    break;
                case 4:
                    System.out.println("Gracias por visitar Cineplanet Real Plaza Juliaca.");
                    break;
                default:
                    System.out.println(" [!] Opcion no valida.");
            }
        } while (opcion != 4);
    }
    public static void comprarEntradas() {
        String[] peliculas = {
                "Pinocho: Sin Cuerdas",
                "Impacto Mortal",
                "Resident Evil"
        };
        String[][] funciones = {
                {"12:00 PM", "3:30 PM", "7:00 PM"},
                {"11:00 AM", "2:00 PM", "5:30 PM"},
                {"1:30 PM", "4:30 PM", "8:00 PM"}
        };
        System.out.println("\n=========== PELICULAS DISPONIBLES ===========");
        mostrarPeliculas(peliculas, 0);
        int pelicula = leerEntero("Seleccione una pelicula (1-3): ") - 1;

        if (pelicula < 0 || pelicula >= peliculas.length) {
            System.out.println(" [!] Pelicula no valida.");
            return;
        }
        System.out.println("\n=============== FUNCIONES ===============");
        for (int i = 0; i < funciones[pelicula].length; i++) {
            System.out.println((i + 1) + ". " + funciones[pelicula][i]);
        }
        int funcion = leerEntero("Seleccione un horario (1-3): ") - 1;
        if (funcion < 0 || funcion >= funciones[pelicula].length) {
            System.out.println(" [!] Horario no valido.");
            return;
        }
        int cantGeneral = 0;
        int cantAdultoMayor = 0;
        int cantNiños = 0;
        int cantConadis = 0;
        int cantPromoSocio = 0;
        int cantSocio = 0;
        int categoria;
        do {
            System.out.println("\n==============================================");
            System.out.println("             TIPO DE ENTRADAS");
            System.out.println("==============================================");
            System.out.println(" [1]. ENTRADAS GENERALES");
            System.out.println(" [2]. ENTRADAS SOCIO");
            System.out.println(" [3]. Finalizar seleccion");
            System.out.println("----------------------------------------------");
            categoria = leerEntero("Seleccione una opcion: ");
            if (categoria == 1) {
                int opcionGeneral;
                do {
                    System.out.println("\n========== ENTRADAS GENERALES ==========");
                    System.out.println(" [1]. General 2D OL                    S/. 14.50");
                    System.out.println(" [2]. Niños menores de 12 años 2D OL   S/. 12.50");
                    System.out.println(" [3]. Mayores de 60 años 2D OL         S/. 12.50");
                    System.out.println(" [4]. Boleto Conadis 2D OL             S/.  7.00");
                    System.out.println(" [5]. Regresar");
                    opcionGeneral = leerEntero("Seleccione una entrada: ");
                    if (opcionGeneral >= 1 && opcionGeneral <= 4) {
                        int cantidad = leerEntero("Ingrese cantidad: ");
                        if (cantidad > 0) {
                            switch (opcionGeneral) {
                                case 1:
                                    cantGeneral += cantidad;
                                    break;
                                case 2:
                                    cantNiños += cantidad;
                                    break;
                                case 3:
                                    cantAdultoMayor += cantidad;
                                    break;
                                case 4:
                                    cantConadis += cantidad;
                                    break;
                            }
                            System.out.println(" [OK] Entrada agregada correctamente.");
                        } else {
                            System.out.println(" [!] Ingrese una cantidad mayor que cero.");
                        }
                    } else if (opcionGeneral != 5) {
                        System.out.println(" [!] Opcion no valida.");
                    }
                } while (opcionGeneral != 5);
            } else if (categoria == 2) {
                int opcionSocio;
                do {
                    System.out.println("\n============= ENTRADAS SOCIO =============");
                    System.out.println(" [1]. Promo Socio: 2 entradas 2D    S/. 20.00");
                    System.out.println(" [2]. Entrada Socio 2D OL           S/.  5.00");
                    System.out.println(" [3]. Regresar");
                    opcionSocio = leerEntero("Seleccione una entrada: ");
                    if (opcionSocio == 1 || opcionSocio == 2) {
                        int cantidad = leerEntero("Ingrese cantidad: ");
                        if (cantidad > 0) {
                            if (opcionSocio == 1) {
                                cantPromoSocio += cantidad;
                            } else {
                                cantSocio += cantidad;
                            }
                            System.out.println(" [OK] Entrada Socio agregada correctamente.");
                        } else {
                            System.out.println(" [!] Ingrese una cantidad mayor que cero.");
                        }
                    } else if (opcionSocio != 3) {
                        System.out.println(" [!] Opcion no valida.");
                    }
                } while (opcionSocio != 3);

            } else if (categoria != 3) {
                System.out.println(" [!] Opcion no valida.");
            }
        } while (categoria != 3);
        int totalAsientos = cantGeneral + cantNiños + cantAdultoMayor
                + cantConadis + (cantPromoSocio * 2) + cantSocio;
        if (totalAsientos == 0) {
            System.out.println(" [!] No selecciono entradas. La compra fue cancelada.");
            return;
        }
        System.out.println("\n=========== RESUMEN DE ENTRADAS ===========");
        System.out.println("General: " + cantGeneral);
        System.out.println("Niños menores de 12 años: " + cantNiños);
        System.out.println("Mayores de 60 años: " + cantAdultoMayor);
        System.out.println("Conadis: " + cantConadis);
        System.out.println("Promociones Socio (paquetes de 2): " + cantPromoSocio);
        System.out.println("Entrada Socio: " + cantSocio);
        System.out.println("Total de asientos que debe elegir: " + totalAsientos);
        // AGREGADO: identificar al cliente (para el bloqueo temporal de butacas)
        clienteActual = pedirCliente();
        int disponibles = 0;
        for (int fila = 0; fila < 12; fila++) {
            for (int columna = 0; columna < 17; columna++) {
                if (!asientos[pelicula][funcion][fila][columna] && !asientoBloqueadoPorOtro(pelicula, funcion, fila, columna)) {
                    disponibles++;
                }
            }
        }
        System.out.println("Asientos disponibles: " + disponibles);
        if (totalAsientos > disponibles) {
            System.out.println(" [!] No hay suficientes asientos disponibles.");
            System.out.println(" [!] La compra no puede continuar.");
            return;
        }
        mostrarAsientos(pelicula, funcion);
        int[] filasElegidas = new int[totalAsientos];
        int[] columnasElegidas = new int[totalAsientos];
        String[] asientosElegidos = new String[totalAsientos];
        int contador = 0;
        while (contador < totalAsientos) {
            System.out.println("\n--- Seleccion de asiento " + (contador + 1)
                    + " de " + totalAsientos + " ---");
            String filaTexto = leerTexto("Ingrese fila (A-L): ").toUpperCase();
            if (filaTexto.length() != 1) {
                System.out.println(" [!] Ingrese solamente una letra.");
                continue;
            }
            char fila = filaTexto.charAt(0);
            int numeroColumna = leerEntero("Ingrese columna (1-17): ");
            int indiceFila = fila - 'A';
            int indiceColumna = numeroColumna - 1;
            if (indiceFila < 0 || indiceFila >= 12) {
                System.out.println(" [!] Asiento fuera de rango.");
            } else if (asientos[pelicula][funcion][indiceFila][indiceColumna]) {
                System.out.println(" [!] Ese asiento ya esta ocupado. Elija otro.");
            } else if (asientoBloqueadoPorOtro(pelicula, funcion, indiceFila, indiceColumna)) {
                System.out.println(" [!] Asiento bloqueado temporalmente por otro cliente (se libera en "
                        + formatoTiempo(bloqueoHasta[pelicula][funcion][indiceFila][indiceColumna]
                        - System.currentTimeMillis()) + "). Elija otro.");
            } else {
                boolean repetido = false;

                for (int i = 0; i < contador; i++) {
                    if (filasElegidas[i] == indiceFila
                            && columnasElegidas[i] == indiceColumna) {
                        repetido = true;
                        break;
                    }
                }
                if (repetido) {
                    System.out.println(" [!] Ya selecciono ese asiento. Elija otro.");
                } else {
                    filasElegidas[contador] = indiceFila;
                    columnasElegidas[contador] = indiceColumna;
                    bloquearAsiento(pelicula, funcion, indiceFila, indiceColumna);
                    asientosElegidos[contador] = fila + "-" + numeroColumna;
                    contador++;
                    System.out.println(" [OK] Asiento seleccionado correctamente.");
                    if (contador == totalAsientos
                            && !validarAsientosHuerfanos(pelicula, funcion,
                            filasElegidas, columnasElegidas, totalAsientos)) {
                        liberarBloqueosCliente();
                        contador = 0;
                        mostrarAsientos(pelicula, funcion);
                        System.out.println("Elija nuevamente sus asientos.");
                    }
                }
            }
        }
        System.out.println("\n===== ASIENTOS BLOQUEADOS TEMPORALMENTE =====");
        System.out.println("Sus asientos quedan reservados por " + (TIEMPO_BLOQUEO / 60000) + " minutos.");
        System.out.println(" [1]. Continuar con la compra y el pago");
        System.out.println(" [2]. Pausar (mantener la reserva y volver al menu)");
        System.out.println(" [3]. Cancelar compra");
        int continuar;
        do {
            continuar = leerEntero("Elija una opcion: ");

            if (continuar < 1 || continuar > 3) {
                System.out.println(" [!] Opcion no valida.");
            }
        } while (continuar < 1 || continuar > 3);
        if (continuar == 2) {
            System.out.println(" [OK] Cliente " + clienteActual
                    + ": su reserva se mantendra activa durante 5 minutos.");
            System.out.println("      Para retomarla ingrese el mismo numero de cliente"
                    + " y elija los mismos asientos.");
            return;

        }
        if (continuar == 3) {
            System.out.println(" [!] Compra cancelada.");
            reiniciarConsumoCompra();
            liberarBloqueosCliente();
            return;
        }
        double subtotalEntradas
                = (cantGeneral * PRECIO_GENERAL)
                + (cantNiños * PRECIO_NIÑOS)
                + (cantAdultoMayor * PRECIO_ADULTO_MAYOR)
                + (cantConadis * PRECIO_CONADIS)
                + (cantPromoSocio * PRECIO_PROMO_SOCIO)
                + (cantSocio * PRECIO_SOCIO);
        double subtotalDulceria = comprarDulceria();
        double totalPagar = subtotalEntradas + subtotalDulceria;
        String textoMetodo = seleccionarMetodoPago();
        if (!verificarBloqueos(pelicula, funcion, filasElegidas, columnasElegidas, totalAsientos)) {
            System.out.println(" [!] Su reserva vencio y otro cliente tomo sus asientos.");
            System.out.println(" [!] No se realizo ningun cobro. Vuelva a elegir sus asientos.");
            reiniciarConsumoCompra();
            liberarBloqueosCliente();
            return;
        }
        for (int i = 0; i < totalAsientos; i++) {
            asientos[pelicula][funcion][filasElegidas[i]][columnasElegidas[i]] = true;
        }
        confirmarConsumoStock();
        liberarBloqueosCliente();
        System.out.println(" [OK] Compra realizada y asientos reservados correctamente.");
        String numeroBoleta = siguienteNumeroBoleta();
        String fechaHora = obtenerFechaHora();
        List<String[]> detalleEntradas = new ArrayList<>();
        String sala = String.valueOf(pelicula + 1);
        List<String> modalidades = new ArrayList<>();
        if (cantGeneral > 0) {
            modalidades.add("GENERAL 2D OL");
        }
        if (cantNiños > 0) {
            modalidades.add("NIÑOS MENORES DE 12 AÑOS 2D OL");
        }
        if (cantAdultoMayor > 0) {
            modalidades.add("MAYORES DE 60 AÑOS 2D OL");
        }
        if (cantConadis > 0) {
            modalidades.add("BOLETO CONADIS 2D OL");
        }
        if (cantPromoSocio > 0) {
            modalidades.add("PROMO SOCIO 2D");
        }
        if (cantSocio > 0) {
            modalidades.add("ENTRADA SOCIO 2D OL");
        }
        String modalidad = String.join(", ", modalidades);
        System.out.println("\n=============== COMPROBANTE ===============");
        System.out.println("       CINEPLANET REAL PLAZA JULIACA");
        System.out.println("-------------------------------------------");
        System.out.println("Boleta N°: " + numeroBoleta);
        System.out.println("Fecha: " + fechaHora);
        System.out.println("Pelicula: " + peliculas[pelicula]);
        System.out.println("Sala: " + sala);
        System.out.println("Funcion: " + funciones[pelicula][funcion]);
        System.out.println("Modalidad: " + modalidad);
        System.out.print("Butacas: ");
        for (int i = 0; i < asientosElegidos.length; i++) {
            System.out.print(asientosElegidos[i]);
            if (i < asientosElegidos.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("\n-------------------------------------------");
        System.out.println("DETALLE DE ENTRADAS:");
        if (cantGeneral > 0) {
            System.out.println("General: " + cantGeneral + " x S/. "
                    + dinero(PRECIO_GENERAL) + " = S/. "
                    + dinero(cantGeneral * PRECIO_GENERAL));
            detalleEntradas.add(new String[]{
                    "General " + cantGeneral + " x S/. " + dinero(PRECIO_GENERAL),
                    dinero(cantGeneral * PRECIO_GENERAL)});
        }
        if (cantNiños > 0) {
            System.out.println("Niños menores de 12 años: " + cantNiños + " x S/. "
                    + dinero(PRECIO_NIÑOS) + " = S/. "
                    + dinero(cantNiños * PRECIO_NIÑOS));
            detalleEntradas.add(new String[]{
                    "Niños <12 " + cantNiños + " x S/. " + dinero(PRECIO_NIÑOS),
                    dinero(cantNiños * PRECIO_NIÑOS)});
        }
        if (cantAdultoMayor > 0) {
            System.out.println("Adulto mayor: " + cantAdultoMayor + " x S/. "
                    + dinero(PRECIO_ADULTO_MAYOR) + " = S/. "
                    + dinero(cantAdultoMayor * PRECIO_ADULTO_MAYOR));
            detalleEntradas.add(new String[]{
                    "Adulto mayor " + cantAdultoMayor + " x S/. " + dinero(PRECIO_ADULTO_MAYOR),
                    dinero(cantAdultoMayor * PRECIO_ADULTO_MAYOR)});
        }
        if (cantConadis > 0) {
            System.out.println("Conadis: " + cantConadis + " x S/. "
                    + dinero(PRECIO_CONADIS) + " = S/. "
                    + dinero(cantConadis * PRECIO_CONADIS));
            detalleEntradas.add(new String[]{
                    "Conadis " + cantConadis + " x S/. " + dinero(PRECIO_CONADIS),
                    dinero(cantConadis * PRECIO_CONADIS)});
        }
        if (cantPromoSocio > 0) {
            System.out.println("Promo Socio (2 entradas): " + cantPromoSocio + " x S/. " + dinero(PRECIO_PROMO_SOCIO) + " = S/. " + dinero(cantPromoSocio * PRECIO_PROMO_SOCIO));
            detalleEntradas.add(new String[]{
                    "Promo Socio (2 ent.) " + cantPromoSocio + " x S/. " + dinero(PRECIO_PROMO_SOCIO),
                    dinero(cantPromoSocio * PRECIO_PROMO_SOCIO)});
        }
        if (cantSocio > 0) {
            System.out.println("Entrada Socio: " + cantSocio + " x S/. " + dinero(PRECIO_SOCIO) + " = S/. " + dinero(cantSocio * PRECIO_SOCIO));
            detalleEntradas.add(new String[]{
                    "Entrada Socio " + cantSocio + " x S/. " + dinero(PRECIO_SOCIO),
                    dinero(cantSocio * PRECIO_SOCIO)});
        }
        System.out.println("-------------------------------------------");
        System.out.println("DETALLE DE DULCERIA:");
        if (detalleDulceria.isEmpty()) {
            System.out.println("No se compraron productos de dulceria.");
        } else {
            System.out.println(detalleDulceria);
        }
        System.out.println("Subtotal entradas: S/. " + dinero(subtotalEntradas));
        System.out.println("Subtotal dulceria: S/. " + dinero(subtotalDulceria));
        System.out.println("-------------------------------------------");
        System.out.println("Op. gravada: S/. " + dinero(calcularOpGravada(totalPagar)));
        System.out.println("IGV (" + Math.round(TASA_IGV * 100) + "%): S/. " + dinero(calcularIgv(totalPagar)));
        System.out.println("IMPORTE TOTAL: S/. " + dinero(totalPagar));
        System.out.println("Metodo de pago: " + textoMetodo);
        System.out.println("===========================================\n");
        List<String> cabecera = new ArrayList<>();
        cabecera.add("Pelicula: " + peliculas[pelicula]);
        cabecera.add("Sala: " + sala);
        cabecera.add("Funcion: " + funciones[pelicula][funcion]);
        cabecera.add("Modalidad: " + modalidad);
        cabecera.add("Butacas: " + String.join(", ", asientosElegidos));
        generarBoletaPDF(numeroBoleta, fechaHora, cabecera, detalleEntradas,
                detalleDulceria, subtotalEntradas, subtotalDulceria,
                totalPagar, textoMetodo);
    }
    public static void mostrarPeliculas(String[] peliculas, int i) {
        if (i == peliculas.length) {
            return;
        }
        System.out.println((i + 1) + ". " + peliculas[i]);
        mostrarPeliculas(peliculas, i + 1);
    }
    public static void mostrarAsientos(int pelicula, int funcion) {
        System.out.println("\n================= P A N T A L L A =================");
        System.out.print("     ");
        for (int columna = 1; columna <= 17; columna++) {
            System.out.print("[" + columna + "] ");
        }
        System.out.println("\n     ------------------------------------------------------------");
        for (int fila = 0; fila < 12; fila++) {
            char letra = (char) ('A' + fila);
            System.out.print(" " + letra + " | ");
            for (int columna = 0; columna < 17; columna++) {
                char estado;
                if (asientos[pelicula][funcion][fila][columna]) {
                    estado = 'X'; // ocupado
                } else if (bloqueoHasta[pelicula][funcion][fila][columna]
                        > System.currentTimeMillis()) {
                    if (bloqueoCliente[pelicula][funcion][fila][columna] == clienteActual) {
                        estado = 'M'; // bloqueado por mi
                    } else {
                        estado = 'B'; // bloqueado por otro cliente
                    }
                } else {
                    estado = 'O'; // libre
                }
                System.out.print("[" + estado + "] ");
            }
            System.out.println();
        }
        System.out.println("O = Libre     X = Ocupado");
        System.out.println("B = Bloqueado por otro cliente     M = Bloqueado por mi");
    }
    public static String elegirSaborPopcorn() {
        int sabor;
        do {
            System.out.println("\n======= SABOR DE POPCORN GIGANTE =======");
            System.out.println(" [1]. Salado" + etiquetaStock(0));
            System.out.println(" [2]. Dulce" + etiquetaStock(1));
            System.out.println(" [3]. Mixto" + etiquetaStock(2));
            sabor = leerEntero("Seleccione el sabor: ");
            if (sabor < 1 || sabor > 3) {
                System.out.println(" [!] Sabor no valido.");
            } else if (disponible(sabor - 1) <= 0) {
                System.out.println(" [!] Ese sabor esta agotado. Elija otro.");
                sabor = 0;
            }
        } while (sabor < 1 || sabor > 3);
        consumoCompra[sabor - 1]++;
        switch (sabor) {
            case 1:
                return "Salado";
            case 2:
                return "Dulce";
            default:
                return "Mixto";
        }
    }

    public static double comprarDulceria() {
        String[] productos = {
                "Combo 1 (Popcorn gigante + gaseosa 32 oz)",
                "Combo Duo (Popcorn gigante + 2 gaseosas + Hot Dog)",
                "Combo Familiar (2 popcorn + 4 gaseosas)",
                "Popcorn gigante",
                "Gaseosa 32 oz"
        };
        double[] precios = {32.00, 48.50, 65.00, 18.00, 11.00};
        double subtotal = 0;
        StringBuilder detalle = new StringBuilder();
        reiniciarConsumoCompra();
        detalleDulceria = "";
        System.out.println("\n=============== DULCERIA ===============");
        System.out.println(" [1]. Si, deseo comprar");
        System.out.println(" [2]. No, continuar sin dulceria");
        int opcion = leerEntero("Seleccione: ");
        if (opcion == 1) {
            int elegir;
            do {
                System.out.println("\n--------------- MENU DULCERIA ---------------");
                for (int i = 0; i < productos.length; i++) {
                    System.out.println((i + 1) + ". " + productos[i] + " - S/. " + dinero(precios[i]) + estadoProducto(i));
                }
                System.out.println(" [6]. Finalizar dulceria\n");
                elegir = leerEntero("Seleccione un producto:\n ");
                if (elegir >= 1 && elegir <= 5) {
                    int cantidad = leerEntero("Cantidad: \n");
                    if (cantidad > 0 && !verificarYReservarProducto(elegir - 1, cantidad)) {
                        continue;
                    }
                    if (cantidad > 0) {
                        detalle.append(productos[elegir - 1])
                                .append(" x ")
                                .append(cantidad)
                                .append("\n");
                        if (elegir == 1) {
                            for (int i = 1; i <= cantidad; i++) {
                                System.out.println("\nCombo 1 - Unidad " + i + " de " + cantidad);
                                String sabor = elegirSaborPopcorn();
                                detalle.append("   Popcorn gigante: ")
                                        .append(sabor).append("\n");
                                System.out.println(" [OK] Popcorn gigante " + sabor + " agregado al Combo 1.");
                            }
                        } else if (elegir == 2) {
                            for (int i = 1; i <= cantidad; i++) {
                                System.out.println("\nCombo Duo - Unidad " + i
                                        + " de " + cantidad);
                                String sabor = elegirSaborPopcorn();
                                detalle.append("   Popcorn gigante: ")
                                        .append(sabor).append("\n");
                                System.out.println(" [OK] Popcorn gigante " + sabor + " agregado al Combo Duo.");
                            }
                        } else if (elegir == 3) {
                            for (int i = 1; i <= cantidad; i++) {
                                System.out.println("\nCombo Familiar " + i
                                        + " de " + cantidad);
                                for (int j = 1; j <= 2; j++) {
                                    System.out.println("\nPopcorn " + j + " de 2:");
                                    String sabor = elegirSaborPopcorn();
                                    detalle.append("   Popcorn ")
                                            .append(j).append(": ")
                                            .append(sabor).append("\n");
                                    System.out.println(" [OK] Popcorn " + sabor + " agregado al Combo Familiar.");
                                }
                            }
                        } else if (elegir == 4) {
                            for (int i = 1; i <= cantidad; i++) {
                                System.out.println("\nPopcorn gigante " + i
                                        + " de " + cantidad);
                                String sabor = elegirSaborPopcorn();
                                detalle.append("   Unidad ").append(i)
                                        .append(": ").append(sabor).append("\n");
                                System.out.println(" [OK] Popcorn gigante "
                                        + sabor + " agregado correctamente.");
                            }
                        } else if (elegir == 5) {
                            System.out.println(" [OK] Gaseosa agregada correctamente.");
                        }
                        subtotal = subtotal + (precios[elegir - 1] * cantidad);
                        detalle.append("   Importe: S/. ")
                                .append(dinero(precios[elegir - 1] * cantidad))
                                .append("\n\n");
                    } else {
                        System.out.println(" [!] Cantidad no valida.");
                    }
                } else if (elegir != 6) {
                    System.out.println(" [!] Opcion no valida.");
                }
            } while (elegir != 6);
        } else if (opcion != 2) {
            System.out.println(" [!] Opcion no valida. Se continuara sin dulceria.");
        }
        mostrarAlertasStock();
        detalleDulceria = detalle.toString();
        System.out.println("Subtotal dulceria: S/. " + dinero(subtotal));
        return subtotal;
    }
    public static void menuDulceria() {
        System.out.println("\n=========== CINEPLANET - DULCERIA ===========\n");
        System.out.println(" [1]. Combos para compartir\n");
        System.out.println(" [2]. Combo personal\n");
        System.out.println(" [3]. Promos de peliculas\n");
        System.out.println(" [4]. Combos Socio\n");
        System.out.println(" [5]. Canchitas\n");
        System.out.println(" [6]. Complementarios y bebidas\n");
        System.out.println(" [7]. Realizar compra\n");
        System.out.println(" [8]. Regresar al menu principal\n");
        System.out.println(" [9]. Ver inventario y alertas de stock\n");
        int opcion = leerEntero("Elija una opcion: ");
        switch (opcion) {
            case 1:
                System.out.println("Combos Duo y Familiar para compartir.");
                break;
            case 2:
                System.out.println("Combo personal: canchita y bebida.");
                break;
            case 3:
                System.out.println("Combos especiales de peliculas, sujetos a stock.");
                break;
            case 4:
                System.out.println("Combos y beneficios exclusivos para Socios.");
                break;
            case 5:
                System.out.println("Canchitas Junior, Regular, Grande y Gigante.");
                break;
            case 6:
                System.out.println("Hot Dog, nachos, gaseosas, agua y golosinas.");
                break;
            case 7:
                double subtotal = comprarDulceria();
                if (subtotal > 0) {
                    String metodo = seleccionarMetodoPago();
                    confirmarConsumoStock();
                    String numeroBoleta = siguienteNumeroBoleta();
                    String fechaHora = obtenerFechaHora();
                    System.out.println("\n=========== COMPROBANTE DULCERIA ===========");
                    System.out.println("Boleta N°: " + numeroBoleta);
                    System.out.println("Fecha: " + fechaHora);
                    System.out.println("DETALLE DE PRODUCTOS:");
                    if (detalleDulceria.isEmpty()) {
                        System.out.println("No se compraron productos.");
                    } else {
                        System.out.println(detalleDulceria);
                    }
                    System.out.println("Subtotal dulceria: S/. " + dinero(subtotal));
                    System.out.println("Op. gravada: S/. " + dinero(calcularOpGravada(subtotal)));
                    System.out.println("IGV (" + Math.round(TASA_IGV * 100) + "%): S/. " + dinero(calcularIgv(subtotal)));
                    System.out.println("IMPORTE TOTAL: S/. " + dinero(subtotal));
                    System.out.println("Metodo de pago: " + metodo);
                    System.out.println("============================================");
                    List<String> cabecera = new ArrayList<>();
                    cabecera.add("Venta de dulceria");
                    generarBoletaPDF(numeroBoleta, fechaHora, cabecera,
                            new ArrayList<String[]>(), detalleDulceria, 0, subtotal, subtotal, metodo);
                }
                break;
            case 8:
                System.out.println("Regresando al menu principal.");
                break;
            case 9:
                mostrarInventario();
                break;
            default:
                System.out.println(" [!] Opcion no valida.");
        }
    }
    public static void menuSocio() {
        int opcion;
        do {
            System.out.println("\n=========== SOCIO CINEPLANET ===========");
            System.out.println(" [1]. Beneficios generales");
            System.out.println(" [2]. Niveles de Socio");
            System.out.println(" [3]. Como hacerte Socio");
            System.out.println(" [4]. Regresar al menu principal");
            opcion = leerEntero("Elija una opcion: ");
            switch (opcion) {
                case 1:
                    System.out.println("Acumula puntos y accede a descuentos y preventas.");
                    break;
                case 2:
                    System.out.println("Niveles informativos: Clasico, Plata, Oro y Black.");
                    break;
                case 3:
                    System.out.println("Puedes registrarte desde los canales de Cineplanet.");
                    break;
                case 4:
                    System.out.println("Regresando al menu principal.");
                    break;
                default:
                    System.out.println(" [!] Opcion no valida.");
            }
        } while (opcion != 4);
    }
    public static String seleccionarMetodoPago() {
        int metodoPago;
        do {
            System.out.println("\n=============== METODO DE PAGO ===============");
            System.out.println(" [1]. Tarjeta de credito/debito");
            System.out.println(" [2]. Yape / Plin");
            System.out.println(" [3]. Efectivo en caja");
            metodoPago = leerEntero("Elija metodo de pago: ");
            if (metodoPago < 1 || metodoPago > 3) {
                System.out.println(" [!] Metodo no valido. Seleccione nuevamente.");
            }
        } while (metodoPago < 1 || metodoPago > 3);
        switch (metodoPago) {
            case 1:
                return "Tarjeta";
            case 2:
                return "Yape / Plin";
            default:
                return "Efectivo";
        }
    }
    public static int leerEntero(String mensaje) {
        int numero = 0;
        boolean correcto = false;
        while (!correcto) {
            System.out.print(mensaje);
            try {
                numero = teclado.nextInt();
                correcto = true;
            } catch (InputMismatchException e) {
                System.out.println(" [!] Debe ingresar un numero.");
                teclado.next();
            }
        }
        teclado.nextLine();
        return numero;
    }
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }
    public static int pedirCliente() {
        int cliente;
        do {
            cliente = leerEntero("Ingrese su numero de cliente (1-99): ");

            if (cliente < 1 || cliente > 99) {
                System.out.println(" [!] Numero de cliente no valido.");
            }
        } while (cliente < 1 || cliente > 99);

        return cliente;
    }
    public static boolean asientoBloqueadoPorOtro(int pelicula, int funcion,
                                                  int fila, int columna) {
        return bloqueoHasta[pelicula][funcion][fila][columna] > System.currentTimeMillis()
                && bloqueoCliente[pelicula][funcion][fila][columna] != clienteActual;
    }
    public static void bloquearAsiento(int pelicula, int funcion, int fila, int columna) {
        bloqueoHasta[pelicula][funcion][fila][columna]
                = System.currentTimeMillis() + TIEMPO_BLOQUEO;
        bloqueoCliente[pelicula][funcion][fila][columna] = clienteActual;
    }
    public static void liberarBloqueosCliente() {
        for (int p = 0; p < 3; p++) {
            for (int f = 0; f < 3; f++) {
                for (int fila = 0; fila < 12; fila++) {
                    for (int columna = 0; columna < 17; columna++) {
                        if (bloqueoCliente[p][f][fila][columna] == clienteActual) {
                            bloqueoHasta[p][f][fila][columna] = 0;
                            bloqueoCliente[p][f][fila][columna] = 0;
                        }
                    }
                }
            }
        }
    }
    public static boolean verificarBloqueos(int pelicula, int funcion, int[] filas, int[] columnas, int cantidad) {
        long ahora = System.currentTimeMillis();
        for (int i = 0; i < cantidad; i++) {
            if (asientos[pelicula][funcion][filas[i]][columnas[i]]
                    || asientoBloqueadoPorOtro(pelicula, funcion, filas[i], columnas[i])) {
                return false;
            }
        }
        long menorRestante = Long.MAX_VALUE;
        boolean renovado = false;
        for (int i = 0; i < cantidad; i++) {
            long hasta = bloqueoHasta[pelicula][funcion][filas[i]][columnas[i]];
            if (hasta > ahora
                    && bloqueoCliente[pelicula][funcion][filas[i]][columnas[i]] == clienteActual) {
                menorRestante = Math.min(menorRestante, hasta - ahora);
            } else {
                bloquearAsiento(pelicula, funcion, filas[i], columnas[i]);
                renovado = true;
            }
        }
        if (renovado) {
            System.out.println(" [i] Su tiempo de reserva vencio, pero los asientos seguian"
                    + " libres. Se renovo el bloqueo.");
        } else {
            System.out.println(" [i] Tiempo restante de su reserva: "
                    + formatoTiempo(menorRestante));
        }
        return true;
    }
    public static String formatoTiempo(long milisegundos) {
        long segundos = (milisegundos + 999) / 1000;

        if (segundos < 0) {
            segundos = 0;
        }

        return String.format( "xd ", segundos / 60, segundos % 60);
    }
    public static boolean validarAsientosHuerfanos(int pelicula, int funcion, int[] filas, int[] columnas, int cantidad) {
        List<String> huerfanos = new ArrayList<>();
        for (int fila = 0; fila < 12; fila++) {
            boolean[] elegido = new boolean[17];
            boolean[] ocupado = new boolean[17];
            for (int i = 0; i < cantidad; i++) {
                if (filas[i] == fila) {
                    elegido[columnas[i]] = true;
                }
            }
            for (int columna = 0; columna < 17; columna++) {
                ocupado[columna] = asientos[pelicula][funcion][fila][columna]
                        || asientoBloqueadoPorOtro(pelicula, funcion, fila, columna)
                        || elegido[columna];
            }
            for (int columna = 0; columna < 17; columna++) {
                if (!ocupado[columna]) {
                    boolean izquierdaCerrada = (columna == 0) || ocupado[columna - 1];
                    boolean derechaCerrada = (columna == 16) || ocupado[columna + 1];
                    boolean izquierdaElegida = (columna > 0) && elegido[columna - 1];
                    boolean derechaElegida = (columna < 16) && elegido[columna + 1];
                    if (izquierdaCerrada && derechaCerrada
                            && (izquierdaElegida || derechaElegida)) {
                        huerfanos.add("Fila " + (char) ('A' + fila)
                                + ", asiento " + (columna + 1));
                    }
                }
            }
        }
        if (huerfanos.isEmpty()) {
            return true;
        }
        System.out.println("\n========== ASIENTOS AISLADOS DETECTADOS ==========");
        System.out.println("Su seleccion dejaria asientos sueltos que casi nunca se venden:");
        for (String huerfano : huerfanos) {
            System.out.println("   - " + huerfano);
        }
        System.out.println(" [1]. Cambiar mi seleccion (recomendado)");
        System.out.println(" [2]. Continuar de todos modos");
        int opcion;
        do {
            opcion = leerEntero("Elija una opcion: ");
            if (opcion < 1 || opcion > 2) {
                System.out.println(" [!] Opcion no valida.");
            }
        } while (opcion < 1 || opcion > 2);
        return opcion == 2;
    }
    public static int disponible(int insumo) {
        return stockInsumos[insumo] - consumoCompra[insumo];
    }
    public static int popcornDisponible() {
        return disponible(0) + disponible(1) + disponible(2);
    }
    public static int factorDemanda() {
        LocalDateTime ahora = LocalDateTime.now();
        int factor = 1;
        if (ahora.getHour() >= 18) {
            factor++;
        }
        if (ahora.getDayOfWeek().getValue() >= 6) {
            factor++;
        }
        return factor;
    }
    public static String descripcionDemanda() {
        LocalDateTime ahora = LocalDateTime.now();
        String texto = "";
        if (ahora.getHour() >= 18) {
            texto = texto + "funcion nocturna ";
        }
        if (ahora.getDayOfWeek().getValue() >= 6) {
            texto = texto + "fin de semana ";
        }
        if (texto.isEmpty()) {
            texto = "horario normal";
        }
        return texto.trim();
    }
    public static int umbralAlerta(int insumo) {
        return stockMinimo[insumo] * factorDemanda();
    }
    public static String etiquetaStock(int insumo) {
        int restante = disponible(insumo);
        if (restante <= 0) {
            return "  [AGOTADO]";
        }
        if (restante <= umbralAlerta(insumo)) {
            return "  [Quedan " + restante + "]";
        }
        return "";
    }
    // Cuanto necesita una unidad de cada producto: {popcorn, gaseosa, hot dog}
    public static int[] recetaProducto(int producto) {
        switch (producto) {
            case 0:
                return new int[]{1, 1, 0}; // Combo 1
            case 1:
                return new int[]{1, 2, 1}; // Combo Duo
            case 2:
                return new int[]{2, 4, 0}; // Combo Familiar
            case 3:
                return new int[]{1, 0, 0}; // Popcorn gigante
            default:
                return new int[]{0, 1, 0}; // Gaseosa 32 oz
        }
    }
    public static String estadoProducto(int producto) {
        int[] receta = recetaProducto(producto);
        int unidades = Integer.MAX_VALUE;
        if (receta[0] > 0) {
            unidades = Math.min(unidades, popcornDisponible() / receta[0]);
        }
        if (receta[1] > 0) {
            unidades = Math.min(unidades, disponible(3) / receta[1]);
        }
        if (receta[2] > 0) {
            unidades = Math.min(unidades, disponible(4) / receta[2]);
        }
        if (unidades <= 0) {
            return "  [AGOTADO]";
        }
        if (unidades <= 5 * factorDemanda()) {
            return "  [Quedan " + unidades + "]";
        }
        return "";
    }
    public static boolean verificarYReservarProducto(int producto, int cantidad) {
        int[] receta = recetaProducto(producto);
        int popcornNecesario = receta[0] * cantidad;
        int gaseosaNecesaria = receta[1] * cantidad;
        int hotdogNecesario = receta[2] * cantidad;
        boolean alcanza = true;
        if (popcornNecesario > popcornDisponible()) {
            System.out.println(" [!] Stock insuficiente de popcorn gigante: quedan "
                    + popcornDisponible() + ".");
            alcanza = false;
        }
        if (gaseosaNecesaria > disponible(3)) {
            System.out.println(" [!] Stock insuficiente de gaseosas 32 oz: quedan "
                    + disponible(3) + ".");
            alcanza = false;
        }
        if (hotdogNecesario > disponible(4)) {
            System.out.println(" [!] Stock insuficiente de Hot Dog: quedan "
                    + disponible(4) + ".");
            alcanza = false;
        }
        if (!alcanza) {
            System.out.println(" [!] No se agrego el producto. Elija una cantidad menor u otro producto.");
            return false;
        }
        consumoCompra[3] = consumoCompra[3] + gaseosaNecesaria;
        consumoCompra[4] = consumoCompra[4] + hotdogNecesario;
        return true;
    }
    public static void mostrarAlertasStock() {
        boolean hayAlertas = false;
        for (int i = 0; i < insumos.length; i++) {
            int restante = disponible(i);
            if (restante <= umbralAlerta(i)) {
                if (!hayAlertas) {
                    System.out.println("\n---------- ALERTAS DE STOCK ----------");
                    hayAlertas = true;
                }
                if (restante <= 0) {
                    System.out.println(" [AGOTADO] " + insumos[i]);
                } else {
                    System.out.println(" [ALERTA] Quedan " + restante + " de " + insumos[i]
                            + " (demanda esperada: " + descripcionDemanda() + ").");
                }
            }
        }
    }
    public static void confirmarConsumoStock() {
        for (int i = 0; i < insumos.length; i++) {
            stockInsumos[i] = stockInsumos[i] - consumoCompra[i];
            vendidosInsumos[i] = vendidosInsumos[i] + consumoCompra[i];
            consumoCompra[i] = 0;
        }
    }  // Se llama cuando la compra no se concreta: se devuelve lo reservado.
    public static void reiniciarConsumoCompra() {
        for (int i = 0; i < insumos.length; i++) {
            consumoCompra[i] = 0;
        }
    }
    public static void mostrarInventario() {
        System.out.println("\n============ INVENTARIO DE DULCERIA ============");
        System.out.println("Demanda esperada ahora: x" + factorDemanda()
                + " (" + descripcionDemanda() + ")");
        System.out.println(String.format("%-3s %-24s %6s %9s %9s  %s", "N°", "Producto", "Stock", "Alerta<=", "Vendidos", "Estado"));
        for (int i = 0; i < insumos.length; i++) {
            String estado = "OK";
            if (stockInsumos[i] <= 0) {
                estado = "AGOTADO";
            } else if (stockInsumos[i] <= umbralAlerta(i)) {
                estado = "STOCK BAJO";
            }
            System.out.println(String.format("%-3d %-24s %6d %9d %9d  %s",
                    i + 1, insumos[i], stockInsumos[i], umbralAlerta(i),
                    vendidosInsumos[i], estado));
        }

        int opcion = leerEntero("\nDesea reponer stock? [1] Si  [2] No: ");
        if (opcion == 1) {
            int numero = leerEntero("Numero de producto (1-" + insumos.length + "): ");
            if (numero < 1 || numero > insumos.length) {
                System.out.println(" [!] Producto no valido.");
            } else {
                int cantidad = leerEntero("Cantidad a reponer: ");
                if (cantidad > 0) {
                    stockInsumos[numero - 1] = stockInsumos[numero - 1] + cantidad;
                    System.out.println(" [OK] Stock actualizado: " + insumos[numero - 1]
                            + " = " + stockInsumos[numero - 1]);
                } else {
                    System.out.println(" [!] Cantidad no valida.");
                }
            }
        }
    }
    public static String dinero(double monto) {
        return String.format(Locale.US, "%.2f", monto);
    }
    public static double calcularOpGravada(double total) {
        return Math.round((total / (1 + TASA_IGV)) * 100.0) / 100.0;
    }
    public static double calcularIgv(double total) {
        return Math.round((total - calcularOpGravada(total)) * 100.0) / 100.0;
    }
    public static String siguienteNumeroBoleta() {
        contadorBoleta++;
        return String.format("B001-%06d", contadorBoleta);
    }
    public static String obtenerFechaHora() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    static final int ANCHO_TICKET = 40; // caracteres por linea
    static final String SEPARADOR = "----------------------------------------";
    public static void generarBoletaPDF(String numero, String fechaHora,
                                        List<String> cabecera, List<String[]> detalle, String dulceria,
                                        double subtotalEntradas, double subtotalDulceria,
                                        double total, String metodo) {
        List<String> lineas = new ArrayList<>();
        List<Boolean> negritas = new ArrayList<>();
        // Encabezado
        agregarLinea(lineas, negritas, centrar("CINEPLANET"), true);
        agregarLinea(lineas, negritas, centrar("REAL PLAZA JULIACA"), true);
        agregarLinea(lineas, negritas, SEPARADOR, false);
        agregarTexto(lineas, negritas, "BOLETA N°: " + numero, false);
        agregarTexto(lineas, negritas, "FECHA: " + fechaHora, false);
        agregarLinea(lineas, negritas, SEPARADOR, false);
        for (String linea : cabecera) {
            agregarTexto(lineas, negritas, linea, false);
        }
        if (!detalle.isEmpty()) {//si huo una entra solo una
            agregarLinea(lineas, negritas, SEPARADOR, false);
            agregarLinea(lineas, negritas, "ENTRADAS", true);
            for (String[] item : detalle) {
                agregarFila(lineas, negritas, item[0], "S/. " + item[1], false);
            }
        }
        if (!dulceria.trim().isEmpty()) {// Detalle de dulceria (solo si hubo productos)
            agregarLinea(lineas, negritas, SEPARADOR, false);
            agregarLinea(lineas, negritas, "DULCERIA", true);
            for (String linea : dulceria.split("\n")) {
                if (!linea.trim().isEmpty()) {
                    agregarTexto(lineas, negritas, linea, false);
                }
            }
        }
        agregarLinea(lineas, negritas, SEPARADOR, false);//totales
        if (!detalle.isEmpty()) {
            agregarFila(lineas, negritas, "Subtotal entradas",
                    "S/. " + dinero(subtotalEntradas), false);
        }
        agregarFila(lineas, negritas, "Subtotal dulceria",
                "S/. " + dinero(subtotalDulceria), false);
        agregarLinea(lineas, negritas, SEPARADOR, false);// IGV: los precios ya lo incluyen, aqui se muestra el desglose.
        agregarFila(lineas, negritas, "Op. gravada",
                "S/. " + dinero(calcularOpGravada(total)), false);
        agregarFila(lineas, negritas, "IGV (" + Math.round(TASA_IGV * 100) + "%)",
                "S/. " + dinero(calcularIgv(total)), false);
        agregarFila(lineas, negritas, "IMPORTE TOTAL",
                "S/. " + dinero(total), true);
        agregarFila(lineas, negritas, "Metodo de pago", metodo, false);
        agregarLinea(lineas, negritas, SEPARADOR, false);
        agregarLinea(lineas, negritas, centrar("Gracias por su compra"), true);
        try {
            File carpeta = new File("boletas");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            File archivo = new File(carpeta, "Boleta_" + numero + ".pdf");
            escribirPdf(archivo, lineas, negritas);
            System.out.println(" [OK] Boleta PDF guardada en: "
                    + archivo.getAbsolutePath());// Abrir el PDF con el programa predeterminado del sistema.
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivo);
                }
            } catch (Exception e) {
                System.out.println(" [!] No se pudo abrir el PDF automaticamente.");
            }
        } catch (IOException e) {
            System.out.println(" [!] No se pudo generar la boleta PDF: " + e.getMessage());
        }
    }
    public static void escribirPdf(File archivo, List<String> lineas, List<Boolean> negritas) throws IOException {
        float ancho = 227f;
        float margenX = 17f;
        float margenY = 10f;
        float interlineado = 11f;
        float alto = (margenY * 2f) + (lineas.size() * interlineado);
        PDType1Font fuenteNormal = new PDType1Font(Standard14Fonts.FontName.COURIER);
        PDType1Font fuenteNegrita = new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD);
        try (PDDocument doc = new PDDocument()) {
            PDPage pagina = new PDPage(new PDRectangle(ancho, alto));
            doc.addPage(pagina);
            try (PDPageContentStream cs = new PDPageContentStream(doc, pagina)) {
                cs.setFont(fuenteNormal, 8);
                cs.beginText();
                cs.newLineAtOffset(margenX, alto - margenY - 8f);

                for (int i = 0; i < lineas.size(); i++) {
                    boolean esNegrita = i < negritas.size() && negritas.get(i);
                    cs.setFont(esNegrita ? fuenteNegrita : fuenteNormal, 8);
                    cs.showText(lineas.get(i));
                    cs.newLineAtOffset(0, -interlineado);
                }
                cs.endText();
            }
            doc.save(archivo);
        }
    }
    public static String escaparPdf(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '\\' || c == '(' || c == ')') {
                resultado.append('\\').append(c);
            } else if (c > 255) {
                resultado.append('?');
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
    public static void agregarLinea(List<String> lineas, List<Boolean> negritas, String texto, boolean negrita) {
        lineas.add(texto);
        negritas.add(negrita);
    }
    public static void agregarTexto(List<String> lineas, List<Boolean> negritas,String texto, boolean negrita) {
        for (String parte : partir(texto, ANCHO_TICKET)) {
            agregarLinea(lineas, negritas, parte, negrita);
        }
    }
    public static void agregarFila(List<String> lineas, List<Boolean> negritas,
                                   String izquierda, String derecha, boolean negrita) {
        int disponible = ANCHO_TICKET - derecha.length() - 1;
        List<String> partes = partir(izquierda, disponible);
        for (int i = 0; i < partes.size(); i++) {
            String parte = partes.get(i);
            if (i == partes.size() - 1) {
                parte = parte
                        + espacios(ANCHO_TICKET - parte.length() - derecha.length())
                        + derecha;
            }
            agregarLinea(lineas, negritas, parte, negrita);
        }
    }
    public static List<String> partir(String texto, int ancho) {
        List<String> partes = new ArrayList<>();
        int sangria = 0;
        while (sangria < texto.length() && texto.charAt(sangria) == ' ') {
            sangria++;
        }
        String relleno = texto.substring(0, sangria);
        String actual = relleno;
        for (String palabra : texto.trim().split(" ")) {
            if (palabra.isEmpty()) continue;
            if (actual.trim().isEmpty()) {
                actual = relleno + palabra;
            } else if (actual.length() + 1 + palabra.length() <= ancho) {
                actual = actual + " " + palabra;
            } else {
                partes.add(actual);
                actual = relleno + palabra;
            }
        }
        partes.add(actual);
        return partes;
    }
    public static String centrar(String texto) {
        return espacios((ANCHO_TICKET - texto.length()) / 2) + texto;
    }
    public static String espacios(int cantidad) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < cantidad; i++) {
            resultado.append(' ');
        }
        return resultado.toString();
    }
}
