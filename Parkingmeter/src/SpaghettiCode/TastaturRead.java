package SpaghettiCode;
/* Klasse TastaturRead zum Einlesen von Daten über die Tastatur im Konsolen-Modus
 *
 * Eingelesen werden können: 
 * Texte (Strings) mit                  TastaturRead.readString()
 * einzelnen Zeichen mit                TastaturRead.readChar()
 * ganze Zahlen des Typs long mit       TastaturRead.readLong()
 * Gleitkommazahlen des Typs double mit TastaturRead.readDouble()
 * 
 * Dabei muss die Klasse TastaturRead (als Java-File) im selben Verzeichnis abgelegt sein
 * wie die Klasse in der die Dateneingaben über die Tastatur ausgeführt werden sollen.
 *
 * Bei der Eingabe von Datentypen byte, short, integer und float muss der Typ
 * durch den Casting-Operator explizit, im Resultat, umgewandelt werden.
 *
 * Ignorieren Sie die für Sie unverständlichen Programmanweisungen. Sie werden im 
 * Verlauf dieses Kurses alle Anweisungen noch kennenlernen
 *
 */

import java.io.*;

public class TastaturRead
{
    // Methode zur Eingabe eines Textes (String)
    public static String readString() 
    {
        // Deklaration der Referenzvariablen din für die Eingabe eines Input-Streams
        BufferedReader din = new BufferedReader( new InputStreamReader( System.in ) );
        System.out.flush();  // Löschen des Stream-Buffers
        // Daten einlesen und mögliche Fehler auffangen
        try 
        { 
            return din.readLine();  // eingegebenen Text zurückgeben
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Eingabe!" );
            System.out.println( e );  // möglichen Fehler anzeigen
        }
        System.out.println( "\nHier ist ein undefinierbarer Fehler aufgetreten!" );
        return new String( "" );  // Leeren String zurückgeben bei Fehler
    }

    // Methode zur Eingabe eines Zeichens (Character)
    public static char readChar() 
    {
        // Deklaration der Referenzvariablen din für die Eingabe eines Input-Streams
        BufferedReader din = new BufferedReader( new InputStreamReader( System.in ) );
        String string = "";  // String string initialisieren 
        System.out.flush();  // Löschen des Stream-Buffers
        // Daten einlesen und mögliche Fehler auffangen
        try 
        {
            string = din.readLine();  // Text einlesen
            // nur das 1. Zeichen des Strings berücksichtigen!
            if ( string.length() > 0 ) return string.charAt( 0 );
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Eingabe!" );
            System.out.println(e);  // möglichen Fehler anzeigen
        }
        return ' ';  // Leerschlag zurückgeben bei Fehler
    }
	
    // Methode zur Eingabe einer Ganzzahl vom Typ long
    public static long readLong() 
    {
        // Deklaration der Referenzvariablen din für die Eingabe eines Input-Streams
        BufferedReader din = new BufferedReader( new InputStreamReader( System.in ) );
        String string = "";  // String string initialisieren 
        System.out.flush();  // Löschen des Stream-Buffers
        try 
        { 
            string = din.readLine();  // Text einlesen
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Eingabe!" );
            System.out.println( e );  // möglichen Fehler anzeigen
        }
        try  
        {
            // Umwandlung des Strings in  einen Long-Wert
            return ( new Long( string.trim() ) ).longValue();
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Umwandlung!" );
            System.out.println( e );  // möglichen Fehler anzeigen
            return 0;  // 0 zurückgeben bei Fehler
        }
    }

    // Methode zur Eingabe einer Gleitkommazahl vom Typ double
    public static double readDouble() 
    {
        // Deklaration der Referenzvariablen din f�r die Eingabe eines Input-Streams
        BufferedReader din = new BufferedReader( new InputStreamReader( System.in ) );
        String string = "";  // String string initialisieren
        System.out.flush();  // Löschen des Stream-Buffers
        try 
        { 
            string = din.readLine();  // Text einlesen 
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Eingabe!" );
            System.out.println( e );  // möglichen Fehler anzeigen 
        }
        try 
        { 
            // Umwandlung des Strings in  einen Double-Wert
            return ( new Double( string.trim() ) ).doubleValue(); 
        }
        catch ( Exception e ) 
        { 
            System.out.println( "Fehler bei der Umwandlung!" );
            System.out.println( e );  // möglichen Fehler anzeigen
            return 0;  // 0.0 zurückgeben bei Fehler 
        }
    }
}