import java.io.Serial
import SerialEmitter.Destination
fun main() {

    HAL.init()
    SerialEmitter.init()
    LCD.init()

    DoorMechanism.open(1)
    TUI.writeLine("OPENING", TUI.ALIGN.Center, TUI.LINES.First, true)

    while (!DoorMechanism.finished());

    LCD.clear()

    LCD.write("CLOSING...")
    DoorMechanism.close(1)

    while (!DoorMechanism.finished());
}


/**
 * Responsible for controlling the state of the door mechanism
 * @property OPENCMD Represents the open command for the door mechanism
 * @property CLOSING Represents the close command for the door mechanism
 * @property busy Indicates if the door mechanism serial receiver is busy
 * @author Bernardo Pereira
 * @author António Paulino
 * @see SerialEmitter
 */

object DoorMechanism {

    const val OPENCMD = 0b10000
    const val CLOSECMD = 0b00000

    var busy = false

    /**
     * Initializes the door mechanism by setting the busy flag to false
     */
    fun init() {
        busy = false
    }


    /**
     * Opens the door with the specified velocity
     * @param velocity The velocity at which the door is opened
     */
    fun open(velocity: Int) = SerialEmitter.send(Destination.DOOR, OPENCMD or velocity)


    /**
     * Closes the door with the specified velocity
     * @param velocity The velocity at which the door is closed
     */
    fun close(velocity: Int) = SerialEmitter.send(Destination.DOOR, CLOSECMD or velocity)


    /**
     * Checks if the door mechanism has finished the close/open operation
     * @return false if the door mechanism is busy, true otherwise
     */
    fun finished(): Boolean {
        busy = SerialEmitter.isBusy()
        return !busy
    }

}