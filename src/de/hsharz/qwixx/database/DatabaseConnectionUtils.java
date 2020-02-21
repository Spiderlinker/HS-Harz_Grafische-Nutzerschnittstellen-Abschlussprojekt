package de.hsharz.qwixx.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionUtils {

	private DatabaseConnectionUtils() {
		// Utils-Klasse
	}

	/**
	 * Schlie�t das gegebene Objekt des Types {@link AutoCloseable} und f�ngt die
	 * eventuell geworfene Exception ab (und gibt diese ggf. auf der Konsole aus)
	 * 
	 * @param c Object, das geschlossen werden soll
	 */
	public static void close(AutoCloseable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ruft {@link Connection#rollback()} auf der gegebenen {@link Connection} auf
	 * und f�ngt die eventuell geworfene Exception ab (und gibt diese ggf. auf der
	 * Konsole aus)
	 * 
	 * @param conn Connection, auf der ein rollback ausgef�hrt werden soll
	 */
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
