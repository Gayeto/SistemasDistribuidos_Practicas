import socket
import random

reloj_lamport = 0
servidores = [("servidor1", 6001), ("servidor2", 6002)]

def incrementar_reloj():
    global reloj_lamport
    reloj_lamport += 1
    return reloj_lamport

def cliente():
    global reloj_lamport
    while True:
        entrada = input("Escribe 'RESERVAR' para una cita o 'SALIR' para salir: ").upper()

        if entrada == "SALIR":
            print("Saliendo del sistema...")
            break

        if entrada == "RESERVAR":
            servidor_host, puerto_servidor = random.choice(servidores)

            try:
                cliente_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                cliente_socket.settimeout(3)  # Timeout de 3 segundos
                cliente_socket.connect((servidor_host, puerto_servidor))
                print(f"Conectado a {servidor_host}:{puerto_servidor}")

                reloj_actual = incrementar_reloj()
                mensaje = f"RESERVA:{reloj_actual}"
                cliente_socket.sendall(mensaje.encode())

                try:
                    respuesta = cliente_socket.recv(1024).decode().strip()
                    if respuesta:
                        print(f"Respuesta del servidor: {respuesta}")
                    else:
                        print("⚠️ Servidor no respondió correctamente.")
                except socket.timeout:
                    print("⚠️ Tiempo de espera agotado al recibir respuesta del servidor.")

                cliente_socket.close()

            except Exception as e:
                print(f"⚠️ Error al conectar con {servidor_host}:{puerto_servidor} - {e}")

if __name__ == "__main__":
    cliente()
