import java.io.Serial


object DoorMechanism { // Controla o estado do mecanismo de abertura da porta.
    var busy = false
    // Inicia a classe, estabelecendo os valores iniciais.
    fun init() {
        busy = SerialEmitter.isBusy()
    }
    // Envia comando para abrir a porta, com o parâmetro de velocidade
    fun open(velocity: Int) = SerialEmitter.send(SerialEmitter.Destination.DOOR, 0b10000 or velocity)


    // Envia comando para fechar a porta, com o parâmetro de velocidade
    fun close(velocity: Int) = SerialEmitter.send(SerialEmitter.Destination.DOOR, 0b00000 or velocity)

    // Verifica se o comando anterior está concluído
    fun finished() : Boolean {
        busy = SerialEmitter.isBusy()
        return !busy
    }

}