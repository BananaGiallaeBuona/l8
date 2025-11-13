package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDeathNote {
    // Nomi costanti come da richiesta
    private static final String RAPPER = "XXXTentacion";
    private static final String PILOT = "Senna";
    private static final String PRESIDENT = "Kennedy";
    private static final String HERO = "Iron Man"; // Corretto per coerenza
    private static final String RACEDEATH = "karting accident";
    private static final String DEATHDETAILS = "ran for too long";
    private DeathnoteImpl book; // Usiamo l'implementazione

    @BeforeEach
    void setUp() {
        this.book = new DeathnoteImpl();
    }

    /* * TRACCIA 1: Regole 0 e negative non esistono.
     * TRACCIA 2: Nessuna regola è vuota o nulla.
     */
    @Test
    void testRules() {
        // Traccia 1: Verifica eccezioni per regole 0 e negative
        final IllegalArgumentException e0 = assertThrows(IllegalArgumentException.class, () -> book.getRule(0));
        assertNotNull(e0.getMessage());
        assertFalse(e0.getMessage().isBlank()); // Verifica che il messaggio non sia vuoto
        final IllegalArgumentException eNeg = assertThrows(IllegalArgumentException.class, () -> book.getRule(-1));
        assertNotNull(eNeg.getMessage());
        assertFalse(eNeg.getMessage().isBlank());
        // Traccia 2: Verifica che nessuna regola valida sia nulla o blank
        // Usiamo <= getNumberOfRules() perché le regole sono 1-based
        for (int i = 1; i <= book.getNumberOfRules(); i++) {
            final String rule = book.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank(), "Rule " + i + " should not be null or blank");
        }
    }

    /* * TRACCIA 3: L'umano il cui nome è scritto morirà.
     */
    @Test
    void testHumanDeathAndPresence() {
        final String alive = "Mario Kart";
        // 3.1: Verifica che l'umano non sia ancora stato scritto
        assertFalse(book.isNameWritten(RAPPER));
        // 3.2: Scrivi l'umano
        book.writeName(RAPPER);
        // 3.3: Verifica che l'umano sia stato scritto
        assertTrue(book.isNameWritten(RAPPER));
        // 3.4: Verifica che un altro umano non sia stato scritto
        assertFalse(book.isNameWritten(alive));
        // 3.5: Verifica che la stringa vuota non sia stata scritta
        assertFalse(book.isNameWritten(""));
    }

    /* * TRACCIA 4: Causa della morte (40ms) o attacco di cuore.
     */
    @Test
    void testDeathCause() {
        // 4.1: Controlla eccezione se si scrive la causa prima del nome
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> book.writeDeathCause("Test Cause"));
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
        // 4.2: Scrivi nome e verifica causa di default (attacco di cuore)
        book.writeName(RAPPER);
        assertEquals(book.getDefaultDeath(), book.getDeathCause(RAPPER));
        // 4.3: Scrivi altro nome e imposta causa entro 40ms
        book.writeName(PILOT);
        assertTrue(book.writeDeathCause(RACEDEATH), "Setting cause should return true within time");
        assertEquals(RACEDEATH, book.getDeathCause(PILOT));
        // 4.4: Prova a cambiare la causa dopo il timeout (100ms)
        try {
            Thread.sleep(100); // Aspetta più di 40ms
        } catch (final InterruptedException ex) {
            ex.printStackTrace(); //NOPMD
        }
        // Verifica che il tentativo di modifica fallisca (return false)
        assertFalse(book.writeDeathCause("modified cause"), "Changing cause after 40ms should fail");
        // Verifica che la causa non sia cambiata
        assertEquals(RACEDEATH, book.getDeathCause(PILOT));
    }

    /* * TRACCIA 5: Dettagli della morte (6s 40ms).
     */
    @Test
    void testDeathDetails() {
        // 5.1: Controlla eccezione se si scrivono dettagli prima del nome
        final IllegalStateException e = assertThrows(IllegalStateException.class, () -> book.writeDetails("Test Details"));
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
        // 5.2: Scrivi nome e verifica dettagli vuoti
        book.writeName(PRESIDENT);
        assertEquals("", book.getDeathDetails(PRESIDENT));
        /* * La traccia (punto 5) dice: "Dopo aver scritto la causa della morte,
         * i dettagli...". Questo implica che writeDeathCause DEVE essere
         * chiamato prima di writeDetails.
         */
        // 5.3: Imposta una causa (necessaria per avviare il timer dei dettagli)
        // Scriviamo una causa vuota per testare che il timer parta comunque
        assertTrue(book.writeDeathCause(""), "Setting an empty cause should be valid"); 
        // 5.4: Imposta dettagli entro il tempo limite
        assertTrue(book.writeDetails(DEATHDETAILS), "Setting details should return true within time");
        assertEquals(DEATHDETAILS, book.getDeathDetails(PRESIDENT));
        // 5.5: Test del timeout
        book.writeName(HERO);
        // Scriviamo la causa anche per HERO per avviare il timer dei dettagli
        assertTrue(book.writeDeathCause("Testing timeout")); 
        try {
            final int tooMuchTime = 6100;
            Thread.sleep(tooMuchTime); // Aspetta più di 6s 40ms
        } catch (final InterruptedException ex) {
            ex.printStackTrace(); //NOPMD
        }
        // 5.6: Prova a cambiare i dettagli e verifica che fallisca
        assertFalse(book.writeDetails("modified details"), "Changing details after 6100ms should fail");
        // 5.7: Verifica che i dettagli non siano stati modificati (sono vuoti)
        assertEquals("", book.getDeathDetails(HERO));
    }
}

/*package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class TestDeathNote {
    private static final String RAPPER = "XXXTentacion";
    private static final String PILOT = "Senna";
    private static final String PRESIDENT = "Kennedy";
    private static final String HERO = "iron man";
    private static final String RACEDEATH = "karting accident";
    private static final String DEATHDETAILS = "ran for too long";
    private final DeathnoteImpl book;

    TestDeathNote() {
        this.book = new DeathnoteImpl();
    }

    @Test
    void testRules() {
        assertThrows(IllegalArgumentException.class, () -> book.getRule(0));
        assertThrows(IllegalArgumentException.class, () -> book.getRule(-1));
        for (int i = 1; i <= book.getNumberOfRules(); i++) {
            final String rule = book.getRule(i);
            assertTrue(rule instanceof String);
            assertNotEquals(rule, "");
            assertNotEquals(rule, null);
        }
    }

    @Test
    void testPresencePeople() {
        final String alive = "Mario KArt";
        book.isNameWritten(RAPPER);
        book.writeName(RAPPER);
        assertTrue(() -> book.isNameWritten(RAPPER));
        assertFalse(() -> book.isNameWritten(alive));
        book.writeName("");
        assertFalse(() -> book.isNameWritten(""));
    }

    @Test
    void testWritingPeople() {
        assertThrows(IllegalStateException.class, () -> book.writeDeathCause(""));
        book.writeName(RAPPER);
        assertEquals(book.getDefaultDeath(), book.getDeathCause(RAPPER));
        book.writeName(PILOT);
        assertTrue(() -> book.writeDeathCause(RACEDEATH));
        assertEquals(RACEDEATH, book.getDeathCause(PILOT));
        try {
            Thread.sleep(100);
            book.writeDeathCause("modified");
            assertEquals(RACEDEATH, book.getDeathCause(PILOT));
        } catch (final InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }
    }

    @Test
    void testDetails() {
        assertThrows(IllegalStateException.class, () -> book.writeDetails(""));
        book.writeName(PRESIDENT);
        assertEquals("", book.getDeathDetails(PRESIDENT));
        book.writeDetails(DEATHDETAILS);
        assertTrue(() -> book.writeDetails(DEATHDETAILS));
        assertEquals(DEATHDETAILS, book.getDeathDetails(PRESIDENT));
        book.writeName(HERO);
        try {
            final int tooMuchTime = 6100;
            Thread.sleep(tooMuchTime);
            book.writeDetails("modified");
            assertEquals(DEATHDETAILS, book.getDeathDetails(PRESIDENT));
        } catch (final InterruptedException e) {
            e.printStackTrace(); //NOPMD
        }
    }
}
*/
