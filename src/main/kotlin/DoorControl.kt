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
 * 22/5/2023
 *
 *
 * Responsible for controlling the state of the door mechanism.
 * @author Bernardo Pereira
 * @author António Paulino
 * @see SerialEmitter
 */

object DoorMechanism {

    /**
     * Represents the open command for the door mechanism.
     */
    private const val OPENCMD_FRAME = 0b00001

    /**
     * Represents the close command for the door mechanism.
     */
    private const val CLOSECMD_FRAME = 0b00000



    /**
     * Initializes the door mechanism by closing the door.
     */
    fun init() {
        close(AccessControlSystem.DOOR_CLOSE_SPEED)
    }


    /**
     * Opens the door with the specified velocity
     * @param velocity The velocity at which the door is opened
     */
    fun open(velocity: Int) = SerialEmitter.send(Destination.DOOR, OPENCMD_FRAME or velocity.shl(1))


    /**
     * Closes the door with the specified velocity
     * @param velocity The velocity at which the door is closed
     */
    fun close(velocity: Int) = SerialEmitter.send(Destination.DOOR, CLOSECMD_FRAME or velocity.shl(1))


    /**
     * Checks if the door mechanism has finished the close/open operation
     * @return false if the door mechanism is busy, true otherwise
     */
    fun finished(): Boolean = !SerialEmitter.isBusy()


}