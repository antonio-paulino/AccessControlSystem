/**
 * 22/5/2023
 *
 * Represents a user in the system.
 *
 * @author Bernardo Pereira
 * @author António Paulino
 *
 *
 * @property UIN The User Identification Number.
 * @property PIN The Personal Identification Number of the user.
 * @property username The username of the user.
 * @property message A message stored for the user to be displayed when authenticated.
 *
 *
 *
 * @see Users
 */
data class User(val UIN: Int, var pin: Int, var username: String, var message: String?)








