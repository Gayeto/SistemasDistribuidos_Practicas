import socket
import threading
import queue
import sys

servidores = [("servidor1", 6001), ("servidor2", 6002)]
reloj_lamport = 0
cola_solicitudes = queue.PriorityQueue()

def incrementar_reloj():
    global reloj_lamport
    reloj_lamport += 1
    return reloj_lamport

def sincronizar_reloj(reloj_recibido):
    global reloj_lamport
    reloj_lamport = max(reloj_lamport, reloj_recibido) + 1

def notificar_servidores(mensaje, mi_puerto):
    for host, puerto in servidores:
        if puerto == mi_puerto:
            continue  # evitar autoenvío
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.connect((host, puerto))
                s.sendall(mensaje.encode())
        except Exception as e:
            print(f"[Advertencia] No se pudo notificar a {host}:{puerto} -> {e}")

def manejar_cliente(cliente_socket, direccion, mi_puerto):
    global reloj_lamport
    try:
        datos = cliente_socket.recv(1024)
        if not datos:
            print(f"[Aviso] Conexión vacía desde {direccion}")
            return

        mensaje = datos.decode().strip()
        partes = mensaje.split(":")
        tipo_mensaje = partes[0]
        reloj_recibido = int(partes[1])

        sincronizar_reloj(reloj_recibido)

        if tipo_mensaje == "RESERVA":
            cola_solicitudes.put((reloj_lamport, direccion))
            print(f"[Reserva recibida] {direccion} @ Reloj: {reloj_lamport}")

            notificar_servidores(f"CONFIRMACION:{reloj_lamport}", mi_puerto)

            respuesta = f"✅ Reserva confirmada. Reloj: {reloj_lamport}"
            cliente_socket.sendall(respuesta.encode())

    except Exception as e:
        print(f"[Error cliente {direccion}] {e}")
    finally:
        cliente_socket.shutdown(socket.SHUT_RDWR)
        cliente_socket.close()

def manejar_servidores(servidor_socket):
    while True:
        conn, _ = servidor_socket.accept()
        try:
            mensaje = conn.recv(1024).decode()
            if not mensaje:
                continue
            tipo, reloj = mensaje.strip().split(":")
            if tipo == "CONFIRMACION":
                sincronizar_reloj(int(reloj))
                print(f"[Sync] Confirmación recibida. Nuevo reloj: {reloj_lamport}")
        except Exception as e:
            print(f"[Error servidor] {e}")
        finally:
            conn.close()

def iniciar_servidor(puerto):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("0.0.0.0", puerto))  # Acepta conexiones de otros contenedores
    s.listen(10)
    print(f"[Boot] Servidor escuchando en puerto {puerto}")

    threading.Thread(target=manejar_servidores, args=(s,), daemon=True).start()

    while True:
        cliente_socket, direccion = s.accept()
        threading.Thread(target=manejar_cliente, args=(cliente_socket, direccion, puerto), daemon=True).start()

if __name__ == "__main__":
    puerto_servidor = int(sys.argv[1]) if len(sys.argv) > 1 else 6001
    iniciar_servidor(puerto_servidor)
